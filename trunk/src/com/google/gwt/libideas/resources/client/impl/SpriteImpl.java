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
package com.google.gwt.libideas.resources.client.impl;

import com.google.gwt.dom.client.Element;

/**
 * Implements the {@literal @sprite} meta-class.
 */
public class SpriteImpl {
  /**
   * We need a second element to actually display the background image.
   */
  public static class SpriteImplIE6 extends SpriteImpl {
    @Override
    public void apply(Element e, String className) {
      e.setClassName(className);
      e.setInnerHTML("<div></div>");
    }
  }

  public void apply(Element e, String className) {
    e.setClassName(className);
    e.setInnerHTML("");
  }
}
