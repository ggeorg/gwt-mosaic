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

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;

/**
 * A base class providing common methods for ResourceGenerator implementations.
 * 
 * @see ResourceGeneratorUtil
 */
public abstract class AbstractResourceGenerator implements ResourceGenerator {
  public abstract String createAssignment(TreeLogger logger, JMethod method)
      throws UnableToCompleteException;

  /**
   * A no-op implementation.
   */
  public void createFields(TreeLogger logger, FieldAccumulator fields)
      throws UnableToCompleteException {
  }

  /**
   * A no-op implementation.
   */
  public void finish(TreeLogger logger) throws UnableToCompleteException {
  }

  public abstract void init(TreeLogger logger, ResourceContext context)
      throws UnableToCompleteException;

  /**
   * A no-op implementation.
   */
  public void prepare(TreeLogger logger, JMethod method)
      throws UnableToCompleteException {
  }
}
