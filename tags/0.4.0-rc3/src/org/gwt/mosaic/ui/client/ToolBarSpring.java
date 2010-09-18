/*
 * Copyright 2008-2010 Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client;

/**
 * Flexible space between {@link ToolBar} items.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see ToolBar
 */
public class ToolBarSpring extends ToolBarSeparator {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ToolBarSpring";

  /**
   * Creates a {@code ToolBarSpring} instance.
   */
  public ToolBarSpring() {
    setStyleName(DEFAULT_STYLENAME);
  }
}