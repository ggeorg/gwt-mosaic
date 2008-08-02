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
package com.google.gwt.libideas.resources.rebind.context;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.libideas.resources.rebind.AbstractResourceBundleGenerator;
import com.google.gwt.libideas.resources.rebind.ResourceContext;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Copies selected files into module output with strong names and generates the
 * ResourceBundle mappings.
 */
public final class StaticResourceBundleGenerator extends
    AbstractResourceBundleGenerator {

  protected ResourceContext createResourceContext(TreeLogger logger,
      GeneratorContext context, JClassType resourceBundleType,
      String simpleSourceName, SourceWriter sw) {
    return new StaticResourceContext(logger.branch(TreeLogger.DEBUG,
        "Using static resources", null), context, resourceBundleType,
        simpleSourceName, sw);
  }

  /**
   * Given a user-defined type name, determine the type name for the generated
   * class.
   */
  protected String generateSimpleSourceName(TreeLogger logger,
      PropertyOracle oracle, String sourceType)
      throws UnableToCompleteException {
    try {
      return sourceType.replaceAll("\\.", "_") + "_"
          + oracle.getPropertyValue(logger, "user.agent") + "_externalBundle";
    } catch (BadPropertyValueException e) {
      logger.log(TreeLogger.ERROR, "No user.agent property defined", e);
      throw new UnableToCompleteException();
    }
  }
}
