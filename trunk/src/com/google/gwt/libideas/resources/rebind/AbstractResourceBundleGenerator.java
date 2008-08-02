/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.libideas.resources.rebind;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.generator.NameFactory;
import com.google.gwt.dev.util.Util;
import com.google.gwt.libideas.resources.client.ImmutableResourceBundle;
import com.google.gwt.libideas.resources.client.ResourcePrototype;
import com.google.gwt.libideas.resources.rg.BundleResourceGenerator;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base class for creating new ResourceBundle implementations.
 */
public abstract class AbstractResourceBundleGenerator extends Generator {

  /**
   * An implementation of FieldAccumulator that immediately composes its output
   * into a StringWriter.
   */
  private static class Fields implements FieldAccumulator {
    private final NameFactory factory = new NameFactory();
    private final StringWriter buffer = new StringWriter();
    private final PrintWriter pw = new PrintWriter(buffer);

    public String addField(JType type, String name) {
      return addField(type, name, null, true, false);
    }

    public String addField(JType type, String name, String initializer,
        boolean isStatic, boolean isFinal) {

      assert Util.isValidJavaIdent(name) : name
          + " is not a valid Java identifier";

      String ident = factory.createName(name);

      pw.print("private ");

      if (isStatic) {
        pw.print("static ");
      }

      if (isFinal) {
        pw.print("final ");
      }

      pw.print(type.getQualifiedSourceName());
      pw.print(" ");
      pw.print(ident);

      if (initializer != null) {
        pw.print(" = ");
        pw.print(initializer);
      }

      pw.println(";");

      return ident;
    }

    public void addName(String name) {
      factory.addName(name);
    }

    public String toString() {
      pw.flush();
      return buffer.getBuffer().toString();
    }
  }

  public AbstractResourceBundleGenerator() {
    super();
  }

  public final String generate(TreeLogger logger, GeneratorContext context,
      String typeName) throws UnableToCompleteException {

    // The TypeOracle knows about all types in the type system
    TypeOracle typeOracle = context.getTypeOracle();

    // Get a reference to the type that the generator should implement
    JClassType sourceType = typeOracle.findType(typeName);

    // Ensure that the requested type exists
    if (sourceType == null) {
      logger.log(TreeLogger.ERROR, "Could not find requested typeName");
      throw new UnableToCompleteException();
    }

    String locale;
    try {
      locale = context.getPropertyOracle().getPropertyValue(logger, "locale");
    } catch (BadPropertyValueException e) {
      // Don't care, likely the user isn't using localization.
      locale = "";
    }

    // Pick a name for the generated class to not conflict. Enclosing class
    // names must be preserved.
    String generatedSimpleSourceName = generateSimpleSourceName(logger,
        context.getPropertyOracle(), sourceType.getName())
        + locale;

    // Begin writing the generated source.
    ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(
        sourceType.getPackage().getName(), generatedSimpleSourceName);

    // The generated class needs to be able to determine the module base URL
    f.addImport(GWT.class.getName());
    f.addImport(ResourcePrototype.class.getName());

    // Determine the interface to implement
    if (sourceType.isInterface() != null) {
      f.addImplementedInterface(sourceType.getQualifiedSourceName());

    } else {
      // The incoming type wasn't a plain interface, we don't support
      // abstract base classes
      logger.log(TreeLogger.ERROR, sourceType.getQualifiedSourceName()
          + " is not an interface.", null);
      throw new UnableToCompleteException();
    }

    // All source gets written through this Writer
    final PrintWriter out = context.tryCreate(logger,
        sourceType.getPackage().getName(), generatedSimpleSourceName);

    // If an implementation already exists, we don't need to do any work
    if (out != null) {

      /*
       * This associates the methods to implement with the class that will
       * generate the implementations of those methods.
       */
      Map<Class<? extends ResourceGenerator>, List<JMethod>> taskList = createTaskList(
          logger, typeOracle, sourceType);

      // We really use a SourceWriter since it's convenient
      SourceWriter sw = f.createSourceWriter(context, out);

      ResourceContext resourceContext = createResourceContext(logger, context,
          sourceType, generatedSimpleSourceName, sw);

      // Aggregates the field names of the resources for use with
      // ResourceBundle.getResources()
      Fields fields = new Fields();

      // Try to provide as many errors as possible before failing.
      boolean success = true;

      // Run the ResourceGenerators to generate implementations of the methods
      for (Map.Entry<Class<? extends ResourceGenerator>, List<JMethod>> entry : taskList.entrySet()) {

        ResourceGenerator rg = instantiateResourceGenerator(logger,
            entry.getKey());

        success &= invokeSingleGenerator(logger.branch(TreeLogger.DEBUG,
            "Invoking ResourceGenerator " + rg.getClass().getName()),
            resourceContext, rg, entry.getValue(), sw, fields);
      }

      if (!success) {
        throw new UnableToCompleteException();
      }

      // Bundles aren't ResourcePrototypes and don't need to be operated on
      // by the following code.
      taskList.remove(BundleResourceGenerator.class);

      // Print the accumulated field definitions
      sw.println(fields.toString());

      // Complete the IRB contract
      sw.println("public ResourcePrototype[] getResources() {");
      sw.indent();
      sw.println("return new ResourcePrototype[] {");
      sw.indent();
      for (List<JMethod> methods : taskList.values()) {
        for (JMethod m : methods) {
          sw.println(m.getName() + "(), ");
        }
      }
      sw.outdent();
      sw.println("};");
      sw.outdent();
      sw.println("}");

      // Use a switch statement as a fast map
      sw.println("public native ResourcePrototype "
          + "getResource(String name) /*-{");
      sw.indent();
      sw.println("switch (name) {");
      sw.indent();
      for (List<JMethod> list : taskList.values()) {
        for (JMethod m : list) {
          sw.println("case '" + m.getName() + "': return this.@"
              + f.getCreatedClassName() + "::" + (m.getName() + "()()") + ";");
        }
      }
      sw.outdent();
      sw.println("}");
      sw.println("return null;");
      sw.outdent();
      sw.println("}-*/;");

      // Write the generated code to disk
      sw.commit(logger);
    }

    // Return the name of the concrete class
    return f.getCreatedClassName();
  }

  /**
   * Create the ResourceContext object that will be used by
   * {@link ResourceGenerator} subclasses. This is the primary way to implement
   * custom logic in the resource generation pass.
   */
  protected abstract ResourceContext createResourceContext(TreeLogger logger,
      GeneratorContext context, JClassType resourceBundleType,
      String simpleSourceName, SourceWriter sw);

  /**
   * Given a user-defined type name, determine the type name for the generated
   * class.
   */
  protected abstract String generateSimpleSourceName(TreeLogger logger,
      PropertyOracle oracle, String sourceType)
      throws UnableToCompleteException;

  /**
   * Given an ImmutableResourceBundle subtype, compute which ResourceGenerators
   * should implement which methods.
   */
  private Map<Class<? extends ResourceGenerator>, List<JMethod>> createTaskList(
      TreeLogger logger, TypeOracle typeOracle, JClassType sourceType)
      throws UnableToCompleteException {

    Map<Class<? extends ResourceGenerator>, List<JMethod>> toReturn = new HashMap<Class<? extends ResourceGenerator>, List<JMethod>>();

    JClassType bundleType = typeOracle.findType(ImmutableResourceBundle.class.getName());
    assert bundleType != null;

    JClassType resourcePrototypeType = typeOracle.findType(ResourcePrototype.class.getName());
    assert resourcePrototypeType != null;

    // Accumulate as many errors as possible before failing
    boolean throwException = false;

    // Using overridable methods allows composition of interface types
    for (JMethod m : sourceType.getOverridableMethods()) {
      JClassType returnType = m.getReturnType().isClassOrInterface();

      if (m.getEnclosingType().equals(bundleType)) {
        // Methods that we must generate, but that are not resources
        continue;

      } else if (!m.isAbstract()) {
        // Covers the case of an abstract class base type
        continue;

      } else if (returnType == null
          || !(returnType.isAssignableTo(resourcePrototypeType) || returnType.isAssignableTo(bundleType))) {
        // Primitives and random other abstract methods
        logger.log(TreeLogger.ERROR, "Unable to implement " + m.getName()
            + " because it does not derive from "
            + resourcePrototypeType.getQualifiedSourceName() + " or "
            + bundleType.getQualifiedSourceName());
        throwException = true;
        continue;
      }

      try {
        Class<? extends ResourceGenerator> clazz = findResourceGenerator(
            logger, typeOracle, m);
        List<JMethod> generatorMethods;
        if (toReturn.containsKey(clazz)) {
          generatorMethods = toReturn.get(clazz);
        } else {
          generatorMethods = new ArrayList<JMethod>();
          toReturn.put(clazz, generatorMethods);
        }

        generatorMethods.add(m);
      } catch (UnableToCompleteException e) {
        throwException = true;
      }
    }

    if (throwException) {
      throw new UnableToCompleteException();
    }

    return toReturn;
  }

  /**
   * Given a JMethod, find the a ResourceGenerator class that will be able to
   * provide an implementation of the method.
   */
  private Class<? extends ResourceGenerator> findResourceGenerator(
      TreeLogger logger, TypeOracle typeOracle, JMethod method)
      throws UnableToCompleteException {

    JClassType resourceType = method.getReturnType().isClassOrInterface();
    assert resourceType != null;

    List<JClassType> searchTypes = new ArrayList<JClassType>();
    searchTypes.add(resourceType);

    ResourceGeneratorType generatorType = null;

    while (!searchTypes.isEmpty()) {
      JClassType current = searchTypes.remove(0);
      generatorType = current.getAnnotation(ResourceGeneratorType.class);
      if (generatorType != null) {
        break;
      }

      if (current.getSuperclass() != null) {
        searchTypes.add(current.getSuperclass());
      }

      for (JClassType t : current.getImplementedInterfaces()) {
        searchTypes.add(t);
      }
    }

    if (generatorType == null) {
      logger.log(TreeLogger.ERROR, "No @"
          + ResourceGeneratorType.class.getName()
          + " was specifed for resource type "
          + resourceType.getQualifiedSourceName() + " or its supertypes");
      throw new UnableToCompleteException();
    }

    return generatorType.value();
  }

  /**
   * Utility method to construct a ResourceGenerator that logs errors.
   */
  private <T extends ResourceGenerator> T instantiateResourceGenerator(
      TreeLogger logger, Class<T> generatorClass)
      throws UnableToCompleteException {
    try {
      return generatorClass.newInstance();
    } catch (InstantiationException e) {
      logger.log(TreeLogger.ERROR, "Unable to initialize ResourceGenerator", e);
    } catch (IllegalAccessException e) {
      logger.log(TreeLogger.ERROR, "Unable to instantiate ResourceGenerator. "
          + "Does it have a public default constructor?", e);
    }
    throw new UnableToCompleteException();
  }

  /**
   * Returns true if the invocation was successful.
   */
  private boolean invokeSingleGenerator(TreeLogger logger,
      ResourceContext resourceContext, ResourceGenerator rg,
      List<JMethod> generatorMethods, SourceWriter sw, Fields fields) {

    try {
      rg.init(
          logger.branch(TreeLogger.DEBUG, "Initializing ResourceGenerator"),
          resourceContext);
    } catch (UnableToCompleteException e) {
      return false;
    }

    // Defer failure until this phase has ended
    boolean fail = false;

    // Prepare the ResourceGenerator by telling it all methods that it is
    // expected to produce.
    for (JMethod m : generatorMethods) {
      try {
        rg.prepare(logger.branch(TreeLogger.DEBUG, "Preparing method "
            + m.getName()), m);
      } catch (UnableToCompleteException e) {
        fail = true;
      }
    }

    if (fail) {
      return false;
    }

    // Write all field values
    try {
      rg.createFields(logger.branch(TreeLogger.DEBUG, "Creating fields"),
          fields);
    } catch (UnableToCompleteException e) {
      return false;
    }

    // Create the instance variables in the IRB subclass by calling
    // writeAssignment() on the ResourceGenerator
    for (JMethod m : generatorMethods) {
      String rhs;

      try {
        rhs = rg.createAssignment(logger.branch(TreeLogger.DEBUG,
            "Creating assignment for " + m.getName()), m);
      } catch (UnableToCompleteException e) {
        fail = true;
        continue;
      }

      String ident = fields.addField(m.getReturnType().isClassOrInterface(),
          m.getName(), null, true, false);

      // Strip off all but the access modifiers
      sw.print(m.getReadableDeclaration(false, true, true, true, true));
      sw.println(" {");
      sw.indent();
      sw.println("if (" + ident + " == null) {");
      sw.indent();
      sw.println(ident + " = " + rhs + ";");
      sw.outdent();
      sw.println("}");
      sw.println("return " + ident + ";");
      sw.outdent();
      sw.println("}");
    }

    if (fail) {
      return false;
    }

    // Finalize the ResourceGenerator
    try {
      rg.finish(logger.branch(TreeLogger.DEBUG, "Finishing ResourceGenerator"));
    } catch (UnableToCompleteException e) {
      return false;
    }

    return true;
  }
}