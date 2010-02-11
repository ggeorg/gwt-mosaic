/*
 * Copyright (c) 2008-2009 GWT Mosaic Alejandro D. Garin.
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
package org.gwt.mosaic.contrib.client;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItemSeparator;

/**
 * A {@link org.gwt.mosaic.ui.client.PopupMenu} which allows to add disabled
 * menu items.
 * 
 * @author Alejandro D. Garin
 * 
 */
// FIXME: Bug: The class name org.gwt.mosaic.contrib.client.PopupMenu shadows
// the simple name of the superclass org.gwt.mosaic.ui.client.PopupMenu
public class PopupMenu extends org.gwt.mosaic.ui.client.PopupMenu {

  private static class MenuItemDisabled extends MenuItemSeparator {
    private MenuBar parentMenu;

    public MenuItemDisabled(String text, boolean asHTML) {
      if (asHTML) {
        DOM.setInnerHTML(getElement(), text);
      } else {
        DOM.setInnerText(getElement(), text);
      }
      DOM.setStyleAttribute(getElement(), "color", "#B5B5B5");
    }

    public MenuBar getParentMenu() {
      return parentMenu;
    }
  }

  /**
   * Adds a disabled item.
   * 
   * @param the text of the disabled item
   */
  public void addDisabledItem(String text) {
    addSeparator(new MenuItemDisabled(text, false));
  }

  /**
   * Adds a disabled item.
   * 
   * @param text the text of the disabled item
   * @param asHTML {@code true} if the text is HTML, {@code false} otherwise
   */
  public void addDisabled(String text, boolean asHTML) {
    addSeparator(new MenuItemDisabled(text, asHTML));
  }
}
