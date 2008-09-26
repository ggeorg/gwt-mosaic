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
package org.gwt.mosaic.ui.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface CaptionImages extends ImageBundle {
  
  @Resource("tool-button-minus.png")
  AbstractImagePrototype toolMinus();
  
  @Resource("tool-button-plus.png")
  AbstractImagePrototype toolPlus();

  @Resource("window-button-close.png")
  AbstractImagePrototype windowClose();

  @Resource("window-button-maximize.png")
  AbstractImagePrototype windowMaximize();

  @Resource("window-button-maximized.png")
  AbstractImagePrototype windowMaximized();

  @Resource("window-button-minimize.png")
  AbstractImagePrototype windowMinimize();

}
