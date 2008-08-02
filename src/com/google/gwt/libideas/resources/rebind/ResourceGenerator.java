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
 * Encapsulates per-type resource generation logic. The methods on
 * ResourceGenerator will be called in the following order:
 * <ol>
 * <li>{@link #init(TreeLogger, ResourceContext)}</li>
 * <li>{@link #prepare(TreeLogger, JMethod)} once for each method</li>
 * <li>{@link #createFields(TreeLogger, FieldAccumulator)}</li>
 * <li>{@link #createAssignment(TreeLogger, JMethod)} once for each method</li>
 * <li>{@link #finish(TreeLogger)}</li>
 * </ol>
 * 
 * Direct access to the contents of the generated bundle implementation is
 * intentionally limited to prevent unrelated ResourceGenerators from
 * potentially creating namespace conflicts.
 */
public interface ResourceGenerator {

  /**
   * Produce the right-hand-side of an assignment expression to provide the
   * singleton instance object for a particular resource.
   */
  String createAssignment(TreeLogger logger, JMethod method)
      throws UnableToCompleteException;

  /**
   * The ResourceGenerator can create bundle-level fields.
   */
  void createFields(TreeLogger logger, FieldAccumulator fields)
      throws UnableToCompleteException;

  /**
   * Called at the end of the resource generation phase and can be used to
   * perform cleanup. The default implementation is a no-op.
   */
  void finish(TreeLogger logger) throws UnableToCompleteException;

  /**
   * Initialize the ResourceGenerator with the generation context. The logger
   * instance should not be retained; use the per-method loggers instead.
   */
  void init(TreeLogger logger, ResourceContext context)
      throws UnableToCompleteException;

  /**
   * Called once for each method the ResourceGenerator is expected to handle.
   * The default implementation is a no-op.
   */
  void prepare(TreeLogger logger, JMethod method)
      throws UnableToCompleteException;
}