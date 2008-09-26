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

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractDecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class PopupMenu extends AbstractDecoratedPopupPanel implements HasAnimation {

  private final MenuBar menu;

  public PopupMenu() {
    super(true, false, "menuPopup");
    menu = new MenuBar(true);
    menu.setAutoOpen(true);
    add(menu);
    setAnimationEnabled(true);
    sinkEvents(Event.ONCLICK);
    setStyleName("gwt-MenuBarPopup");
  }

  @Override
  public void setAnimationEnabled(boolean b) {
    super.setAnimationEnabled(true);
    menu.setAnimationEnabled(true);
  }

  /**
   * Adds a menu item to the menu.
   * 
   * @param item the item to be added
   * @return the {@link MenuItem} object
   */
  public MenuItem addItem(MenuItem item) {
    return menu.addItem(item);
  }

  /**
   * Adds a menu item to the bar, that will fire the given command when it is
   * selected.
   * 
   * @param text the item's text
   * @param asHTML <code>true</code> to treat the specified text as html
   * @param cmd the command to be fired
   * @return the {@link MenuItem} object created
   */
  public MenuItem addItem(String text, boolean asHTML, Command cmd) {
    return menu.addItem(text, asHTML, cmd);
  }

  /**
   * Adds a menu item to the bar, that will open the specified menu when it is
   * selected.
   * 
   * @param text the item's text
   * @param asHTML <code>true</code> to treat the specified text as html
   * @param popup the menu to be cascaded from it
   * @return the {@link MenuItem} object created
   */
  public MenuItem addItem(String text, boolean asHTML, MenuBar popup) {
    return menu.addItem(text, asHTML, popup);
  }

  /**
   * Adds a menu item to the bar, that will fire the given command when it is
   * selected.
   * 
   * @param text the item's text
   * @param cmd the command to be fired
   * @return the {@link MenuItem} object created
   */
  public MenuItem addItem(String text, Command cmd) {
    return menu.addItem(text, cmd);
  }

  /**
   * Adds a menu item to the bar, that will open the specified menu when it is
   * selected.
   * 
   * @param text the item's text
   * @param popup the menu to be cascaded from it
   * @return the {@link MenuItem} object created
   */
  public MenuItem addItem(String text, MenuBar popup) {
    return menu.addItem(text, popup);
  }

  /**
   * Adds a thin line to the {@link MenuBar} to separate sections of
   * {@link MenuItem}s.
   * 
   * @return the {@link MenuItemSeparator} object created
   */
  public MenuItemSeparator addSeparator() {
    return menu.addSeparator();
  }

  /**
   * Adds a thin line to the {@link MenuBar} to separate sections of
   * {@link MenuItem}s.
   * 
   * @param separator the {@link MenuItemSeparator} to be added
   * @return the {@link MenuItemSeparator} object
   */
  public MenuItemSeparator addSeparator(MenuItemSeparator separator) {
    return menu.addSeparator(separator);
  }

  /**
   * Removes all menu items from this menu bar.
   */
  public void clearItems() {
    menu.clearItems();
  }

  /**
   * Gets whether this menu bar's child menus will open when the mouse is moved
   * over it.
   * 
   * @return <code>true</code> if child menus will auto-open
   */
  public boolean getAutoOpen() {
    return menu.getAutoOpen();
  }

  /**
   * Removes the specified menu item from the bar.
   * 
   * @param item the item to be removed
   */
  public void removeItem(MenuItem item) {
    menu.removeItem(item);
  }

  /**
   * Removes the specified {@link MenuItemSeparator} from the bar.
   * 
   * @param separator the separator to be removed
   */
  public void removeSeparator(MenuItemSeparator separator) {
    menu.removeSeparator(separator);
  }

  /**
   * Sets whether this menu bar's child menus will open when the mouse is moved
   * over it.
   * 
   * @param autoOpen <code>true</code> to cause child menus to auto-open
   */
  public void setAutoOpen(boolean autoOpen) {
    menu.setAutoOpen(autoOpen);
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    switch (DOM.eventGetType(event)) {
      case Event.ONCLICK:
        this.hide();
        break;
    }
  }

}
