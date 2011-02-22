package gwt.mosaic.rebind;

import gwt.mosaic.client.beans.AbstractBeanAdapter;
import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.beans.BeanAdapterFactory;
import gwt.mosaic.client.beans.GetterMethod;
import gwt.mosaic.client.beans.SetterMethod;
import gwt.mosaic.client.util.ListenerList;

import java.beans.Introspector;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
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

	public static final String LISTENERS_SUFFIX = "Listeners";
	public static final String PROPERTY_CHANGE_SUFFIX = "Changed";

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

	private HashMap<String, JavaBeanProperty> javaBeanProperties = new HashMap<String, JavaBeanProperty>();
	private HashSet<String> properties = new HashSet<String>();
	private HashSet<String> notifyingProperties = new HashSet<String>();
	private HashSet<JClassType> beanListenerInterfaces = new HashSet<JClassType>();

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

	public void lookupNotifyingProperties(JClassType type) {
		JMethod[] methods = type.getOverridableMethods();
		for (JMethod method : methods) {
			if (!method.isPublic() || method.isStatic()) {
				continue;
			}

			JType returnType = method.getReturnType();
			JClassType classType = returnType.isClass();

			if (classType == null) {
				continue;
			}

			JClassType listenerListClassType = oracle
					.findType(ListenerList.class.getName());
			if (listenerListClassType.isAssignableFrom(classType)) {
				JParameterizedType genericType = classType.isParameterized();

				if (genericType == null) {
					continue;
				}

				JClassType[] typeArguments = genericType.getTypeArgs();

				if (typeArguments.length != 1) {
					continue;
				}

				JClassType listenerInterface = typeArguments[0].isInterface();

				if (listenerInterface == null) {
					logger.warn(typeArguments[0]
							.getParameterizedQualifiedSourceName()
							+ " listener interface is not an interface.");
					continue;
				}

				boolean hasNotifyingProperties = false;

				JMethod[] interfaceMethods = listenerInterface
						.getOverridableMethods();
				for (JMethod interfaceMethod : interfaceMethods) {
					String interfaceMethodName = interfaceMethod.getName();

					if (interfaceMethodName.endsWith(PROPERTY_CHANGE_SUFFIX)) {
						String propertyName = interfaceMethodName.substring(0,
								interfaceMethodName.length()
										- PROPERTY_CHANGE_SUFFIX.length());
						if (properties.contains(propertyName)) {
							notifyingProperties.add(propertyName);

							if (!hasNotifyingProperties) {
								hasNotifyingProperties = true;
							}
						}
					}
				}

				if (hasNotifyingProperties) {
					beanListenerInterfaces.add(listenerInterface);
				}
			}
		}
	}

	void lookupJavaBeanPropertyAccessors(JClassType type) {
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
					JType parameterType = parameter.getType().getErasedType();
					if (parameterType.isPrimitive() != null) {
						propertyType = parameterType.isPrimitive()
								.getQualifiedBoxedSourceName();
					} else {
						propertyType = parameterType
								.getParameterizedQualifiedSourceName();
					}
				} else {
					warn("Property '" + name + "' has " + parameters.length
							+ " parameters: " + parameters + "!");
					continue;
				}

				JavaBeanProperty property = javaBeanProperties.get(name + '#'
						+ propertyType);

				if (property == null) {
					property = new JavaBeanProperty(name);
					javaBeanProperties.put(name + '#' + propertyType, property);
				}

				property.setter = method;

				if (property.propertyType == null) {
					property.propertyType = propertyType;
				}

			} else if (method.getName().startsWith("get")
					&& method.getParameters().length == 0) {

				String name = Introspector.decapitalize(method.getName()
						.substring(3));

				String propertyType = null;
				JType retType = method.getReturnType().getErasedType();
				if (retType.isPrimitive() != null) {
					propertyType = retType.isPrimitive()
							.getQualifiedBoxedSourceName();
				} else {
					propertyType = retType
							.getParameterizedQualifiedSourceName();
				}

				JavaBeanProperty property = javaBeanProperties.get(name + "#"
						+ propertyType);

				if (property == null) {
					property = new JavaBeanProperty(name);
					javaBeanProperties.put(name + "#" + propertyType, property);
				}

				property.getter = method;

				if (property.propertyType == null) {
					property.propertyType = propertyType;
				}

				properties.add(name);

			} else if (method.getName().startsWith("is")
					&& method.getParameters().length == 0) {

				String name = Introspector.decapitalize(method.getName()
						.substring(2));

				String propertyType = null;
				JType retType = method.getReturnType().getErasedType();
				if (retType.isPrimitive() != null) {
					propertyType = retType.isPrimitive()
							.getQualifiedBoxedSourceName();
				} else {
					propertyType = retType
							.getParameterizedQualifiedSourceName();
				}

				JavaBeanProperty property = javaBeanProperties.get(name + "#"
						+ propertyType);

				if (property == null) {
					property = new JavaBeanProperty(name);
					javaBeanProperties.put(name + "#" + propertyType, property);
				}

				property.getter = method;

				if (property.propertyType == null) {
					property.propertyType = propertyType;
				}

				properties.add(name);
			}
		}
	}

	public void writeBeanAdapter(IndentedWriter w)
			throws UnableToCompleteException {
		writePackage(w);
		w.newline();

		writeImports(w);
		w.newline();

		writeClassOpen(w);
		w.indent();

		w.newline();
		writeBeanAdapterFactoryRegistration(w);
		w.newline();

		// default constructor
		w.write("public %s() {", implClassName);
		w.indent();

		Collection<JavaBeanProperty> javaBeanProperties = this.javaBeanProperties
				.values();

		for (JavaBeanProperty property : javaBeanProperties) {
			if (property.getter != null) {
				writeGetter(w, property);
			}
			if (property.setter != null) {
				writeSetter(w, property);
			}
		}

		w.newline();
		writeNotifyingProperties(w);
		w.newline();

		w.newline();
		writeBeanListenerProxies(w);
		w.newline();

		// close default constructor
		w.outdent();
		w.write("}");

		writeRegisterBeanListenersMethod(w);
		writeUnregisterBeanListenersMethod(w);

		// close class
		w.outdent();
		w.write("}");
	}

	private void writeUnregisterBeanListenersMethod(IndentedWriter w) {
		w.write("@Override");
		w.write("public void unregisterBeanListeners() {");
		w.indent();

		for (JClassType listenerInterface : beanListenerInterfaces) {
			w.write("bean.get%ss().remove((%s) beanListenerProxies.get(%s.class));",
					listenerInterface.getName(),
					listenerInterface.getParameterizedQualifiedSourceName(),
					listenerInterface.getQualifiedSourceName());
		}

		w.outdent();
		w.write("}");
	}

	private void writeRegisterBeanListenersMethod(IndentedWriter w) {
		w.write("@Override");
		w.write("public void registerBeanListeners() {");
		w.indent();

		for (JClassType listenerInterface : beanListenerInterfaces) {
			w.write("bean.get%ss().add((%s) beanListenerProxies.get(%s.class));",
					listenerInterface.getName(),
					listenerInterface.getParameterizedQualifiedSourceName(),
					listenerInterface.getQualifiedSourceName());
		}

		w.outdent();
		w.write("}");
	}

	private void writeBeanListenerProxies(IndentedWriter w) {
		for (JClassType listenerInterface : beanListenerInterfaces) {
			try {
				oracle.getType(listenerInterface.getQualifiedSourceName()
						+ ".Adapter");
				w.write("beanListenerProxies.put(%s.class, new %s.Adapter() {",
						listenerInterface.getQualifiedSourceName(),
						listenerInterface.getParameterizedQualifiedSourceName());
			} catch (NotFoundException e) {
				logger.warn(e.getMessage());
				w.write("beanListenerProxies.put(%s.class, new %s() {",
						listenerInterface.getQualifiedSourceName(),
						listenerInterface.getParameterizedQualifiedSourceName());
			}
			w.indent();

			JMethod[] interfaceMethods = listenerInterface
					.getOverridableMethods();
			for (JMethod interfaceMethod : interfaceMethods) {
				String interfaceMethodName = interfaceMethod.getName();

				if (interfaceMethodName.endsWith(PROPERTY_CHANGE_SUFFIX)) {
					String propertyName = interfaceMethodName.substring(0,
							interfaceMethodName.length()
									- PROPERTY_CHANGE_SUFFIX.length());
					if (properties.contains(propertyName)) {

						StringBuilder sb = new StringBuilder();
						JType[] parameterTypes = interfaceMethod
								.getParameterTypes();
						for (int i = 0; i < parameterTypes.length; i++) {
							JType parameterType = parameterTypes[i];
							if (i == parameterTypes.length - 1) {
								sb.append(
										parameterType
												.getParameterizedQualifiedSourceName())
										.append(" arg").append(i);
							} else {
								sb.append(
										parameterType
												.getParameterizedQualifiedSourceName())
										.append(" arg").append(i).append(", ");
							}
						}

						w.write("@Override");
						w.write("public %s %sChanged(%s) {", interfaceMethod
								.getReturnType()
								.getParameterizedQualifiedSourceName(),
								propertyName, sb.toString());
						w.indent();

						w.write("propertyChangeListeners.propertyChanged(bean, \"%s\");",
								propertyName);

						w.outdent();
						w.write("}");
					}
				}
			}

			w.outdent();
			w.write("});");
		}

	}

	private void writeNotifyingProperties(IndentedWriter w) {
		for (String notifyingProperty : notifyingProperties) {
			w.write("notifyingProperties.add(\"%s\");", notifyingProperty);
		}
	}

	private void writeBeanAdapterFactoryRegistration(IndentedWriter w) {
		w.write("static {");
		w.indent();
		w.write("BeanAdapterFactory.register(%s.class, new BeanAdapterFactory<%s>() {",
				beanType.getParameterizedQualifiedSourceName(),
				beanType.getParameterizedQualifiedSourceName());
		w.indent();
		w.write("@Override");
		w.write("protected BeanAdapter<%s> create(Object value) {",
				beanType.getParameterizedQualifiedSourceName());
		w.indent();
		w.write("BeanAdapter<%s> adapter = GWT.create(%s.class);",
				beanType.getParameterizedQualifiedSourceName(),
				baseClass.getParameterizedQualifiedSourceName());
		w.write("adapter.setBean((%s)value);",
				beanType.getParameterizedQualifiedSourceName());
		w.write("return adapter;");
		w.outdent();
		w.write("}");
		w.outdent();
		w.write("});");
		w.outdent();
		w.write("}");
	}

	private void writeSetter(IndentedWriter w, JavaBeanProperty property) {
		if (property.getter != null) {
			w.write("setterMap.put(\"%s\", new SetterMethod() {", property.name);
		} else {
			w.write("setterMap.put(\"%s\", new SetterMethod() {", property.name
					+ "#" + property.propertyType, property.propertyType);
		}
		w.indent();

		w.write("@Override");
		w.write("public void invokeSetterMethod(Object value) {");
		w.indent();
		w.write("try {");
		w.indent();
		w.write("((%s) bean).%s((%s) value);",
				beanType.getParameterizedQualifiedSourceName(),
				property.setter.getName(), property.propertyType);
		w.outdent();
		w.write("} catch (Exception ex) {");
		w.indent();
		w.write("throw new RuntimeException(ex);");
		w.outdent();
		w.write("}");
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
		w.write("try {");
		w.indent();
		JType retType = property.getter.getReturnType().getErasedType();
		if (retType.isPrimitive() != null) {
			w.write("return (%s) getBean().%s();", retType.isPrimitive()
					.getQualifiedBoxedSourceName(), property.getter.getName());
		} else {
			w.write("return getBean().%s();", property.getter.getName());
		}
		w.outdent();
		w.write("} catch (Exception ex) {");
		w.indent();
		w.write("throw new RuntimeException(ex);");
		w.outdent();
		w.write("}");
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
		w.write("import %s;", BeanAdapterFactory.class.getName());
		w.write("import %s;", GetterMethod.class.getName());
		w.write("import %s;", GWT.class.getName());
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
