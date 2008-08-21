/*
 * Copyright 2008 Google Inc.
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
package com.google.gwt.libideas.resources.rg;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.DefaultTextOutput;
import com.google.gwt.dev.util.Util;
import com.google.gwt.dom.client.Element;
import com.google.gwt.libideas.resources.client.CssResource;
import com.google.gwt.libideas.resources.client.DataResource;
import com.google.gwt.libideas.resources.client.CssResource.ClassName;
import com.google.gwt.libideas.resources.client.ImageResource.ImageOptions;
import com.google.gwt.libideas.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.libideas.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.libideas.resources.css.CssGenerationVisitor;
import com.google.gwt.libideas.resources.css.GenerateCssAst;
import com.google.gwt.libideas.resources.css.ast.Context;
import com.google.gwt.libideas.resources.css.ast.CssCompilerException;
import com.google.gwt.libideas.resources.css.ast.CssDef;
import com.google.gwt.libideas.resources.css.ast.CssEval;
import com.google.gwt.libideas.resources.css.ast.CssIf;
import com.google.gwt.libideas.resources.css.ast.CssMediaRule;
import com.google.gwt.libideas.resources.css.ast.CssModVisitor;
import com.google.gwt.libideas.resources.css.ast.CssNode;
import com.google.gwt.libideas.resources.css.ast.CssProperty;
import com.google.gwt.libideas.resources.css.ast.CssRule;
import com.google.gwt.libideas.resources.css.ast.CssSelector;
import com.google.gwt.libideas.resources.css.ast.CssSprite;
import com.google.gwt.libideas.resources.css.ast.CssStylesheet;
import com.google.gwt.libideas.resources.css.ast.CssUrl;
import com.google.gwt.libideas.resources.css.ast.CssVisitor;
import com.google.gwt.libideas.resources.css.ast.HasNodes;
import com.google.gwt.libideas.resources.css.ast.CssProperty.DotPathValue;
import com.google.gwt.libideas.resources.css.ast.CssProperty.ExpressionValue;
import com.google.gwt.libideas.resources.css.ast.CssProperty.ListValue;
import com.google.gwt.libideas.resources.css.ast.CssProperty.StringValue;
import com.google.gwt.libideas.resources.css.ast.CssProperty.Value;
import com.google.gwt.libideas.resources.rebind.AbstractResourceGenerator;
import com.google.gwt.libideas.resources.rebind.ResourceContext;
import com.google.gwt.libideas.resources.rebind.ResourceGeneratorUtil;
import com.google.gwt.libideas.resources.rebind.StringSourceWriter;
import com.google.gwt.user.rebind.SourceWriter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;

/**
 * Provides implementations of CSSResources.
 */
public class CssResourceGenerator extends AbstractResourceGenerator {
  private static class ClassRenamer extends CssVisitor {
    private final Map<String, String> classReplacements;
    private final TreeLogger logger;
    private final Set<String> missingClasses;

    public ClassRenamer(TreeLogger logger, Map<String, String> classReplacements) {
      this.logger = logger.branch(TreeLogger.DEBUG, "Replacing CSS class names");
      this.classReplacements = classReplacements;
      missingClasses = new HashSet<String>(classReplacements.keySet());
    }

    @Override
    public void endVisit(CssSelector x, Context ctx) {
      String sel = x.getSelector();

      // TODO This would be simplified by having a class hierarchy for selectors
      for (Map.Entry<String, String> entry : classReplacements.entrySet()) {
        Pattern p = Pattern.compile("(.*)\\.(" + Pattern.quote(entry.getKey())
            + ")([ :>+#].*|$)");
        Matcher m = p.matcher(sel);
        if (m.find()) {
          sel = m.group(1) + "." + entry.getValue() + m.group(3);
          missingClasses.remove(entry.getKey());
        }
      }

      sel = sel.trim();
      x.setSelector(sel);
    }

    @Override
    public void endVisit(CssStylesheet x, Context ctx) {
      if (missingClasses.isEmpty()) {
        return;
      }

      TreeLogger errorLogger = logger.branch(TreeLogger.INFO,
          "The following obfuscated style classes were missing from "
              + "the source CSS file:");
      for (String c : missingClasses) {
        errorLogger.log(TreeLogger.ERROR, c + ": Fix by adding ." + c + "{}");
      }
      throw new CssCompilerException("Missing a CSS replacement");
    }
  }

  /**
   * This delegate class bypasses traversal of a node, instead traversing the
   * node's children. Any modifications made to the node list of the
   * CollapsedNode will be reflected in the original node.
   */
  private static class CollapsedNode extends CssNode implements HasNodes {

    private final List<CssNode> nodes;

    public CollapsedNode(HasNodes parent) {
      this(parent.getNodes());
    }

    public CollapsedNode(List<CssNode> nodes) {
      this.nodes = nodes;
    }

    public List<CssNode> getNodes() {
      return nodes;
    }

    public void traverse(CssVisitor visitor, Context context) {
      visitor.acceptWithInsertRemove(getNodes());
    }
  }

  /**
   * Statically evaluates {@literal @if} rules.
   */
  private static class IfEvaluator extends CssModVisitor {
    private final PropertyOracle oracle;
    private final TreeLogger logger;

    public IfEvaluator(TreeLogger logger, PropertyOracle oracle) {
      this.logger = logger.branch(TreeLogger.DEBUG,
          "Replacing property-based @if blocks");
      this.oracle = oracle;
    }

    @Override
    public void endVisit(CssIf x, Context ctx) {
      if (x.getExpression() != null) {
        // This gets taken care of by the runtime substitution visitor
      } else {
        try {
          String propValue = oracle.getPropertyValue(logger,
              x.getPropertyName());

          /*
           * If the deferred binding property's value is in the list of values
           * in the @if rule, move the rules into the @if's context.
           */
          if (Arrays.asList(x.getPropertyValues()).contains(propValue)
              ^ x.isNegated()) {
            for (CssNode n : x.getNodes()) {
              ctx.insertBefore(n);
            }
          } else {
            // Otherwise, move the else block into the if statement's position
            for (CssNode n : x.getElseNodes()) {
              ctx.insertBefore(n);
            }
          }

          // Always delete @if rules that we can statically evaluate
          ctx.removeMe();
        } catch (BadPropertyValueException e) {
          logger.log(TreeLogger.ERROR, "Unable to evaluate @if block", e);
          throw new CssCompilerException("Unable to parse CSS", e);
        }
      }
    }
  }

  /**
   * Merges rules that have matching selectors.
   */
  private static class MergeIdenticalSelectorsVisitor extends CssModVisitor {
    private final Map<String, CssRule> canonicalRules = new HashMap<String, CssRule>();
    private final List<CssRule> rulesInOrder = new ArrayList<CssRule>();

    @Override
    public boolean visit(CssIf x, Context ctx) {
      visitInNewContext(x.getNodes());
      visitInNewContext(x.getElseNodes());
      return false;
    }

    @Override
    public boolean visit(CssMediaRule x, Context ctx) {
      visitInNewContext(x.getNodes());
      return false;
    }

    @Override
    public boolean visit(CssRule x, Context ctx) {
      // Assumed to run immediately after SplitRulesVisitor
      assert x.getSelectors().size() == 1;
      CssSelector sel = x.getSelectors().get(0);

      if (canonicalRules.containsKey(sel.getSelector())) {
        CssRule canonical = canonicalRules.get(sel.getSelector());

        // Check everything between the canonical rule and this rule for common
        // properties. If there are common properties, it would be unsafe to
        // promote the rule.
        boolean hasCommon = false;
        int index = rulesInOrder.indexOf(canonical) + 1;
        assert index != 0;

        for (Iterator<CssRule> i = rulesInOrder.listIterator(index); i.hasNext()
            && !hasCommon;) {
          hasCommon = haveCommonProperties(i.next(), x);
        }

        if (!hasCommon) {
          // It's safe to promote the rule
          canonical.getProperties().addAll(x.getProperties());
          ctx.removeMe();
          return false;
        }
      }

      canonicalRules.put(sel.getSelector(), x);
      rulesInOrder.add(x);
      return false;
    }

    private void visitInNewContext(List<CssNode> nodes) {
      MergeIdenticalSelectorsVisitor v = new MergeIdenticalSelectorsVisitor();
      v.accept(nodes);
      rulesInOrder.addAll(v.rulesInOrder);
    }
  }

  /**
   * Merges rules that have identical content.
   */
  private static class MergeRulesByContentVisitor extends CssModVisitor {
    private Map<String, CssRule> rulesByContents = new HashMap<String, CssRule>();
    private final List<CssRule> rulesInOrder = new ArrayList<CssRule>();

    @Override
    public boolean visit(CssIf x, Context ctx) {
      visitInNewContext(x.getNodes());
      visitInNewContext(x.getElseNodes());
      return false;
    }

    @Override
    public boolean visit(CssMediaRule x, Context ctx) {
      visitInNewContext(x.getNodes());
      return false;
    }

    @Override
    public boolean visit(CssRule x, Context ctx) {
      StringBuilder b = new StringBuilder();
      for (CssProperty p : x.getProperties()) {
        b.append(p.getName()).append(":").append(p.getValues().getExpression());
      }

      String content = b.toString();
      CssRule canonical = rulesByContents.get(content);

      // Check everything between the canonical rule and this rule for common
      // properties. If there are common properties, it would be unsafe to
      // promote the rule.
      if (canonical != null) {
        boolean hasCommon = false;
        int index = rulesInOrder.indexOf(canonical) + 1;
        assert index != 0;

        for (Iterator<CssRule> i = rulesInOrder.listIterator(index); i.hasNext()
            && !hasCommon;) {
          hasCommon = haveCommonProperties(i.next(), x);
        }

        if (!hasCommon) {
          canonical.getSelectors().addAll(x.getSelectors());
          ctx.removeMe();
          return false;
        }
      }

      rulesByContents.put(content, x);
      rulesInOrder.add(x);
      return false;
    }

    private void visitInNewContext(List<CssNode> nodes) {
      MergeRulesByContentVisitor v = new MergeRulesByContentVisitor();
      v.accept(nodes);
      rulesInOrder.addAll(v.rulesInOrder);
    }
  }

  /**
   * Splits rules with compound selectors into multiple rules.
   */
  private static class SplitRulesVisitor extends CssModVisitor {
    @Override
    public void endVisit(CssRule x, Context ctx) {
      if (x.getSelectors().size() == 1) {
        return;
      }

      for (CssSelector sel : x.getSelectors()) {
        CssRule newRule = new CssRule();
        newRule.getSelectors().add(sel);
        newRule.getProperties().addAll(x.getProperties());
        ctx.insertBefore(newRule);
      }
      ctx.removeMe();
      return;
    }
  }

  /**
   * Replaces CssSprite nodes with CssRule nodes that will display the sprited
   * image. The real trick with spriting the images is to reuse the
   * ImageResource processing framework by requiring the sprite to be defined in
   * terms of an ImageResource.
   */
  private static class Spriter extends CssModVisitor {
    private final ResourceContext context;
    private final TreeLogger logger;

    public Spriter(TreeLogger logger, ResourceContext context) {
      this.logger = logger.branch(TreeLogger.DEBUG,
          "Creating image sprite classes");
      this.context = context;
    }

    @Override
    public void endVisit(CssSprite x, Context ctx) {
      JClassType bundleType = context.getResourceBundleType();
      String functionName = x.getResourceFunction();

      if (functionName == null) {
        logger.log(TreeLogger.ERROR, "The @sprite rule " + x.getSelectors()
            + " must specify the " + CssSprite.IMAGE_PROPERTY_NAME
            + " property");
        throw new CssCompilerException("No image property specified");
      }

      // Find the image accessor method
      JMethod imageMethod = bundleType.findMethod(functionName, new JType[0]);

      if (imageMethod == null) {
        logger.log(TreeLogger.ERROR, "Unable to find ImageResource method "
            + functionName + " in " + bundleType.getQualifiedSourceName());
        throw new CssCompilerException("Cannot find image function");
      }

      ImageOptions options = imageMethod.getAnnotation(ImageOptions.class);
      RepeatStyle repeatStyle;
      if (options != null) {
        repeatStyle = options.repeatStyle();
      } else {
        repeatStyle = RepeatStyle.None;
      }

      /*
       * This casts the ImageResource to ImageResourcePrototype to get at the
       * precomputed size metrics.
       */
      String instance = "((" + ImageResourcePrototype.class.getName() + ")("
          + context.getImplementationSimpleSourceName() + ".this."
          + functionName + "()))";

      CssRule replacement = new CssRule();
      replacement.getSelectors().addAll(x.getSelectors());
      List<CssProperty> properties = replacement.getProperties();

      if (repeatStyle == RepeatStyle.None
          || repeatStyle == RepeatStyle.Horizontal) {
        properties.add(new CssProperty("height", new ExpressionValue(instance
            + ".getHeight() + \"px\""), true));
      }

      if (repeatStyle == RepeatStyle.None
          || repeatStyle == RepeatStyle.Vertical) {
        properties.add(new CssProperty("width", new ExpressionValue(instance
            + ".getWidth() + \"px\""), true));
      }
      properties.add(new CssProperty("overflow", new StringValue("hidden"),
          true));

      String repeatText;
      switch (repeatStyle) {
        case None:
          repeatText = " no-repeat";
          break;
        case Horizontal:
          repeatText = " repeat-x";
          break;
        case Vertical:
          repeatText = " repeat-y";
          break;
        case Both:
          repeatText = " repeat";
          break;
        default:
          throw new RuntimeException("Unknown repeatStyle " + repeatStyle);
      }

      String backgroundExpression = "\"url(\\\"\" + " + instance
          + ".getURL() + \"\\\") -\" + " + instance
          + ".getLeft() + \"px -\" + " + instance + ".getTop() + \"px "
          + repeatText + "\"";
      properties.add(new CssProperty("background", new ExpressionValue(
          backgroundExpression), true));

      // Retain any user-specified properties
      properties.addAll(x.getProperties());

      ctx.replaceMe(replacement);
    }
  }

  private static class SubstitutionCollector extends CssVisitor {
    private final Map<String, CssDef> substitutions = new HashMap<String, CssDef>();

    @Override
    public void endVisit(CssDef x, Context ctx) {
      substitutions.put(x.getKey(), x);
    }

    @Override
    public void endVisit(CssEval x, Context ctx) {
      substitutions.put(x.getKey(), x);
    }

    @Override
    public void endVisit(CssUrl x, Context ctx) {
      substitutions.put(x.getKey(), x);
    }
  }

  /**
   * Substitute symbolic replacements into string values.
   */
  private static class SubstitutionReplacer extends CssVisitor {
    private final ResourceContext context;
    private final TreeLogger logger;
    private final Map<String, CssDef> substitutions;

    public SubstitutionReplacer(TreeLogger logger, ResourceContext context,
        Map<String, CssDef> substitutions) {
      this.context = context;
      this.logger = logger;
      this.substitutions = substitutions;
    }

    @Override
    public void endVisit(CssProperty x, Context ctx) {
      if (x.getValues() == null) {
        // Nothing to do
        return;
      }

      List<Value> values = x.getValues().getValues();

      for (ListIterator<Value> i = values.listIterator(); i.hasNext();) {
        Value v = i.next();

        if (!(v instanceof StringValue)) {
          // Don't try to substitute into anything other than string values
          continue;
        }

        String value = ((StringValue) v).getValue();
        CssDef def = substitutions.get(value);

        if (def == null) {
          continue;
        } else if (def instanceof CssEval) {
          i.set(new ExpressionValue(def.getValue()));
        } else if (def instanceof CssUrl) {
          String functionName = def.getValue();

          // Find the method
          JMethod method = context.getResourceBundleType().findMethod(
              functionName, new JType[0]);

          if (method == null) {
            logger.log(TreeLogger.ERROR, "Unable to find DataResource method "
                + functionName + " in "
                + context.getResourceBundleType().getQualifiedSourceName());
            throw new CssCompilerException("Cannot find data function");
          }

          String instance = "((" + DataResource.class.getName() + ")("
              + context.getImplementationSimpleSourceName() + ".this."
              + functionName + "()))";

          StringBuilder expression = new StringBuilder();
          expression.append("\"url('\" + ");
          expression.append(instance).append(".getUrl()");
          expression.append(" + \"')\"");
          i.set(new ExpressionValue(expression.toString()));

        } else {
          i.set(new StringValue(def.getValue()));
        }
      }

      x.setValue(new ListValue(values));
    }
  }

  /**
   * A lookup table of base-32 chars we use to encode CSS idents. Because CSS
   * class selectors may be case-insensitive, we don't have enough characters to
   * use a base-64 encoding.
   */
  private static final char[] BASE32_CHARS = new char[] {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
      'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-', '0',
      '1', '2', '3', '4'};

  public static void main(String[] args) {
    for (int i = 0; i < 1000; i++) {
      System.out.println(makeIdent(i));
    }
  }

  static boolean haveCommonProperties(CssRule a, CssRule b) {
    if (a.getProperties().size() == 0 || b.getProperties().size() == 0) {
      return false;
    }

    SortedSet<String> aProperties = new TreeSet<String>();
    SortedSet<String> bProperties = new TreeSet<String>();

    for (CssProperty p : a.getProperties()) {
      aProperties.add(p.getName());
    }
    for (CssProperty p : b.getProperties()) {
      bProperties.add(p.getName());
    }

    Iterator<String> ai = aProperties.iterator();
    Iterator<String> bi = bProperties.iterator();

    String aName = ai.next();
    String bName = bi.next();
    for (;;) {
      int comp = aName.compareToIgnoreCase(bName);
      if (comp == 0) {
        return true;
      } else if (comp > 0) {
        if (aName.startsWith(bName + "-")) {
          return true;
        }

        if (!bi.hasNext()) {
          break;
        }
        bName = bi.next();
      } else {
        if (bName.startsWith(aName + "-")) {
          return true;
        }
        if (!ai.hasNext()) {
          break;
        }
        aName = ai.next();
      }
    }

    return false;
  }

  private static String makeIdent(int id) {
    assert id >= 0;

    StringBuilder b = new StringBuilder();

    // Use only guaranteed-alpha characters for the first character
    b.append(BASE32_CHARS[id & 0xf]);
    id >>= 4;

    while (id != 0) {
      b.append(BASE32_CHARS[id & 0x1f]);
      id >>= 5;
    }

    return b.toString();
  }

  private String classPrefix;
  private ResourceContext context;
  private JClassType elementType;
  private boolean enableMerge;
  private boolean prettyOutput;
  private Map<JClassType, Map<String, String>> replacementsByType;
  private JClassType stringType;
  private Map<JMethod, URL> urlMap;

  @Override
  public String createAssignment(TreeLogger logger, JMethod method)
      throws UnableToCompleteException {

    URL resource = urlMap.get(method);
    SourceWriter sw = new StringSourceWriter();
    // Write the expression to create the subtype.
    sw.println("new " + method.getReturnType().getQualifiedSourceName()
        + "() {");
    sw.indent();

    // Convenience when examining the generated code.
    sw.println("// " + resource.toExternalForm());

    JClassType cssResourceSubtype = method.getReturnType().isClassOrInterface();
    Map<String, String> replacements = computeReplacementsForType(cssResourceSubtype);
    assert replacements != null;

    for (JMethod toImplement : cssResourceSubtype.getOverridableMethods()) {
      String name = toImplement.getName();
      if ("getName".equals(name) || "getText".equals(name)) {
        continue;
      }

      if (toImplement.getReturnType().equals(stringType)
          && toImplement.getParameters().length == 0) {
        writeClassAssignment(sw, toImplement, replacements);

      } else {
        logger.log(TreeLogger.ERROR, "Don't know how to implement method "
            + toImplement.getName());
        throw new UnableToCompleteException();
      }
    }

    sw.println("public String getText() {");
    sw.indent();

    String cssExpression = makeExpression(logger, cssResourceSubtype, resource,
        replacements);
    sw.println("return " + cssExpression + ";");
    sw.outdent();
    sw.println("}");

    sw.println("public String getName() {");
    sw.indent();
    sw.println("return \"" + method.getName() + "\";");
    sw.outdent();
    sw.println("}");

    sw.outdent();
    sw.println("}");

    return sw.toString();
  }

  @Override
  public void init(TreeLogger logger, ResourceContext context)
      throws UnableToCompleteException {
    this.context = context;

    try {
      PropertyOracle propertyOracle = context.getGeneratorContext().getPropertyOracle();
      String style = propertyOracle.getPropertyValue(logger,
          "CssResource.style").toLowerCase();
      prettyOutput = style.equals("pretty");

      String merge = propertyOracle.getPropertyValue(logger,
          "CssResource.enableMerge").toLowerCase();
      enableMerge = merge.equals("true");

      classPrefix = propertyOracle.getPropertyValue(logger,
          "CssResource.globalPrefix");
    } catch (BadPropertyValueException e) {
      logger.log(TreeLogger.WARN, "Unable to query module property", e);
      throw new UnableToCompleteException();
    }

    if ("default".equals(classPrefix)) {
      // Compute it later in computeObfuscatedNames();
      classPrefix = null;
    } else if ("empty".equals(classPrefix)) {
      classPrefix = "";
    }

    // Find all of the types that we care about in the type system
    TypeOracle typeOracle = context.getGeneratorContext().getTypeOracle();
    elementType = typeOracle.findType(Element.class.getName());
    assert elementType != null;

    stringType = typeOracle.findType(String.class.getName());
    assert stringType != null;

    replacementsByType = new IdentityHashMap<JClassType, Map<String, String>>();
    urlMap = new IdentityHashMap<JMethod, URL>();

    computeObfuscatedNames(logger);
  }

  @Override
  public void prepare(TreeLogger logger, JMethod method)
      throws UnableToCompleteException {

    URL[] resources = ResourceGeneratorUtil.findResources(logger, context,
        method);

    if (resources.length != 1) {
      logger.log(TreeLogger.ERROR, "Exactly one "
          + ResourceGeneratorUtil.METADATA_TAG + " must be specified", null);
      throw new UnableToCompleteException();
    }

    URL resource = resources[0];
    urlMap.put(method, resource);
  }

  private void computeObfuscatedNames(TreeLogger logger) {
    logger = logger.branch(TreeLogger.DEBUG, "Computing CSS class replacements");

    TypeOracle oracle = context.getGeneratorContext().getTypeOracle();
    JClassType cssResourceType = oracle.findType(CssResource.class.getName());
    assert cssResourceType != null;

    if (classPrefix == null) {
      Adler32 checksum = new Adler32();
      for (JClassType type : cssResourceType.getSubtypes()) {
        checksum.update(Util.getBytes(type.getQualifiedSourceName()));
      }
      classPrefix = "G"
          + Long.toString(checksum.getValue(), Character.MAX_RADIX);
    }

    int count = 0;
    for (JClassType type : cssResourceType.getSubtypes()) {
      Map<String, String> replacements = new HashMap<String, String>();
      replacementsByType.put(type, replacements);

      for (JMethod method : type.getMethods()) {
        String name = method.getName();
        if ("getName".equals(name) || "getText".equals(name)) {
          continue;
        }

        // The user provided the class name to use
        ClassName classNameOverride = method.getAnnotation(ClassName.class);
        if (classNameOverride != null) {
          name = classNameOverride.value();
        }

        String obfuscatedClassName;
        if (prettyOutput) {
          obfuscatedClassName = type.getQualifiedSourceName().replaceAll(
              "[.$]", "-")
              + "-" + name;
        } else {
          obfuscatedClassName = classPrefix + makeIdent(count++);
        }

        replacements.put(name, obfuscatedClassName);
        logger.log(TreeLogger.SPAM, "Mapped " + type.getQualifiedSourceName()
            + "." + name + " to " + obfuscatedClassName);
      }
    }
  }

  /**
   * Compute the mapping of original class names to obfuscated type names for a
   * given subtype of CssResource. Mappings are inherited from the type's
   * supertypes.
   */
  private Map<String, String> computeReplacementsForType(JClassType type) {
    Map<String, String> toReturn = new HashMap<String, String>();
    if (type == null) {
      return toReturn;
    }

    // Later putAlls replace current putalls
    if (replacementsByType.containsKey(type)) {
      toReturn.putAll(replacementsByType.get(type));
    }

    toReturn.putAll(computeReplacementsForType(type.getSuperclass()));
    for (JClassType superInterface : type.getImplementedInterfaces()) {
      toReturn.putAll(computeReplacementsForType(superInterface));
    }

    return toReturn;
  }

  /**
   * Create a Java expression that evaluates to the string representation of the
   * stylesheet resource.
   */
  private String makeExpression(TreeLogger logger, JClassType cssResourceType,
      URL css, Map<String, String> classReplacements)
      throws UnableToCompleteException {

    try {
      // Create the base AST
      CssStylesheet sheet = GenerateCssAst.exec(logger, css);

      // Create CSS sprites
      (new Spriter(logger, context)).accept(sheet);

      // Perform @def and @eval substitutions
      SubstitutionCollector collector = new SubstitutionCollector();
      collector.accept(sheet);

      (new SubstitutionReplacer(logger, context, collector.substitutions)).accept(sheet);

      // Evaluate @if statements based on deferred binding properties
      (new IfEvaluator(logger,
          context.getGeneratorContext().getPropertyOracle())).accept(sheet);

      // Rename css .class selectors
      (new ClassRenamer(logger, classReplacements)).accept(sheet);

      // Combine rules with identical selectors
      if (enableMerge) {
        // TODO This is an off-switch while this is being developed; remove
        (new SplitRulesVisitor()).accept(sheet);
        (new MergeIdenticalSelectorsVisitor()).accept(sheet);
        (new MergeRulesByContentVisitor()).accept(sheet);
      }

      return makeExpression(logger, sheet);

    } catch (CssCompilerException e) {
      // Take this as a sign that one of the visitors was unhappy, but only
      // log the stack trace if there's a causal (i.e. unknown) exception.
      logger.log(TreeLogger.ERROR, "Unable to process CSS",
          e.getCause() == null ? null : e);
      throw new UnableToCompleteException();
    }
  }

  /**
   * Create a Java expression that evaluates to a string representation of the
   * given node.
   */
  private <T extends CssNode & HasNodes> String makeExpression(
      TreeLogger logger, T node) throws UnableToCompleteException {
    // Generate the CSS template
    DefaultTextOutput out = new DefaultTextOutput(!prettyOutput);
    CssGenerationVisitor v = new CssGenerationVisitor(out);
    v.accept(node);

    // Generate the final Java expression
    String template = out.toString();
    StringBuilder b = new StringBuilder();
    int start = 0;
    for (Map.Entry<Integer, List<CssNode>> entry : v.getSubstitutionPositions().entrySet()) {
      // Add the static section between start and the substitution point
      b.append('"');
      b.append(Generator.escape(template.substring(start, entry.getKey())));
      b.append("\" + ");

      // Add the nodes at the substitution point
      for (CssNode x : entry.getValue()) {
        TreeLogger loopLogger = logger.branch(TreeLogger.DEBUG,
            "Performing substitution in node " + x.toString());

        if (x instanceof CssIf) {
          CssIf asIf = (CssIf) x;

          // Generate the sub-expressions
          String expression = makeExpression(loopLogger,
              new CollapsedNode(asIf));

          String elseExpression;
          if (asIf.getElseNodes().isEmpty()) {
            // We'll treat an empty else block as an empty string
            elseExpression = "\"\"";
          } else {
            elseExpression = makeExpression(loopLogger, new CollapsedNode(
                asIf.getElseNodes()));
          }

          // ((expr) ? "CSS" : "elseCSS") +
          b.append("((" + asIf.getExpression() + ") ? " + expression + " : "
              + elseExpression + ") + ");

        } else if (x instanceof CssProperty) {
          CssProperty property = (CssProperty) x;

          validateValue(loopLogger, property.getValues());

          // (expr) +
          b.append("(" + property.getValues().getExpression() + ") + ");

        } else {
          // This indicates that some magic node is slipping by our visitors
          loopLogger.log(TreeLogger.ERROR, "Unhandled substitution "
              + x.getClass());
          throw new UnableToCompleteException();
        }
      }
      start = entry.getKey();
    }

    // Add the remaining parts of the template
    b.append('"');
    b.append(Generator.escape(template.substring(start)));
    b.append('"');

    return b.toString();
  }

  /**
   * This function validates any context-sensitive Values.
   */
  private void validateValue(TreeLogger logger, Value value)
      throws UnableToCompleteException {
    if (value instanceof DotPathValue) {
      DotPathValue dot = (DotPathValue) value;
      String[] elements = dot.getPath().split("\\.");
      if (elements.length == 0) {
        logger.log(TreeLogger.ERROR, "value() functions must specify a path");
        throw new UnableToCompleteException();
      }

      JType currentType = context.getResourceBundleType();
      for (Iterator<String> i = Arrays.asList(elements).iterator(); i.hasNext();) {
        String pathElement = i.next();

        JClassType referenceType = currentType.isClassOrInterface();
        if (referenceType == null) {
          logger.log(TreeLogger.ERROR, "Cannot resolve member " + pathElement
              + " on non-reference type "
              + currentType.getQualifiedSourceName());
          throw new UnableToCompleteException();
        }

        try {
          JMethod m = referenceType.getMethod(pathElement, new JType[0]);
          currentType = m.getReturnType();
        } catch (NotFoundException e) {
          logger.log(TreeLogger.ERROR, "Could not find no-arg method named "
              + pathElement + " in type "
              + currentType.getQualifiedSourceName());
          throw new UnableToCompleteException();
        }
      }
    }
  }

  /**
   * Write the CssResource accessor method for simple String return values.
   */
  private void writeClassAssignment(SourceWriter sw, JMethod toImplement,
      Map<String, String> classReplacements) {

    String name = toImplement.getName();
    ClassName nameOverride = toImplement.getAnnotation(ClassName.class);
    if (nameOverride != null) {
      name = nameOverride.value();
    }

    String replacement = classReplacements.get(name);
    assert replacement != null;

    sw.println(toImplement.getReadableDeclaration(false, true, true, true, true)
        + "{");
    sw.indent();
    sw.println("return \"" + replacement + "\";");
    sw.outdent();
    sw.println("}");
  }
}
