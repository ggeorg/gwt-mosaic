package gwt.mosaic.rebind;

import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.beans.BeanAdapterFactory;
import gwt.mosaic.shared.Bean;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class BeanAdapterFactoryGenerator extends Generator {

	/**
	 * The {@code TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;

	/**
	 * The generator context.
	 */
	private GeneratorContext context = null;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;

		// Get the subtypes to examine
		JClassType type = null;
		try {
			type = context.getTypeOracle().getType(Object.class.getName());
		} catch (NotFoundException e) {
			this.logger.log(TreeLogger.ERROR, e.getMessage(), e);
			throw new UnableToCompleteException();
		}
		JClassType[] types = type.getSubtypes();

		ArrayList<JClassType> typeList = new ArrayList<JClassType>();
		for (JClassType classType : types) {
			if (classType.getAnnotation(Bean.class) == null) {
				continue;
			}
			typeList.add(classType);
		}

		types = new JClassType[typeList.size()];
		typeList.toArray(types);

		// we don't need the ArrayList anymore.
		typeList = null;

		try {
			return doGenerate(typeName, types);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, null, e);
			throw new UnableToCompleteException();
		}
	}

	private String doGenerate(String typeName, JClassType[] types)
			throws NotFoundException {
		TypeOracle typeOracle = context.getTypeOracle();
		JClassType type = typeOracle.getType(typeName);
		String packageName = type.getPackage().getName();
		String simpleClassName = type.getSimpleSourceName();
		String className = simpleClassName + "Impl";
		String qualifiedBeanClassName = packageName + "." + className;
		SourceWriter sourceWriter = getSourceWriter(packageName, className,
				type);
		if (sourceWriter == null) {
			return qualifiedBeanClassName;
		}
		write(sourceWriter, type, types);
		sourceWriter.commit(logger);
		return qualifiedBeanClassName;
	}

	protected SourceWriter getSourceWriter(String packageName,
			String beanClassName, JClassType superType) {
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				beanClassName);
		if (printWriter == null) {
			return null;
		}
		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				packageName, beanClassName);

		composerFactory.addImport(BeanAdapter.class.getName());
		composerFactory.addImport(BeanAdapterFactory.class.getName());
		composerFactory.addImport(GWT.class.getName());

		composerFactory.setSuperclass("BeanAdapterFactory");

		return composerFactory.createSourceWriter(context, printWriter);
	}

	private void write(SourceWriter w, JClassType type, JClassType[] types) {
		for (int i = 0; i < types.length; i++) {
			JClassType classType = types[i];
			w.println("interface %sBeanAdapter extends BeanAdapter<%s> {}",
					classType.getName(),
					classType.getParameterizedQualifiedSourceName());
		}

		w.println("static {");
		w.indent();
		for (int i = 0; i < types.length; i++) {
			JClassType classType = types[i];
			w.println(
					"BeanAdapterFactory.register(%s.class, new BeanAdapterFactory<%s>() {",
					classType.getQualifiedSourceName(),
					classType.getParameterizedQualifiedSourceName());
			w.indent();
			w.println("@Override");
			w.println("protected BeanAdapter<%s> create(Object value) {",
					classType.getParameterizedQualifiedSourceName());
			w.indent();
			w.println(
					"BeanAdapter<%s> adapter = GWT.create(%sBeanAdapter.class);",
					classType.getParameterizedQualifiedSourceName(),
					classType.getName());
			w.println("adapter.setBean((%s) value);",
					classType.getParameterizedQualifiedSourceName());
			w.println("return adapter;");
			w.outdent();
			w.println("}");
			w.outdent();
			w.println("});");
		}
		w.outdent();
		w.println("}");
		
		w.println("@Override");
		w.println("protected BeanAdapter<Object> create(Object value) { throw new UnsupportedOperationException(); }");
	}

}