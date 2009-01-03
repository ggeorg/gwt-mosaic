/*
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
package org.gwt.mosaic.actions.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * Common action images.
 * 
 * @author ggeorgopoulos.georgios(at)gmail.com
 */
public interface ActionImages extends ImageBundle {

  @Resource(value = "exit_action.png")
  AbstractImagePrototype exit_action();

  @Resource(value = "award_star_bronze_1.png")
  AbstractImagePrototype award_star_bronze_1();

  @Resource(value = "bell.png")
  AbstractImagePrototype bell();

  @Resource(value = "bomb.png")
  AbstractImagePrototype bomb();

  @Resource(value = "menuitem_checkbox.png")
  AbstractImagePrototype menuitem_checkbox();

  @Resource(value = "menuitem_radiobutton.png")
  AbstractImagePrototype menuitem_radiobutton();

  @Resource(value = "noimage.png")
  AbstractImagePrototype noimage();

  @Resource(value = "copy_action.png")
  AbstractImagePrototype copy_action();

  @Resource(value = "cut_action.png")
  AbstractImagePrototype cut_action();

  @Resource(value = "paste_action.png")
  AbstractImagePrototype paste_action();

  @Resource(value = "close_action.png")
  AbstractImagePrototype close_action();

  @Resource(value = "open_action.png")
  AbstractImagePrototype open_action();
}
