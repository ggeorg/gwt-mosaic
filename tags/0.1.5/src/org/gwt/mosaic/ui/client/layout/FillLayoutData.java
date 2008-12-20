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
package org.gwt.mosaic.ui.client.layout;

/**
 * Layout data object for {@link FillLayout}.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see FillLayout
 */
public class FillLayoutData extends LayoutData {

  /**
   * Creates a new instance of {@code FillLayoutData}. The associated widget
   * should be undecorated.
   */
  public FillLayoutData() {
    super(false);
  }

  /**
   * Creates a new instance of {@code FillLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}.
   * 
   * @param decorate specifies whether the associated widget will be decorated
   *          or not.
   */
  public FillLayoutData(final boolean decorate) {
    super(decorate);
  }

}
