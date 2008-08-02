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
package com.google.gwt.libideas.resources.rebind;

import com.google.gwt.core.ext.typeinfo.JType;

/**
 * This interface insulates fields declared in a bundle by different
 * ResourceGenerators from one another.
 */
public interface FieldAccumulator {
  /**
   * Adds a field to the bundle. Equivalent to
   * <code>addField(type, name, null, true, false)</code>.
   * 
   * @param type the declared type of the field
   * @param name a Java identifier to be used as the name of the field
   * @return the identifier that should be used to access the field
   */
  String addField(JType type, String name);

  /**
   * Adds a field to the bundle.
   * 
   * @param type the declared type of the field
   * @param name a Java identifier to be used as the name of the field
   * @param initializer a Java expression that will be used as the field's
   *          initializer, or <code>null</code> if no initialization
   *          expression is desired
   * @param isStatic if <code>true</code> the field will be declared to be
   *          static
   * @param isFinal if <code>true</code> the fields will be declared as final
   * @return the identifier that should be used to access the field
   */
  String addField(JType type, String name, String initializer,
      boolean isStatic, boolean isFinal);
}
