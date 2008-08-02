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
 * This is a refinement that will use data urls for browsers that support them.
 * Only files whose size are smaller than MAX_INLINE_SIZE will be inlined.
 * Larger files will use the standard CacheBundle behavior.
 * 
 * @see "RFC 2397"
 */
public final class InlineResourceBundleGenerator extends
    AbstractResourceBundleGenerator {
  protected ResourceContext createResourceContext(TreeLogger logger,
      GeneratorContext context, JClassType resourceBundleType,
      String simpleSourceName, SourceWriter sw) {
    return new InlineResourceContext(logger.branch(TreeLogger.DEBUG,
        "Using inline resources", null), context, resourceBundleType,
        simpleSourceName, sw);
  }

  protected String generateSimpleSourceName(TreeLogger logger,
      PropertyOracle oracle, String sourceType)
      throws UnableToCompleteException {
    try {
      return sourceType.replaceAll("\\.", "_") + "_"
          + oracle.getPropertyValue(logger, "user.agent") + "_inlineBundle";
    } catch (BadPropertyValueException e) {
      logger.log(TreeLogger.ERROR, "No user.agent property defined", e);
      throw new UnableToCompleteException();
    }
  }
}
