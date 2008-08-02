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
package com.google.gwt.libideas.resources.css.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class CssProperty extends CssNode {
  private boolean important;

  /**
   * This is a Java expression which evaluates to the value of the property.
   */
  private String expression;
  private String name;
  private final List<String> values = new ArrayList<String>();

  public CssProperty(String name, List<String> values, boolean important) {
    this.name = name;
    this.values.addAll(values);
    this.important = important;
  }

  public CssProperty(String name, String expression, boolean important) {
    this.name = name;
    this.expression = expression;
    this.important = important;
  }

  public String getExpression() {
    return expression;
  }

  public String getName() {
    return name;
  }

  public List<String> getValues() {
    return values;
  }

  public boolean isImportant() {
    return important;
  }

  public void setExpression(String expression) {
    this.expression = expression;
    values.clear();
  }

  public void setImportant(boolean important) {
    this.important = important;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void traverse(CssVisitor visitor, Context context) {
    visitor.visit(this, context);
    visitor.endVisit(this, context);
  }
}
