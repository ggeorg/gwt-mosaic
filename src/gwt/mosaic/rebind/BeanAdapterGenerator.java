package gwt.mosaic.rebind;

import gwt.mosaic.rebind.BeanAdapterWriter.JavaBeanProperty;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.uibinder.rebind.IndentedWriter;
import com.google.gwt.uibinder.rebind.MortalLogger;

public class BeanAdapterGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		TypeOracle oracle = context.getTypeOracle();
		JClassType interfaceType;
		try {
			interfaceType = oracle.getType(typeName);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

		String implName = interfaceType.getName().replace('.', '_') + "Impl";
		String packageName = interfaceType.getPackage().getName();
		PrintWriterManager writers = new PrintWriterManager(context, logger,
				packageName);
		PrintWriter printWriter = writers.tryToMakePrintWriterFor(implName);

		if (printWriter != null) {
			generateOnce(interfaceType, implName, printWriter, logger, oracle,
					writers);
		}

		return packageName + "." + implName;
	}

	private void generateOnce(JClassType interfaceType, String implName,
			PrintWriter printWriter, TreeLogger treeLogger, TypeOracle oracle,
			PrintWriterManager writerManager) throws UnableToCompleteException {
		MortalLogger logger = new MortalLogger(treeLogger);

		BeanAdapterWriter writer = new BeanAdapterWriter(interfaceType,
				implName, oracle, logger);
		Collection<JavaBeanProperty> javaBeanProperties = writer
				.lookupJavaBeanPropertyAccessors(writer.getBeanType());
		
		StringWriter stringWriter = new StringWriter();
		IndentedWriter niceWriter = new IndentedWriter(new PrintWriter(
				stringWriter));
		writer.writeBeanAdapter(niceWriter, javaBeanProperties);

		printWriter.print(stringWriter.toString());
		
		writerManager.commit();
	}

}
