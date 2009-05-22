/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class DecoratedTabLayoutPanel extends TabLayoutPanel {

  public DecoratedTabLayoutPanel() {
    this(TabBarPosition.TOP, false);
  }

  public DecoratedTabLayoutPanel(boolean decorateBody) {
    this(TabBarPosition.TOP, decorateBody);
  }

  public DecoratedTabLayoutPanel(TabBarPosition region) {
    this(region, false);
  }

  public DecoratedTabLayoutPanel(TabBarPosition region, boolean decorateBody) {
    super(region, true, decorateBody);
  }

}