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

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Defines base methods for ResourceContext implementations.
 */
public abstract class AbstractResourceContext implements ResourceContext {
  private final TreeLogger logger;
  private final GeneratorContext context;
  private final JClassType resourceBundleType;
  private final String simpleSourceName;
  private final SourceWriter sw;

  protected AbstractResourceContext(TreeLogger logger,
      GeneratorContext context, JClassType resourceBundleType,
      String simpleSourceName, SourceWriter sw) {
    this.logger = logger;
    this.context = context;
    this.resourceBundleType = resourceBundleType;
    this.simpleSourceName = simpleSourceName;
    this.sw = sw;
  }

  public GeneratorContext getGeneratorContext() {
    return context;
  }

  public String getImplementationSimpleSourceName() {
    return simpleSourceName;
  }

  public TreeLogger getLogger() {
    return logger;
  }

  public JClassType getResourceBundleType() {
    return resourceBundleType;
  }

  public SourceWriter getSourceWriter() {
    return sw;
  }
}
