/*
 * Copyright 2008 Georgios J. Georgopoulos.
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

import com.google.gwt.user.client.ui.DecoratorPanel;

public abstract class LayoutData {

  int minSize = 0, maxSize = -1;

  double preferredSize = -1.0;

  DecoratorPanel decoratorPanel;

  DecoratorPanel getDecoratorPanel() {
    return decoratorPanel;
  }

  public int getMaxSize() {
    return maxSize;
  }

  public int getMinSize() {
    return minSize;
  }

  public double getPreferredSize() {
    return preferredSize;
  }

  public boolean hasDecoratorPanel() {
    return decoratorPanel != null;
  }

  protected void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  protected void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  protected void setPreferredSize(double preferredSize) {
    this.preferredSize = preferredSize;
  }

}
