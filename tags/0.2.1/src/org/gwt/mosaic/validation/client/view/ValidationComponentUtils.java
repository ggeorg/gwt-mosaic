/*
 * Copyright (c) 2003-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.gwt.mosaic.validation.client.view;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.validation.client.Severity;
import org.gwt.mosaic.validation.client.ValidationMessage;
import org.gwt.mosaic.validation.client.ValidationResult;
import org.gwt.mosaic.validation.client.util.ValidationUtils;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * Consists exclusively of static methods that provide convenience behavior for
 * operating on widgets that present validation data. Methods that access
 * component state utilize the {@code Widget} client property mechanism as a
 * backing store.
 * 
 * @author Karsten Lentzsch
 * 
 * @see ValidationMessage
 * @see ValidationMessage#key()
 * @see ValidationResult
 * @see ValidationResult#subResult(Object)
 * @see ValidationResult#keyMap()
 */
public final class ValidationComponentUtils {

  private static final String SEVERITY_ERROR = "ERROR".intern();
  private static final String SEVERITY_WARNING = "WARNING".intern();
  private static final String SEVERITY_OK = "OK".intern();

  // Client Property Keys **************************************************

  /**
   * The Widget client property key for the mandatory property that indicates
   * whether a widget's content is mandatory or optional.
   * 
   * @see #isMandatory(Widget)
   * @see #isMandatoryAndBlank(Widget)
   * @see #setMandatory(Widget, boolean)
   */
  private static final String MANDATORY_KEY = "validation.isMandatory";

  /**
   * The Widget client property key used to associate a widget with a set of
   * ValidationMessages. Multiple keys are allowed.
   * 
   * @see #getMessageKeys(Widget)
   * @see #setMessageKey(Widget, Object)
   * @see #setMessageKeys(Widget, Object...)
   * @see ValidationMessage#key()
   * @see ValidationResult#subResult(Object)
   * @see ValidationResult#keyMap()
   */
  private static final String MESSAGE_KEYS = "validation.messageKeys";

  /**
   * The Widget client property key for the input hint text. The text stored
   * under this key is intended to be displayed if and only if the component has
   * the focus.
   * 
   * @see #getInputHint(Widget)
   * @see #setInputHint(Widget, Object)
   */
  private static final String INPUT_HINT_KEY = "validation.inputHint";

  /**
   * The Widget client property key for the severity property. Once a widget's
   * severity state has been set by the method
   * {@link #updateWidgetTreeSeverity(LayoutPanel, ValidationResult)} it can be
   * used to display validation feedback, such as background changes, overlay
   * information, etc. See for example
   * {@link #updateWidgetTreeSeverityBackground(LayoutPanel, ValidationResult)}
   * 
   * @see #getSeverity(Widget)
   * @see #setSeverity(Widget, Severity)
   * @see #updateComponentTreeSeverity(LayoutPanel, ValidationResult)
   * @see #updateComponentTreeSeverityBackground(LayoutPanel, ValidationResult)
   */
  private static final String SEVERITY_KEY = "validation.severity";

  /**
   * The Widget client property key used to store a component's original
   * background color. The stored background can be restored later.
   * 
   * @see #getStoredBackground(JTextComponent)
   * @see #restoreBackground(JTextComponent)
   * @see #ensureCustomBackgroundStored(JTextComponent)
   * @see #setMandatoryBackground(JTextComponent)
   */
  private static final String STORED_BACKGROUND_KEY = "validation.storedBackground";

  // Instance creation ******************************************************

  private ValidationComponentUtils() {
    // Override default constructor; prevents instantiation.
  }

  // Accessing Validation Properties ****************************************

  /**
   * Returns if the widget has been marked as mandatory.
   * 
   * @param widget the widget to be checked
   * @return true if the widget has been marked as mandatory
   * 
   * @see #isMandatoryAndBlank(Widget)
   * @see #setMandatory(Widget, boolean)
   * @see #setMandatoryBackground(TextBoxBase)
   * @see #setMandatoryBorder(TextBoxBase)
   */
  public static boolean isMandatory(Widget widget) {
    // return Boolean.TRUE.equals(comp.getClientProperty(MANDATORY_KEY));
    return DOM.getElementPropertyBoolean(widget.getElement(), MANDATORY_KEY);
  }

  /**
   * Returns if the widget is a {@code TextBoxBase} with blank content and has
   * been marked as mandatory.
   * 
   * @param widget the widget to be checked
   * @return true if the widget has a blank content and has been marked as
   *         mandatory
   * 
   * @see #isMandatory(Widget)
   * @see #setMandatory(Widget, boolean)
   * @see #setMandatoryBackground(TextBoxBase)
   * @see #setMandatoryBorder(TextBoxBase)
   */
  public static boolean isMandatoryAndBlank(Widget widget) {
    if (!(widget instanceof TextBoxBase)) {
      return false;
    }
    TextBoxBase textComponent = (TextBoxBase) widget;
    return isMandatory(textComponent)
        && ValidationUtils.isBlank(textComponent.getText());
  }

  /**
   * Marks the given widget as mandatory or optional. The value will be stored
   * as a element property value.
   * 
   * @param widget the widget to be marked
   * @param mandatory true for mandatory, false for optional
   * 
   * @see #isMandatory(Widget)
   * @see #isMandatoryAndBlank(Widget)
   * @see #setMandatoryBackground(TextBoxBase)
   * @see #setMandatoryBorder(TextBoxBase)
   */
  public static void setMandatory(Widget widget, boolean mandatory) {
    boolean oldMandatory = isMandatory(widget);
    if (oldMandatory != mandatory) {
      // comp.putClientProperty(MANDATORY_KEY, Boolean.valueOf(mandatory));
      DOM.setElementPropertyBoolean(widget.getElement(), MANDATORY_KEY,
          mandatory);
    }
  }

  /**
   * Returns the widget's {@link Severity} if it has been set before. Useful for
   * validation-aware layout panels that render the widget's validation state.
   * 
   * @param widget the widget to be read
   * @return the widget's <code>Severity</code> as set before
   * 
   * @see #setSeverity(Widget, Severity)
   * @see #updateComponentTreeSeverity(LayoutPanel, ValidationResult)
   * @see #updateComponentTreeSeverityBackground(LayoutPanel, ValidationResult)
   */
  public static Severity getSeverity(Widget widget) {
    // return (Severity) comp.getClientProperty(SEVERITY_KEY);
    String str = DOM.getElementProperty(widget.getElement(), SEVERITY_KEY);
    if (str != null) {
      str = str.intern();
      if (str == SEVERITY_ERROR) {
        return Severity.ERROR;
      } else if (str == SEVERITY_WARNING) {
        return Severity.WARNING;
      } else if (str == SEVERITY_OK) {
        return Severity.OK;
      }
    }
    return null;
  }

  /**
   * Marks the given widget with the specified severity. The severity will be
   * stored as an element property value. Useful for validation-aware layout
   * panels that render the widget's validation state once it has been set.
   * 
   * @param widget the widget that shall be marked
   * @param severity the widget's severity
   * 
   * @see #getSeverity(Widget)
   * @see #updateComponentTreeSeverity(LayoutPanel, ValidationResult)
   * @see #updateComponentTreeSeverityBackground(LayoutPanel, ValidationResult)
   */
  public static void setSeverity(Widget widget, Severity severity) {
    // comp.putClientProperty(SEVERITY_KEY, severity);
    if (severity == Severity.ERROR) {
      DOM.setElementProperty(widget.getElement(), SEVERITY_KEY, SEVERITY_ERROR);
    } else if (severity == Severity.WARNING) {
      DOM.setElementProperty(widget.getElement(), SEVERITY_KEY,
          SEVERITY_WARNING);
    } else if (severity == Severity.OK) {
      DOM.setElementProperty(widget.getElement(), SEVERITY_KEY, SEVERITY_OK);
    }
  }

  /**
   * Returns the message key that has been set to associate the given widget
   * with a set of ValidationMessages.
   * 
   * @param comp the widget to be requested
   * @return the widget's validation association key
   * 
   * @see #setMessageKey(Widget, Object)
   * @see ValidationMessage
   * @see ValidationMessage#key()
   * @see ValidationResult#subResult(Object)
   * @see ValidationResult#keyMap()
   */
  public static Object[] getMessageKeys(Widget comp) {
    // return (Object[]) comp.getClientProperty(MESSAGE_KEYS);
    // String value = DOM.getElementAttribute(comp.getElement(), MESSAGE_KEYS);
    // if (value != null) {
    // JSONArray array = (JSONArray) JSONParser.parse(value);
    // for (int i = 0, n = array.size(); i < n; i++) {
    // JSONString jsonStr = (JSONString) array.get(i);
    // jsonStr.stringValue();
    // }
    // }
    return null;
  }

  /**
   * Associates the given widget with the specified message key. That in turn
   * will associate the widget with all ValidationMessages that share this key.
   * The latter can be checked by comparing this key with the key provided by a
   * ValidationMessage.
   * 
   * @param widget the widget that shall be associated with the key
   * @param messageKey the key to be set
   * 
   * @see #getMessageKeys(Widget)
   * @see ValidationMessage
   * @see ValidationMessage#key()
   * @see ValidationResult#subResult(Object)
   * @see ValidationResult#keyMap()
   */
  public static void setMessageKey(Widget widget, Object messageKey) {
    // Object[] keyArray = messageKey == null ? null : new Object[]
    // {messageKey};
    // setMessageKeys(widget, keyArray);
  }

  /**
   * Associates the given component with the specified message keys. That in
   * turn will associate the component with all ValidationMessages that share
   * these keys. The latter can be checked by comparing the given (and stored)
   * keys with the key provided by a ValidationMessage.
   * 
   * @param comp the component that shall be associated with the keys
   * @param messageKeys the keys to be set
   * 
   * @see #getMessageKeys(JComponent)
   * @see com.jgoodies.validation.ValidationMessage
   * @see com.jgoodies.validation.ValidationMessage#key()
   * @see ValidationResult#subResult(Object)
   * @see ValidationResult#keyMap()
   */
  public static void setMessageKeys(Widget comp, Object... messageKeys) {
    // comp.putClientProperty(MESSAGE_KEYS, messageKeys);
  }

  /**
   * Returns the widget's input hint that is stored in a element property.
   * Useful to indicate the format of valid data to the user. <strike>The input
   * hint object can be a plain <code>String</code> or a compound object, for
   * example that is able to localize the input hint for the active {@code
   * java.util.Locale}.</strike>
   * <p>
   * To make use of this information an editor should register a listener with
   * the focus management. Whenever the focused component changes, the mechanism
   * can request the input hint for the focus owner using this service and can
   * display the result hint in the user interface.
   * 
   * @param widget the widget to be requested
   * @return the widget's input hint
   * 
   * @see #setInputHint(Widget, Object)
   */
  public static Object getInputHint(Widget widget) {
    return DOM.getElementProperty(widget.getElement(), INPUT_HINT_KEY);
  }

  /**
   * Sets the input hint for the given widget. This hint can be later retrieved
   * to indicate to the user the format of valid data for the focused widget.
   * 
   * @param comp the component to set a hint for
   * @param hint the input hint to be associated with the component
   * 
   * @see #getInputHint(Widget)
   */
  public static void setInputHint(Widget comp, Object hint) {
    assert (hint instanceof String); // XXX ggeorg
    DOM.setElementProperty(comp.getElement(), INPUT_HINT_KEY, (String) hint);
  }

  /**
   * Checks and answers if the specified widget is associated with an error
   * message in the given validation result. As a prerequisite, the widget must
   * have an <em>association key</em> set. That can be done using
   * {@link #setMessageKey(Widget, Object)} or
   * {@link #setMessageKeys(Widget, Object[])}.
   * <p>
   * <strong>Note:</strong> This method may become slow if invoked for larger
   * validation results <em>and</em> multiple widgets. In this case, it is
   * recommended to use {@link ValidationResult#keyMap()} instead. The latter
   * iterates once over the validation result and can be used later to request
   * the severity for multiple widgets in almost linear time.
   * 
   * @param widget used to get the association key from
   * @param result used to lookup the validation messages from
   * @return true if the given widget is associated with an error message
   * @throws NullPointerException if the widget or validation result is {@code
   *           null}
   * 
   * @see #hasWarning(Widget, ValidationResult)
   * @see #getMessageKeys(Widget)
   */
  public static boolean hasError(Widget widget, ValidationResult result) {
    return result.subResult(getMessageKeys(widget)).hasErrors();
  }

  /**
   * Checks and answers if the specified widget is associated with a warning
   * message in the given validation result. As a prerequisite, the widget must
   * have a <em>message key</em> set. That can be done using
   * {@link #setMessageKey(Widget, Object)} or
   * {@link #setMessageKeys(Widget, Object[])}.
   * <p>
   * <strong>Note:</strong> This method may become slow if invoked for larger
   * validation results <em>and</em> multiple widgets. In this case, it is
   * recommended to use {@link ValidationResult#keyMap()} instead. The latter
   * iterates once over the validation result and can be used later to request
   * the severity for multiple widgets in almost linear time.
   * 
   * @param widget used to get the association key from
   * @param result used to lookup the validation messages from
   * @return true if the given widget is associated with a warning message
   * @throws NullPointerException if the widget or validation result is {@code
   *           null}
   * 
   * @see #hasError(Widget, ValidationResult)
   * @see #getMessageKeys(Widget)
   */
  public static boolean hasWarning(Widget widget, ValidationResult result) {
    return result.subResult(getMessageKeys(widget)).hasWarnings();
  }

  // Predefined Component Tree Updates **************************************

  /**
   * Traverses a widget tree and sets mandatory backgrounds to text components
   * that have been marked as mandatory with
   * {@link #setMandatory(Widget, boolean)} before. The iteration starts at the
   * given container.
   * 
   * @param container the component tree root
   * 
   * @see #setMandatory(Widget, boolean)
   * @see #setMandatoryBackground(JTextComponent)
   */
  public static void updateComponentTreeMandatoryBackground(
      IndexedPanel container) {
    visitComponentTree(container, null, new MandatoryBackgroundVisitor());
  }

  /**
   * Traverses a component tree and sets mandatory backgrounds to text
   * components that have blank content and have been marked as mandatory with
   * {@link #setMandatory(JComponent, boolean)} before. The iteration starts at
   * the given container.
   * 
   * @param container the component tree root
   * 
   * @see #setMandatory(JComponent, boolean)
   * @see #setMandatoryBackground(JTextComponent)
   */
  public static void updateComponentTreeMandatoryAndBlankBackground(
      IndexedPanel container) {
    visitComponentTree(container, null,
        new MandatoryAndBlankBackgroundVisitor());
  }

  /**
   * Traverses a component tree and sets mandatory borders to text components
   * that have been marked as mandatory with
   * {@link #setMandatory(JComponent, boolean)} before. The iteration starts at
   * the given container.
   * 
   * @param container the component tree root
   * 
   * @see #setMandatory(JComponent, boolean)
   * @see #setMandatoryBorder(JTextComponent)
   */
  public static void updateComponentTreeMandatoryBorder(IndexedPanel container) {
    visitComponentTree(container, null, new MandatoryBorderVisitor());
  }

  /**
   * Traverses a component tree and sets the text component backgrounds
   * according to the severity of an associated validation result - if any. The
   * iteration starts at the given container.
   * <p>
   * 
   * The message keys used to associate components with validation messages
   * should be set using {@link #setMessageKey(JComponent, Object)} before you
   * call this method.
   * 
   * @param container the component tree root
   * @param result the validation result used to lookup the severities
   * 
   * @see #setMandatory(JComponent, boolean)
   * @see #setMessageKey(JComponent, Object)
   * @see #setMandatoryBackground(JTextComponent)
   * @see #setErrorBackground(JTextComponent)
   * @see #setWarningBackground(JTextComponent)
   */
  public static void updateComponentTreeSeverityBackground(
      IndexedPanel container, ValidationResult result) {
    visitComponentTree(container, result.keyMap(),
        new SeverityBackgroundVisitor());
  }

  /**
   * Traverses a component tree and sets the severity for all text components.
   * The iteration starts at the given container. If a validation result is
   * associated with a component, the result's severity is set. Otherwise the
   * severity is set to {@code null}. The severity is set using
   * {@link #setSeverity(JComponent, Severity)}.
   * <p>
   * 
   * Before you use this method, associate components with validation messages
   * using {@link #setMessageKey(JComponent, Object)}.
   * 
   * @param container the component tree root
   * @param result the validation result that provides the associated messages
   * 
   * @see #setSeverity(JComponent, Severity)
   */
  public static void updateComponentTreeSeverity(IndexedPanel container,
      ValidationResult result) {
    visitComponentTree(container, result.keyMap(), new SeverityVisitor());
  }

  // Visiting Text Components in a Component Tree ***************************

  /**
   * Traverses the component tree starting at the given container and invokes
   * the given visitor's <code>#visit</code> method on each instance of
   * {@link JTextComponent}. Useful to perform custom component tree updates
   * that are not already provided by the <code>#updateComponentTreeXXX</code>
   * methods.
   * <p>
   * 
   * The arguments passed to the #visit method are the visited component and its
   * associated validation subresult. This subresult is requested from the
   * specified <code>keyMap</code> using the component's message key.
   * <p>
   * 
   * Before you use this method, associate text component with validation
   * messages using {@link #setMessageKey(JComponent, Object)}.
   * 
   * @param container the component tree root
   * @param keyMap maps messages keys to associated validation results
   * @param visitor the visitor that is applied to all text components
   * 
   * @see #setMessageKey(JComponent, Object)
   * @see Visitor
   */
  public static void visitComponentTree(IndexedPanel container,
      Map<Object, ValidationResult> keyMap, Visitor visitor) {
    int componentCount = container.getWidgetCount();
    for (int i = 0; i < componentCount; i++) {
      Widget child = container.getWidget(i);
      if (child instanceof TextBoxBase) {
        visitor.visit(child, keyMap);
      } else if (child instanceof IndexedPanel) {
        visitComponentTree((IndexedPanel) child, keyMap, visitor);
      }
    }
  }

  // Helper Code ************************************************************

  /**
   * Returns the ValidationResult associated with the given widget using the
   * specified validation result key map, or {@code null} if the widget has no
   * message key set, or <code>ValidationResult.EMPTY</code> if the key map
   * contains no result for the widget.
   * 
   * @param comp the widget may be marked with a validation message keys
   * @param keyMap maps validation message keys to ValidationResults
   * @return the ValidationResult associated with the given widget as provided
   *         by the specified validation key map or {@code null} if the widget
   *         has no message key set, or <code>ValidationResult.EMPTY</code> if
   *         no result is associated with the widget
   */
  public static ValidationResult getAssociatedResult(Widget comp,
      Map<Object, ValidationResult> keyMap) {
    Object[] messageKeys = getMessageKeys(comp);
    if ((messageKeys == null) || (keyMap == null)) {
      return null;
    }
    if (messageKeys.length == 1) {
      ValidationResult result = keyMap.get(messageKeys[0]);
      return result == null ? ValidationResult.EMPTY : result; // already
      // unmodifiable
    }
    ValidationResult result = null;
    for (Object element : messageKeys) {
      ValidationResult subResult = keyMap.get(element);
      if (subResult != null) {
        if (result == null) {
          result = new ValidationResult();
        }
        result.addAllFrom(subResult);
      }
    }
    return result == null ? ValidationResult.EMPTY
        : ValidationResult.unmodifiableResult(result);
  }

  // Visitor Definition and Predefined Visitor Implementations **************

  /**
   * Describes visitors that visit a widget tree. Visitor implementations are
   * used to mark widgets, to change widget background, to associate widgets
   * with additional information; things that are not already provided by the
   * <code>#updateComponentTreeXXX</code> methods and this class' predefined
   * Visitor implementations.
   */
  public static interface Visitor {

    /**
     * Visits the given widget using the specified key map, that maps message
     * keys to associated validation subresults. Typically an implementation
     * will operate on the widget state.
     * 
     * @param widget the widget to be visited
     * @param keyMap maps messages keys to associated validation results
     */
    void visit(Widget widget, Map<Object, ValidationResult> keyMap);
  }

  /**
   * A validation visitor that sets the background color of {@code TextBoxBase}
   * widgets to mark mandatory widgets.
   */
  private static final class MandatoryBackgroundVisitor implements Visitor {

    /**
     * Sets the mandatory background to text widgets that have been marked as
     * mandatory.
     * 
     * @param widget the widget to be visited
     * @param keyMap ignored
     */
    public void visit(Widget widget, Map<Object, ValidationResult> keyMap) {
      if ((widget instanceof TextBoxBase) && isMandatory(widget)) {
        // TODO setMandatoryBackground((JTextComponent) component);
      }
    }

  }

  /**
   * A validation visitor that sets the background color of {@code TextBoxBase}
   * widgets to indicate if mandatory components have a blank text or not.
   */
  private static final class MandatoryAndBlankBackgroundVisitor implements
      Visitor {

    /**
     * Sets the mandatory background to text widgets that have been marked as
     * mandatory if the content is blank, otherwise the original background is
     * restored.
     * 
     * @param widget the widget to be visited
     * @param keyMap ignored
     */
    public void visit(Widget widget, Map<Object, ValidationResult> keyMap) {
      TextBoxBase textChild = (TextBoxBase) widget;
      if (isMandatoryAndBlank(textChild)) {
        // setMandatoryBackground(textChild);
      } else {
        // restoreBackground(textChild);
      }
    }
  }

  /**
   * A validation visitor that sets a mandatory border for {@code TextBoxBase}
   * widgets that have been marked as mandatory.
   */
  private static final class MandatoryBorderVisitor implements Visitor {

    /**
     * Sets the mandatory border to text widgets that have been marked as
     * mandatory.
     * 
     * @param widget the widget to be visited
     * @param keyMap ignored
     */
    public void visit(Widget widget, Map<Object, ValidationResult> keyMap) {
      if ((widget instanceof TextBoxBase) && isMandatory(widget)) {
        // setMandatoryBorder((JTextComponent) component);
      }
    }
  }

  /**
   * A validation visitor that sets the background color of {@code TextBoxBase}
   * widgets according to the severity of an associated validation result - if
   * any.
   */
  private static final class SeverityBackgroundVisitor implements Visitor {

    /**
     * Sets the widget background according to the associated validation result:
     * default, error, warning.
     * 
     * @param widget the widget to be visited
     * @param keyMap maps messages keys to associated validation results
     */
    public void visit(Widget widget, Map<Object, ValidationResult> keyMap) {
      Object messageKeys = getMessageKeys(widget);
      if (messageKeys == null) {
        return;
      }
      TextBoxBase textChild = (TextBoxBase) widget;
      // TODO ensureCustomBackgroundStored(textChild);
      ValidationResult result = getAssociatedResult(widget, keyMap);
      if ((result == null) || result.isEmpty()) {
        // TODO restoreBackground(textChild);
      } else if (result.hasErrors()) {
        // TODO setErrorBackground(textChild);
      } else if (result.hasWarnings()) {
        // TODO setWarningBackground(textChild);
      }
    }
  }

  /**
   * A validation visitor that sets each widget's severity according to its
   * associated validation result or to {@code null} if no message key is set
   * for the widget.
   */
  private static final class SeverityVisitor implements Visitor {

    /**
     * Sets the widget's severity according to its associated validation result,
     * or {@code null} if the widget has no message key set.
     * 
     * @param widget the widget to be visited
     * @param keyMap maps messages keys to associated validation results
     */
    public void visit(Widget widget, Map<Object, ValidationResult> keyMap) {
      ValidationResult result = getAssociatedResult(widget, keyMap);
      Severity severity = result == null ? null : result.getSeverity();
      setSeverity(widget, severity);
    }
  }

}
