/*
 * Copyright 2006 Google Inc.
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

package com.google.gwt.libideas.validation.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.libideas.validation.client.validator.BuiltInValidator;
import com.google.gwt.libideas.validation.client.validator.BuiltInValidatorMessages;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Allows users to include Validations in their client or server side code. The
 * <code>Validation</code> class serves as the bookkeeper for the validation
 * system. Each <code>Validation</code> is composed of <code>Validators</code>,
 * <code>Subjects</code>, and a single <code>ErrorHandler</code>. When the
 * <code>Validation</code> is validated, it takes the cross-product of
 * (Validator,Validatables) and calls <code>Validator.validate</code> on each
 * pair. If the <code>Validator</code> throws a
 * <code>ValidationException</code> the <code>Validation</code> fills in
 * some extra information about the exception and passes it into its
 * <code>ErrorHandler</code>.
 * 
 */
public class Validation implements ClickListener, FocusListener, ChangeListener {

  protected static boolean setup = false;
  protected static ErrorHandler defaultErrorHandler;

  static {
    if (inGWTModule()) {
      setupInBrowser();
    }
  }

  /**
   * Creates a new Validation for the given text box, which is checked when the
   * text box is focused.
   */
  public static Validation addAsFocusListener(TextBox box, Validator validator) {
    BuiltInTextBoxSubject target = new BuiltInTextBoxSubject(box);
    Validation v = new Validation(target, validator);
    box.addFocusListener(v);
    return v;
  }

  /**
   * Gets the default error handler.
   * @return the default error handler.
   */
  public static ErrorHandler getDefaultErrorHandler() {
    return defaultErrorHandler;
  }

  // Joel: Is there a better way to do this?
  private static boolean inGWTModule() {
    try {
      GWT.getModuleName();
      // This is actual a UnsatifiedLinkError, but GWT does not have that class.
    } catch (Error l) {
      return false;
    }
    return true;
  }

  private static void setupInBrowser() {
    if (setup == false) {
      setup = true;
      defaultErrorHandler = new DefaultErrorHandler();
      BuiltInValidator.setMessages((BuiltInValidatorMessages) GWT.create(BuiltInValidatorMessages.class));
    }
  }

  protected ErrorHandler handler = defaultErrorHandler;

  List children = null;

  List validatables = new ArrayList();

  List validators = new ArrayList();

  /**
   * Constructor for <code>Validation</code>.
   */
  public Validation() {
    // Default constructor.
  }

  /**
   * Constructor for <code>Validation</code>.
   */
  public Validation(Subject subject) {
    addSubject(subject);
  }

  /**
   * Constructor for <code>Validation</code>.
   */
  public Validation(Subject subject, Validator validator) {
    addSubject(subject);
    addValidator(validator);
  }

  public Validation(Subject subject, Validator validator,
      ErrorHandler errorHandler) {
    this(subject, validator);
    setErrorHandler(errorHandler);
  }

  /**
   * Constructor for <code>Validation</code>.
   */
  public void addChild(Validation validation) {
    if (children == null) {
      children = new ArrayList();
    }
    children.add(validation);
  }

  public void addSubject(Subject validatable) {
    validatables.add(validatable);
  }

  public void addValidator(Validator validator) {
    validators.add(validator);
  }

  public void onChange(Widget sender) {
    validate();
  }

  public void onClick(Widget sender) {
    validate();
  }

  public void onFocus(Widget sender) {
    // Do nothing
  }

  public void onLostFocus(Widget sender) {
    validate();
  }

  public void setErrorHandler(ErrorHandler handler) {
    this.handler = handler;
  }

  public final void validate() {
    for (int i = 0; i < validators.size(); i++) {
      Validator validator = (Validator) validators.get(i);
      validate(validator, validatables);
    }
    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        Validation v = (Validation) children.get(i);
        v.validate();
      }
    }
  }

  private final void validate(Validator validator, Collection subjects) {
    Iterator iter = subjects.iterator();
    while (iter.hasNext()) {
      validate(validator, (Subject) iter.next());
    }
  }

  private final void validate(Validator validator, Subject subject) {
    if (subject.getValue() != null) {
      validator.checkValid(subject, handler);
    }
  }
}
