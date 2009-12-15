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
package org.gwt.mosaic.showcase.client.content.tables;

import org.gwt.mosaic.showcase.client.content.tables.shared.Student;

import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.property.FooterProperty;

/**
 * An {@link AbstractColumnDefinition} applied to {@link Student} row values.
 * 
 * @param <ColType> the data type of the column
 */
public abstract class StudentColumnDefinition<ColType> extends
    AbstractColumnDefinition<Student, ColType> {
  /**
   * Dynamic {@link FooterProperty} used with students.
   */
  static class StudentFooterProperty extends FooterProperty {
    public StudentFooterProperty() {
      setFooterCount(1);
    }

    @Override
    public Object getFooter(int row, int column) {
      if (row == 0) {
        return "Col " + column;
      }
      return null;
    }
  }

  /**
   * The general grouping of the column definition.
   */
  public static enum Group {
    GENERAL("General Info"), SCHOOL("School Info"), LOGIN("Login Info");

    private String name;

    private Group(String name) {
      this.name = name;
    }

    /**
     * @return the name of the group
     */
    public String getName() {
      return name;
    }
  }

  /**
   * Construct a new {@link StudentColumnDefinition}.
   * 
   * @param name the name of the column
   * @param group the column group
   */
  public StudentColumnDefinition(String name, Group group) {
    setHeader(0, name);
    if (group == null) {
      setHeader(1, name);
    } else {
      setHeader(1, group.getName());
    }
    setHeader(2, "User Information");

    // Setup the footer property
    setColumnProperty(FooterProperty.TYPE, new StudentFooterProperty());
  }
}