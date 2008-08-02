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
package com.google.gwt.libideas.resources.css;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.libideas.resources.css.ast.CssDef;
import com.google.gwt.libideas.resources.css.ast.CssEval;
import com.google.gwt.libideas.resources.css.ast.CssIf;
import com.google.gwt.libideas.resources.css.ast.CssMediaRule;
import com.google.gwt.libideas.resources.css.ast.CssNode;
import com.google.gwt.libideas.resources.css.ast.CssPageRule;
import com.google.gwt.libideas.resources.css.ast.CssProperty;
import com.google.gwt.libideas.resources.css.ast.CssRule;
import com.google.gwt.libideas.resources.css.ast.CssSelector;
import com.google.gwt.libideas.resources.css.ast.CssSprite;
import com.google.gwt.libideas.resources.css.ast.CssStylesheet;
import com.google.gwt.libideas.resources.css.ast.CssUrl;
import com.google.gwt.libideas.resources.css.ast.HasNodes;
import com.google.gwt.libideas.resources.css.ast.HasProperties;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.PositionalCondition;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.flute.parser.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Generates a CssStylesheet from the contents of a URL.
 */
public class GenerateCssAst {

  /**
   * Maps SAC CSSParseExceptions into a TreeLogger. All parsing errors will be
   * recorded in a single TreeLogger branch, which will be created only if a
   * loggable error message is emitted.
   */
  private static class Errors implements ErrorHandler {
    /**
     * A flag that controls whether or not the exec method will fail.
     */
    private boolean fatalErrorEncountered;
    private TreeLogger logger;
    private final TreeLogger parentLogger;

    /**
     * Constructor.
     * 
     * @param parentLogger the TreeLogger that should be branched to produce the
     *          CSS parsing messages.
     */
    public Errors(TreeLogger parentLogger) {
      this.parentLogger = parentLogger;
    }

    public void error(CSSParseException exception) throws CSSException {
      // TODO Since this indicates a loss of data, should this be a fatal error?
      log(TreeLogger.WARN, exception);
    }

    public void fatalError(CSSParseException exception) throws CSSException {
      log(TreeLogger.ERROR, exception);
    }

    public void log(TreeLogger.Type type, String message) {
      log(type, message, null);
    }

    public void log(TreeLogger.Type type, String message, Throwable t) {
      fatalErrorEncountered |= type == TreeLogger.Type.ERROR;
      if (parentLogger.isLoggable(type)) {
        maybeBranch();
        logger.log(type, message, t);
      }
    }

    public void warning(CSSParseException exception) throws CSSException {
      log(TreeLogger.DEBUG, exception);
    }

    private void log(TreeLogger.Type type, CSSParseException e) {
      log(type, "Line " + e.getLineNumber() + " column " + e.getColumnNumber()
          + ": " + e.getMessage());
    }

    private void maybeBranch() {
      if (logger == null) {
        logger = parentLogger.branch(TreeLogger.INFO,
            "The following problems were detected");
      }
    }
  }

  /**
   * Maps the SAC model into our own CSS AST nodes.
   */
  private static class GenerationHandler implements DocumentHandler {
    /**
     * Utility method to concatenate strings.
     */
    private static String join(Iterable<String> elements, String separator) {
      StringBuilder b = new StringBuilder();
      for (Iterator<String> i = elements.iterator(); i.hasNext();) {
        b.append(i.next());
        if (i.hasNext()) {
          b.append(separator);
        }
      }
      return b.toString();
    }

    /**
     * The stylesheet that is being composed.
     */
    private final CssStylesheet css = new CssStylesheet();

    /**
     * Accumulates CSS nodes as they are created.
     */
    private final Stack<HasNodes> currentParent = new Stack<HasNodes>();

    /**
     * Accumulates CSS properties as they are seen.
     */
    private HasProperties currentRule;

    /**
     * Used when parsing the contents of meta-styles.
     */
    private final Errors errors;

    public GenerationHandler(Errors errors) {
      this.errors = errors;
      currentParent.push(css);
    }

    public void comment(String text) throws CSSException {
      // Ignore comments
      // TODO Should comments be retained but not generally printed?
    }

    public void endDocument(InputSource source) throws CSSException {
    }

    public void endFontFace() throws CSSException {
    }

    public void endMedia(SACMediaList media) throws CSSException {
      currentParent.pop();
    }

    public void endPage(String name, String pseudoPage) throws CSSException {
    }

    public void endSelector(SelectorList selectors) throws CSSException {
    }

    /**
     * Reflectively invoke a method named parseRule on this instance.
     */
    public void ignorableAtRule(String atRule) throws CSSException {
      String ruleName = atRule.substring(1, atRule.indexOf(" "));
      String methodName = "parse" + (Character.toUpperCase(ruleName.charAt(0)))
          + ruleName.substring(1);
      try {
        Method parseMethod = getClass().getDeclaredMethod(methodName,
            String.class);
        parseMethod.invoke(this, atRule);
      } catch (NoSuchMethodException e) {
        errors.log(TreeLogger.WARN, "Ignoring @" + ruleName);
      } catch (IllegalAccessException e) {
        errors.log(TreeLogger.ERROR, "Unable to invoke parse method ", e);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof CSSException) {
          throw (CSSException) e.getCause();
        }

        errors.log(TreeLogger.ERROR, "Unable to invoke parse method ", e);
      }
    }

    public void importStyle(String uri, SACMediaList media,
        String defaultNamespaceURI) throws CSSException {
    }

    public void namespaceDeclaration(String prefix, String uri)
        throws CSSException {
    }

    public void property(String name, LexicalUnit value, boolean important)
        throws CSSException {
      List<String> values = new ArrayList<String>();
      if (value != null) {
        extractValueOf(values, value);
      }
      currentRule.getProperties().add(
          new CssProperty(escapeIdent(name), values, important));
    }

    public void startDocument(InputSource source) throws CSSException {
    }

    public void startFontFace() throws CSSException {
    }

    public void startMedia(SACMediaList media) throws CSSException {
      CssMediaRule r = new CssMediaRule();
      for (int i = 0; i < media.getLength(); i++) {
        r.getMedias().add(media.item(i));
      }

      pushParent(r);
    }

    public void startPage(String name, String pseudoPage) throws CSSException {
      CssPageRule r = new CssPageRule();
      // name appears to be unused in CSS2
      r.setPseudoPage(pseudoPage);
      addNode(r);
      currentRule = r;
    }

    public void startSelector(SelectorList selectors) throws CSSException {
      CssRule r = new CssRule();
      addNode(r);
      for (int i = 0; i < selectors.getLength(); i++) {
        r.getSelectors().add(new CssSelector(valueOf(selectors.item(i))));
      }
      currentRule = r;
    }

    void parseDef(String atRule) {
      // @def key value with strings
      String[] parts = atRule.substring(0, atRule.length() - 1).split("\\s");

      String[] valueParts = new String[parts.length - 2];
      System.arraycopy(parts, 2, valueParts, 0, valueParts.length);

      CssDef def = new CssDef(parts[1], join(Arrays.asList(valueParts), " "));
      addNode(def);
    }

    /**
     * The elif nodes are processed as though they were {@code @if} nodes. The
     * newly-generated CssIf node will be attached to the last CssIf in the
     * if/else chain.
     */
    void parseElif(String atRule) throws CSSException {
      List<CssNode> nodes = currentParent.peek().getNodes();
      CssIf lastIf = findLastIfInChain(nodes);

      if (lastIf == null) {
        throw new CSSException(CSSException.SAC_SYNTAX_ERR,
            "@elif must immediately follow an @if or @elif", null);
      }

      assert lastIf.getElseNodes().isEmpty();

      // @elif -> lif (because parseIf strips the first three chars)
      parseIf(atRule.substring(2));

      // Fix up the structure by remove the newly-created node from the parent
      // context and moving it to the end of the @if chain
      lastIf.getElseNodes().add(nodes.remove(nodes.size() - 1));
    }

    /**
     * The else nodes are processed as though they were written as
     * {@code @elif true} rules.
     */
    void parseElse(String atRule) throws CSSException {
      // The last CssIf in the if/else chain
      CssIf lastIf = findLastIfInChain(currentParent.peek().getNodes());

      if (lastIf == null) {
        throw new CSSException(CSSException.SAC_SYNTAX_ERR,
            "@else must immediately follow an @if or @elif", null);
      }

      // Create the CssIf to hold the @else rules
      String fakeElif = "@elif true " + atRule.substring(atRule.indexOf("{"));
      parseElif(fakeElif);
      CssIf elseIf = findLastIfInChain(currentParent.peek().getNodes());

      assert lastIf.getElseNodes().size() == 1
          && lastIf.getElseNodes().get(0) == elseIf;
      assert elseIf.getElseNodes().isEmpty();

      // Merge the rules into the last CssIf to break the chain and prevent
      // @else followed by @else
      lastIf.getElseNodes().clear();
      lastIf.getElseNodes().addAll(elseIf.getNodes());
    }

    void parseEval(String atRule) throws CSSException {
      // @eval key com.google.Type.staticFunction
      String[] parts = atRule.substring(0, atRule.length() - 1).split("\\s");

      if (parts.length != 3) {
        throw new CSSException(CSSException.SAC_SYNTAX_ERR,
            "Incorrect number of parts for @eval", null);
      }

      CssEval eval = new CssEval(parts[1], parts[2]);
      addNode(eval);
    }

    void parseIf(String atRule) throws CSSException {
      // TODO tighten this up
      String predicate = atRule.substring(3, atRule.indexOf('{') - 1).trim();
      String[] predicateParts = predicate.split("\\s");
      String blockContents = atRule.substring(atRule.indexOf('{') + 1,
          atRule.length() - 1);

      CssIf cssIf = new CssIf();
      switch (predicateParts.length) {
        case 0:
          throw new CSSException(CSSException.SAC_SYNTAX_ERR,
              "Incorrect format for @if predicate", null);
        case 1:
          if (predicateParts[0].length() == 0) {
            throw new CSSException(CSSException.SAC_SYNTAX_ERR,
                "Incorrect format for @if predicate", null);
          }
          cssIf.setExpression(predicateParts[0]);
          break;
        default:
          if (predicateParts[0].startsWith("!")) {
            cssIf.setNegated(true);
            cssIf.setProperty(predicateParts[0].substring(1));
          } else {
            cssIf.setProperty(predicateParts[0]);
          }
          String[] values = new String[predicateParts.length - 1];
          System.arraycopy(predicateParts, 1, values, 0, values.length);
          cssIf.setPropertyValues(values);
      }

      pushParent(cssIf);

      // parse the inner text
      InputSource s = new InputSource();
      s.setCharacterStream(new StringReader(blockContents));
      Parser parser = new Parser();
      parser.setDocumentHandler(this);
      parser.setErrorHandler(errors);

      try {
        parser.parseStyleSheet(s);
      } catch (IOException e) {
        throw new CSSException(CSSException.SAC_SYNTAX_ERR,
            "Unable to parse @if", e);
      }

      if (currentParent.pop() != cssIf) {
        // This is a coding error
        throw new RuntimeException("Incorrect element popped");
      }
    }

    void parseSprite(String atRule) throws CSSException {
      // @sprite className methodName
      String[] parts = atRule.substring(0, atRule.length() - 1).split("\\s");

      if (parts.length != 3) {
        throw new CSSException(CSSException.SAC_SYNTAX_ERR,
            "Incorrect number of parts for @sprite", null);
      }

      CssSprite sprite = new CssSprite(parts[1], parts[2]);
      addNode(sprite);
    }

    void parseUrl(String atRule) throws CSSException {
      // @url key dataResourceFunction
      String[] parts = atRule.substring(0, atRule.length() - 1).split("\\s");

      if (parts.length != 3) {
        throw new CSSException(CSSException.SAC_SYNTAX_ERR,
            "Incorrect number of parts for @url", null);
      }

      CssUrl url = new CssUrl(parts[1], parts[2]);
      addNode(url);
    }

    /**
     * Add a node to the current parent.
     */
    private void addNode(CssNode node) {
      currentParent.peek().getNodes().add(node);
    }

    /**
     * Expresses an rgb function as a hex expression.
     * 
     * @param colors a sequence of LexicalUnits, assumed to be
     *          <code>(INT COMMA INT COMMA INT)</code>
     * @return the minimal hex expression for the RGB color values
     */
    private String colorValue(LexicalUnit colors) {
      LexicalUnit red = colors;
      assert red.getLexicalUnitType() == LexicalUnit.SAC_INTEGER;
      LexicalUnit green = red.getNextLexicalUnit().getNextLexicalUnit();
      assert green.getLexicalUnitType() == LexicalUnit.SAC_INTEGER;
      LexicalUnit blue = green.getNextLexicalUnit().getNextLexicalUnit();
      assert blue.getLexicalUnitType() == LexicalUnit.SAC_INTEGER;

      int r = Math.min(red.getIntegerValue(), 255);
      int g = Math.min(green.getIntegerValue(), 255);
      int b = Math.min(blue.getIntegerValue(), 255);

      String sr = Integer.toHexString(r);
      if (sr.length() == 1) {
        sr = "0" + sr;
      }

      String sg = Integer.toHexString(g);
      if (sg.length() == 1) {
        sg = "0" + sg;
      }

      String sb = Integer.toHexString(b);
      if (sb.length() == 1) {
        sb = "0" + sb;
      }

      // #AABBCC --> #ABC
      if (sr.charAt(0) == sr.charAt(1) && sg.charAt(0) == sg.charAt(1)
          && sb.charAt(0) == sb.charAt(1)) {
        sr = sr.substring(1);
        sg = sg.substring(1);
        sb = sb.substring(1);
      }

      return "#" + sr + sg + sb;
    }

    private String escapeIdent(String selector) {
      assert selector.length() > 0;

      StringBuilder toReturn = new StringBuilder();
      if (!isIdentStart(selector.charAt(0))) {
        toReturn.append('\\');
      }
      toReturn.append(selector.charAt(0));

      if (selector.length() > 1) {
        for (char c : selector.substring(1).toCharArray()) {
          if (!isIdentPart(c)) {
            toReturn.append('\\');
          }
          toReturn.append(c);
        }
      }
      return toReturn.toString();
    }

    private String escapeValue(String s, boolean inDoubleQuotes) {
      StringBuilder b = new StringBuilder();
      for (char c : s.toCharArray()) {
        if (Character.isISOControl(c)) {
          b.append('\\').append(Integer.toHexString(c).toUpperCase()).append(
              " ");
        } else {
          switch (c) {
            case '\'':
              // Special case a single quote in a pair of double quotes
              if (inDoubleQuotes) {
                b.append(c);
              } else {
                b.append("\\'");
              }
              break;

            case '"':
              b.append("\\\"");
              break;

            case '\\':
              b.append("\\\\");
              break;

            default:
              b.append(c);
          }
        }
      }

      return b.toString();
    }

    /**
     * Convert a LexicalUnit list into a List of Strings.
     */
    private void extractValueOf(List<String> accumulator, LexicalUnit value) {
      do {
        accumulator.add(valueOf(value));
        value = value.getNextLexicalUnit();
      } while (value != null);
    }

    /**
     * The elif and else constructs are modeled as nested if statements in the
     * CssIf's elseNodes field. This method will search a list of CssNodes and
     * remove the last chained CssIf from the last element in the list of nodes.
     */
    private CssIf findLastIfInChain(List<CssNode> nodes) {
      if (nodes.isEmpty()) {
        return null;
      }

      CssNode lastNode = nodes.get(nodes.size() - 1);
      if (lastNode instanceof CssIf) {
        CssIf asIf = (CssIf) lastNode;
        if (asIf.getElseNodes().isEmpty()) {
          return asIf;
        } else {
          return findLastIfInChain(asIf.getElseNodes());
        }
      }
      return null;
    }

    private boolean isIdentPart(char c) {
      return Character.isLetterOrDigit(c) || (c == '\\') || (c == '-');
    }

    private boolean isIdentStart(char c) {
      return Character.isLetter(c) || (c == '\\');
    }

    /**
     * Adds a node to the current parent and then makes the node the current
     * parent node.
     */
    private <T extends CssNode & HasNodes> void pushParent(T newParent) {
      addNode(newParent);
      currentParent.push(newParent);
    }

    private String valueOf(Condition condition) {
      if (condition instanceof AttributeCondition) {
        AttributeCondition c = (AttributeCondition) condition;
        switch (c.getConditionType()) {
          case Condition.SAC_ATTRIBUTE_CONDITION:
            return "[" + c.getLocalName()
                + (c.getValue() != null ? "=" + c.getValue() : "") + "]";
          case Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION:
            return "[" + c.getLocalName() + "~=" + c.getValue() + "]";
          case Condition.SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
            return "[" + c.getLocalName() + "|=" + c.getValue() + "]";
          case Condition.SAC_ID_CONDITION:
            return "#" + c.getValue();
          case Condition.SAC_CLASS_CONDITION:
            return "." + c.getValue();
          case Condition.SAC_PSEUDO_CLASS_CONDITION:
            return ":" + c.getValue();
        }

      } else if (condition instanceof CombinatorCondition) {
        CombinatorCondition c = (CombinatorCondition) condition;
        switch (condition.getConditionType()) {
          case Condition.SAC_AND_CONDITION:
            return valueOf(c.getFirstCondition())
                + valueOf(c.getSecondCondition());
          case Condition.SAC_OR_CONDITION:
            // Unimplemented in CSS2?
        }

      } else if (condition instanceof ContentCondition) {
        // Unimplemented in CSS2?

      } else if (condition instanceof LangCondition) {
        LangCondition c = (LangCondition) condition;
        return ":lang(" + c.getLang() + ")";

      } else if (condition instanceof NegativeCondition) {
        // Unimplemented in CSS2?
      } else if (condition instanceof PositionalCondition) {
        // Unimplemented in CSS2?
      }

      throw new RuntimeException("Unhandled condition of type "
          + condition.getConditionType() + " " + condition.getClass().getName());
    }

    private String valueOf(float f) {
      int i = (int) f;
      if (i == f) {
        return String.valueOf(i);
      } else {
        return String.valueOf(f);
      }
    }

    private String valueOf(LexicalUnit value) {
      List<String> parameters = new ArrayList<String>();
      switch (value.getLexicalUnitType()) {
        case LexicalUnit.SAC_ATTR:
          return "attr(" + value.getStringValue() + ")";
        case LexicalUnit.SAC_IDENT:
          return value.getStringValue();
        case LexicalUnit.SAC_STRING_VALUE:
          return '"' + escapeValue(value.getStringValue(), true) + '"';
        case LexicalUnit.SAC_RGBCOLOR:
          // flute models the commas as operators so no separator needed
          return colorValue(value.getParameters());
        case LexicalUnit.SAC_INTEGER:
          return String.valueOf(value.getIntegerValue());
        case LexicalUnit.SAC_REAL:
          return valueOf(value.getFloatValue());
        case LexicalUnit.SAC_CENTIMETER:
        case LexicalUnit.SAC_DEGREE:
        case LexicalUnit.SAC_DIMENSION:
        case LexicalUnit.SAC_EM:
        case LexicalUnit.SAC_EX:
        case LexicalUnit.SAC_GRADIAN:
        case LexicalUnit.SAC_HERTZ:
        case LexicalUnit.SAC_KILOHERTZ:
        case LexicalUnit.SAC_MILLIMETER:
        case LexicalUnit.SAC_MILLISECOND:
        case LexicalUnit.SAC_PERCENTAGE:
        case LexicalUnit.SAC_PICA:
        case LexicalUnit.SAC_PIXEL:
        case LexicalUnit.SAC_POINT:
        case LexicalUnit.SAC_RADIAN:
        case LexicalUnit.SAC_SECOND:
          return valueOf(value.getFloatValue()) + value.getDimensionUnitText();
        case LexicalUnit.SAC_URI:
          return "url(" + value.getStringValue() + ")";
        case LexicalUnit.SAC_OPERATOR_COMMA:
          return ",";
        case LexicalUnit.SAC_COUNTER_FUNCTION:
        case LexicalUnit.SAC_COUNTERS_FUNCTION:
        case LexicalUnit.SAC_FUNCTION:
          extractValueOf(parameters, value.getParameters());
          return value.getFunctionName() + "(" + join(parameters, "") + ")";
        case LexicalUnit.SAC_INHERIT:
          return "inherit";
        case LexicalUnit.SAC_OPERATOR_EXP:
          return "^";
        case LexicalUnit.SAC_OPERATOR_GE:
          return ">=";
        case LexicalUnit.SAC_OPERATOR_GT:
          return ">";
        case LexicalUnit.SAC_OPERATOR_LE:
          return "<=";
        case LexicalUnit.SAC_OPERATOR_LT:
          return "<";
        case LexicalUnit.SAC_OPERATOR_MINUS:
          return "-";
        case LexicalUnit.SAC_OPERATOR_MOD:
          return "%";
        case LexicalUnit.SAC_OPERATOR_MULTIPLY:
          return "*";
        case LexicalUnit.SAC_OPERATOR_PLUS:
          return "+";
        case LexicalUnit.SAC_OPERATOR_SLASH:
          return "/";
        case LexicalUnit.SAC_OPERATOR_TILDE:
          return "~";
        case LexicalUnit.SAC_RECT_FUNCTION:
          extractValueOf(parameters, value.getParameters());
          return "rect(" + join(parameters, "") + ")";
        case LexicalUnit.SAC_SUB_EXPRESSION:
          // Should have been taken care of by our own traversal
        case LexicalUnit.SAC_UNICODERANGE:
          // Cannot be expressed in CSS2
      }
      throw new RuntimeException("Unhandled LexicalUnit type "
          + value.getLexicalUnitType());
    }

    private String valueOf(Selector selector) {
      if (selector instanceof CharacterDataSelector) {
        // Unimplemented in CSS2?

      } else if (selector instanceof ConditionalSelector) {
        ConditionalSelector s = (ConditionalSelector) selector;
        String simpleSelector = valueOf(s.getSimpleSelector());

        if ("*".equals(simpleSelector)) {
          // Don't need the extra * for compound selectors
          return valueOf(s.getCondition());
        } else {
          return simpleSelector + valueOf(s.getCondition());
        }

      } else if (selector instanceof DescendantSelector) {
        DescendantSelector s = (DescendantSelector) selector;
        switch (s.getSelectorType()) {
          case Selector.SAC_CHILD_SELECTOR:
            if (s.getSimpleSelector().getSelectorType() == Selector.SAC_PSEUDO_ELEMENT_SELECTOR) {
              return valueOf(s.getAncestorSelector()) + ":"
                  + valueOf(s.getSimpleSelector());
            } else {
              return valueOf(s.getAncestorSelector()) + ">"
                  + valueOf(s.getSimpleSelector());
            }
          case Selector.SAC_DESCENDANT_SELECTOR:
            return valueOf(s.getAncestorSelector()) + " "
                + valueOf(s.getSimpleSelector());
        }

      } else if (selector instanceof ElementSelector) {
        ElementSelector s = (ElementSelector) selector;
        if (s.getLocalName() == null) {
          return "*";
        } else {
          return escapeIdent(s.getLocalName());
        }

      } else if (selector instanceof NegativeSelector) {
        // Unimplemented in CSS2?

      } else if (selector instanceof ProcessingInstructionSelector) {
        // Unimplemented in CSS2?

      } else if (selector instanceof SiblingSelector) {
        SiblingSelector s = (SiblingSelector) selector;
        return valueOf(s.getSelector()) + "+" + valueOf(s.getSiblingSelector());
      }

      throw new RuntimeException("Unhandled selector of type "
          + selector.getClass().getName());
    }
  }

  /**
   * Create a CssStylesheet from the contents of a URL.
   */
  public static CssStylesheet exec(TreeLogger logger, URL stylesheet)
      throws UnableToCompleteException {
    logger = logger.branch(TreeLogger.DEBUG, "Parsing CSS stylesheet "
        + stylesheet.toExternalForm());
    Parser p = new Parser();
    Errors errors = new Errors(logger);
    GenerationHandler g = new GenerationHandler(errors);
    p.setDocumentHandler(g);
    p.setErrorHandler(errors);

    try {
      p.parseStyleSheet(stylesheet.toURI().toString());
    } catch (CSSException e) {
      logger.log(TreeLogger.ERROR, "Unable to parse CSS", e);
      throw new UnableToCompleteException();
    } catch (IOException e) {
      logger.log(TreeLogger.ERROR, "Unable to parse CSS", e);
      throw new UnableToCompleteException();
    } catch (URISyntaxException e) {
      logger.log(TreeLogger.ERROR, "Unable to parse CSS", e);
      throw new UnableToCompleteException();
    }

    if (errors.fatalErrorEncountered) {
      // Logging will have been performed by the Errors instance, just exit
      throw new UnableToCompleteException();
    }

    return g.css;
  }

  /**
   * Utility class.
   */
  private GenerateCssAst() {
  }
}
