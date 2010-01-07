/*
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
package org.gwt.mosaic.ui.client.layout;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class FlowLayoutData extends LayoutData {

  public FlowLayoutData() {
    this(false);
  }

  public FlowLayoutData(boolean decorate) {
    super(decorate);
  }

  public FlowLayoutData(String preferredWidth, String preferredHeight) {
    this(preferredWidth, preferredHeight, false);
  }

  public FlowLayoutData(String preferredWidth, String preferredHeight,
      boolean decorate) {
    this(decorate);
    setPreferredSize(preferredWidth, preferredHeight);
  }
}
