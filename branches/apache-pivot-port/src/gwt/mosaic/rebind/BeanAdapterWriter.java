package gwt.mosaic.rebind;

import gwt.mosaic.client.beans.AbstractBeanAdapter;
import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.beans.GetterMethod;
import gwt.mosaic.client.beans.SetterMethod;

import java.beans.Introspector;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.uibinder.rebind.IndentedWriter;
import com.google.gwt.uibinder.rebind.MortalLogger;

/**
 * Write for GWT BeanAdapter generated classes.
 */
class BeanAdapterWriter {

	class JavaBeanProperty {
		String name;
		String propertyType;
		JMethod getter;
		JMethod setter;

		public JavaBeanProperty(String name) {
			this.name = name;
		}
	}

	/**
	 * The type we have been asked to generate, e.g. MyBeanAdapter.
	 */
	private final JClassType baseClass;

	/**
	 * The name of the class we're creating, e.g. MyBeanAdapterImpl.
	 */
	private final String implClassName;

	private final TypeOracle oracle;
	private final MortalLogger logger;

	private final JClassType beanType;

	public BeanAdapterWriter(JClassType baseClass, String implClassName,
			TypeOracle oracle, MortalLogger logger)
			throws UnableToCompleteException {
		this.baseClass = baseClass;
		this.implClassName = implClassName;
		this.oracle = oracle;
		this.logger = logger;

		// Check for possible misuse 'GWT.create(BeanAdapter.class)'
		JClassType beanAdapterItself = oracle.findType(BeanAdapter.class
				.getCanonicalName());
		if (beanAdapterItself.equals(baseClass)) {
			die("You must use a subtype of BeanAdapter in GWT.create(). E.g.,\n"
					+ "  interface MyBeanAdapter extends BeanAdapter<JavaBean> {}\n"
					+ "  GWT.create(MyBeanAdapter.class);");
		}

		JClassType[] beanAdapterTypes = baseClass.getImplementedInterfaces();
		if (beanAdapterTypes.length == 0) {
			throw new RuntimeException("No implemented interfaces for "
					+ baseClass.getName());
		}
		JClassType beanAdapterType = beanAdapterTypes[0];

		JClassType[] typeArgs = beanAdapterType.isParameterized().getTypeArgs();
		if (typeArgs.length != 1) {
			throw new RuntimeException(
					"Java bean type parameter is required for type %s"
							+ beanAdapterType.getName());
		}
		beanType = typeArgs[0];
	}

	public JClassType getBaseClass() {
		return baseClass;
	}

	public String getImplClassName() {
		return implClassName;
	}

	public TypeOracle getOracle() {
		return oracle;
	}

	public MortalLogger getLogger() {
		return logger;
	}

	public JClassType getBeanType() {
		return beanType;
	}

	Collection<JavaBeanProperty> lookupJavaBeanPropertyAccessors(JClassType type) {
		HashMap<String, JavaBeanProperty> properties = new HashMap<String, JavaBeanProperty>();

		JMethod[] methods = type.getOverridableMethods();
		for (JMethod method : methods) {
			if (!method.isPublic() || method.isStatic()) {
				continue;
			}
			if (method.getName().startsWith("set")
					&& method.getParameters().length == 1) {
				String name = Introspector.decapitalize(method.getName()
						.substring(3));
				String propertyType = null;
				JParameter[] parameters = method.getParameters();
				if (parameters.length == 1) {
					JParameter parameter = parameters[0];
					propertyType = parameter.getType().getErasedType()
							.getQualifiedSourceName();
				} else {
					warn("Property '" + name + "' has " + parameters.length
							+ " parameters: " + parameters + "!");
					continue;
				}
				JavaBeanProperty property = properties.get(name);
				if (property == null) {
					property = new JavaBeanProperty(name);
					properties.put(name, property);
				}
				property.setter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					warn("Property '" + name + "' has an invalid setter: "
							+ propertyType + " was excpected, "
							+ property.propertyType + " found!");
					continue;
				}
			} else if (method.getName().startsWith("get")
					&& method.getParameters().length == 0) {
				String name = Introspector.decapitalize(method.getName()
						.substring(3));
				String propertyType = method.getReturnType().getErasedType()
						.getQualifiedSourceName();
				JavaBeanProperty property = properties.get(name);
				if (property == null) {
					property = new JavaBeanProperty(name);
					properties.put(name, property);
				}
				property.getter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					warn("Property '" + name + "' has an invalid getter: "
							+ propertyType + " was excpected, "
							+ property.propertyType + " found!");
					continue;
				}
			} else if (method.getName().startsWith("is")
					&& method.getParameters().length == 0) {
				String name = Introspector.decapitalize(method.getName()
						.substring(2));
				String propertyType = method.getReturnType().getErasedType()
						.getQualifiedSourceName();
				JavaBeanProperty property = properties.get(name);
				if (property == null) {
					property = new JavaBeanProperty(name);
					properties.put(name, property);
				}
				property.getter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					warn("Property '" + name + "' has an invalid (is) getter: "
							+ propertyType + " was excpected, "
							+ property.propertyType + " found!");
					continue;
				}
			}
		}
		return properties.values();
	}

	public void writeBeanAdapter(IndentedWriter w,
			Collection<JavaBeanProperty> javaBeanProperties)
			throws UnableToCompleteException {
		writePackage(w);
		w.newline();

		writeImports(w);
		w.newline();

		writeClassOpen(w);
		w.indent();

		w.write("public %s() {", implClassName);
		w.indent();

		for (JavaBeanProperty property : javaBeanProperties) {
			if (property.getter != null) {
				writeGetter(w, property);
			}
			if (property.setter != null) {
				writeSetter(w, property);
			}
		}

		w.outdent();
		w.write("}");

		// close class
		w.outdent();
		w.write("}");
	}

	private void writeSetter(IndentedWriter w, JavaBeanProperty property) {
		w.write("setterMap.put(\"%s\", new SetterMethod() {", property.name);
		w.indent();

		w.write("@Override");
		w.write("public void invokeSetterMethod(Object value) {");
		w.indent();
		JType argType = property.setter.getParameters()[0].getType()
				.getErasedType();
		if (argType.isPrimitive() != null) {
			w.write("getBean().%s((%s) value);", property.setter.getName(),
					argType.isPrimitive().getQualifiedBoxedSourceName());
		} else {
			w.write("getBean().%s((%s) value);", property.setter.getName(),
					argType.getParameterizedQualifiedSourceName());
		}
		w.outdent();
		w.write("}");

		w.outdent();
		w.write("});");
	}

	private void writeGetter(IndentedWriter w, JavaBeanProperty property) {
		w.write("getterMap.put(\"%s\", new GetterMethod() {", property.name);
		w.indent();

		w.write("@Override");
		w.write("public Object invokeGetterMethod() {");
		w.indent();
		JType retType = property.getter.getReturnType().getErasedType();
		if (retType.isPrimitive() != null) {
			w.write("return (%s) getBean().%s();", retType.isPrimitive()
					.getQualifiedBoxedSourceName(), property.getter.getName());
		} else {
			w.write("return getBean().%s();", property.getter.getName());
		}
		w.outdent();
		w.write("}");

		w.outdent();
		w.write("});");
	}

	private void writeClassOpen(IndentedWriter w) {
		String s = "public class %s extends AbstractBeanAdapter<%s> implements %s {";
		w.write(s, implClassName,
				beanType.getParameterizedQualifiedSourceName(),
				baseClass.getParameterizedQualifiedSourceName());
	}

	private void writeImports(IndentedWriter w) {
		w.write("import %s;", AbstractBeanAdapter.class.getName());
		w.write("import %s;", BeanAdapter.class.getName());
		w.write("import %s;", GetterMethod.class.getName());
		w.write("import %s;", SetterMethod.class.getName());
	}

	private void writePackage(IndentedWriter w) {
		String packageName = baseClass.getPackage().getName();
		if (packageName.length() > 0) {
			w.write("package %1$s;", packageName);
		}
	}

	/**
	 * Post a warning message.
	 */
	public void warn(String message) {
		logger.warn(message);
	}

	/**
	 * Post a warning message.
	 */
	public void warn(String message, Object... params) {
		logger.warn(message, params);
	}

	/**
	 * Post an error message and halt processing. This method always throws an
	 * {@link UnableToCompleteException}
	 */
	public void die(String message) throws UnableToCompleteException {
		logger.die(message);
	}

	/**
	 * Post an error message and halt processing. This method always throws an
	 * {@link UnableToCompleteException}
	 */
	public void die(String message, Object... params)
			throws UnableToCompleteException {
		logger.die(message, params);
	}

}
