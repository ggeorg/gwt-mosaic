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

import com.google.gwt.core.ext.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Maps a named property to a Value.
 */
public class CssProperty extends CssNode {

  /**
   * Represents a sequence of no-arg method invocations.
   */
  public static class DotPathValue implements Value {
    private final String path;
    private final String suffix;

    public DotPathValue(String path, String suffix) {
      this.path = path;
      this.suffix = suffix;
    }

    public String getExpression() {
      return path.replace(".", "().") + "() + \"" + Generator.escape(suffix)
          + "\"";
    }

    public String getPath() {
      return path;
    }

    public String getSuffix() {
      return suffix;
    }
  }

  /**
   * Represents a literal Java expression.
   */
  public static class ExpressionValue implements Value {
    private final String expression;

    public ExpressionValue(String expression) {
      this.expression = expression;
    }

    public String getExpression() {
      return expression;
    }
  }

  /**
   * Represents a space-separated list of Values.
   */
  public static class ListValue implements Value {
    private final List<Value> values;

    public ListValue(List<Value> values) {
      this.values = new ArrayList<Value>(values);
    }

    public ListValue(Value... values) {
      this(Arrays.asList(values));
    }

    public String getExpression() {
      StringBuilder toReturn = new StringBuilder();
      for (Iterator<Value> i = values.iterator(); i.hasNext();) {
        toReturn.append(i.next().getExpression());
        if (i.hasNext()) {
          toReturn.append("+ \" \" +");
        }
      }
      return toReturn.toString();
    }

    public List<Value> getValues() {
      return values;
    }
  }

  /**
   * Represents one or more literal string values.
   */
  public static class StringValue implements Value {
    private final String value;

    public StringValue(String value) {
      this.value = value;
    }

    public String getExpression() {
      return "\"" + Generator.escape(value) + "\"";
    }

    public String getValue() {
      return value;
    }
  }

  /**
   * An abstract encapsulation of property values in GWT CSS.
   */
  public interface Value {
    /**
     * Generate a Java expression whose execution results in the value.
     */
    String getExpression();
  }

  private boolean important;

  private String name;
  private ListValue value;

  public CssProperty(String name, Value value, boolean important) {
    this.name = name;
    setValue(value);
    this.important = important;
  }

  public String getName() {
    return name;
  }

  public ListValue getValues() {
    return value;
  }

  public boolean isImportant() {
    return important;
  }

  public void setImportant(boolean important) {
    this.important = important;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(Value value) {
    if (value instanceof ListValue) {
      this.value = (ListValue) value;
    } else {
      this.value = new ListValue(value);
    }
  }

  public void traverse(CssVisitor visitor, Context context) {
    visitor.visit(this, context);
    visitor.endVisit(this, context);
  }
}
