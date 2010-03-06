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
package com.google.gwt.user.client.ui;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class AbstractDecoratedPopupPanel extends DecoratedPopupPanel {

  /**
   * The type of animation to use when opening the popup.
   * 
   * <ul>
   * <li>CENTER - Expand from the center of the popup</li>
   * <li>ONE_WAY_CORNER - Expand from the top left corner, do not animate hiding
   * </li>
   * <li>ROLL_DOWN - ?</li>
   * </ul>
   */
  public static enum AnimationType {
    CENTER(PopupPanel.AnimationType.CENTER),

    ONE_WAY_CORNER(PopupPanel.AnimationType.ONE_WAY_CORNER),

    ROLL_DOWN(PopupPanel.AnimationType.ROLL_DOWN);

    private PopupPanel.AnimationType type;

    AnimationType(PopupPanel.AnimationType type) {
      this.type = type;
    }

    public PopupPanel.AnimationType getType() {
      return type;
    }
  }

  /**
   * Creates an empty decorated popup panel using the specified style names.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   * @param modal <code>true</code> if keyboard or mouse events that do not
   *          target the PopupPanel or its children should be ignored
   * @param prefix the prefix applied to child style names
   * @param type the type of animation to use
   */
  protected AbstractDecoratedPopupPanel(boolean autoHide, boolean modal,
      String prefix, AnimationType type) {
    super(autoHide, modal, prefix);
    setAnimationType(type.getType());
  }
  
  @Override
  protected void onDetach() {
    // See GWT Issue 4720
    super.onDetach();

    if (getGlassElement() != null) {
      getGlassElement().removeFromParent();
    }
  }

}
