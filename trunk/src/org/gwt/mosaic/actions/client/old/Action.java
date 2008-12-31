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
package org.gwt.mosaic.actions.client.old;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * An action represents the non-UI side of a command which can be triggered by
 * the end user. Actions are typically associated with buttons, menu items, and
 * items in tool bars. When the end user triggers the command via its control,
 * the action's <code>execute</code> method is invoked to do the real work.
 * 
 * @deprecated
 */
public class Action implements Command, HasText, ClickListener {

  /**
   * Unique identifier for this action
   */
  private final String id;

  /**
   * The text for this action.
   */
  private String text;

  /**
   * The title (tool-tip) for this action.
   */
  private String title;

  /**
   * The image for this action.
   */
  private AbstractImagePrototype image;

  /**
   * The description for this action.
   */
  private String description;

  /**
   * The action's states properties.
   */
  private boolean checked, enabled = true, visible = true;

  /**
   * The action's widget factory.
   */
  private ActionWidgetFactory widgetFactory;

  /**
   * The registered listeners for this action.
   */
  private List<ActionListener> listeners;

  /**
   * The widgets that are referenced by this action.
   */
  private List<UIObject> uiObjects;

  /**
   * 
   * @param id
   */
  public Action(String id) {
    this(id, null, null, null, null, new DefaultActionWidgetFactory());
  }

  public Action(String id, String text) {
    this(id, text, null, null, null, new DefaultActionWidgetFactory());
  }

  public Action(String id, String text, String title) {
    this(id, text, title, null, null, new DefaultActionWidgetFactory());
  }

  public Action(String id, String text, String title,
      AbstractImagePrototype image) {
    this(id, text, title, image, null, new DefaultActionWidgetFactory());
  }

  public Action(String id, String text, String title,
      AbstractImagePrototype image, String description) {
    this(id, text, title, image, description, new DefaultActionWidgetFactory());
  }

  public Action(String id, String text, String title,
      AbstractImagePrototype image, String description,
      ActionWidgetFactory widgetFactory) {
    this.id = id;
    this.text = text;
    this.title = title;
    this.image = image;
    this.description = description;
    this.widgetFactory = widgetFactory;
  }

  public void execute() {
    if (enabled && listeners != null) {
      for (ActionListener listener : listeners) {
        try {
          listener.handleAction(this);
        } catch (Exception e) {
          // ignore
        }
      }
    }
  }

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    final String oldValue = this.text;
    this.text = text;
    fireActionPropertyChange(ActionProperty.TEXT, oldValue, text);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    final String oldValue = this.title;
    this.title = title;
    fireActionPropertyChange(ActionProperty.TITLE, oldValue, title);
  }

  public AbstractImagePrototype getImage() {
    return image;
  }

  public void setImage(AbstractImagePrototype image) {
    final AbstractImagePrototype oldValue = this.image;
    this.image = image;
    fireActionPropertyChange(ActionProperty.IMAGE, oldValue, image);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    final String oldValue = this.description;
    this.description = description;
    fireActionPropertyChange(ActionProperty.DESCRIPTION, oldValue, description);
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    final Boolean oldValue = this.checked;
    this.checked = checked;
    fireActionPropertyChange(ActionProperty.CHECKED, oldValue, new Boolean(
        checked));
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    final Boolean oldValue = this.enabled;
    this.enabled = enabled;
    fireActionPropertyChange(ActionProperty.ENABLED, oldValue, new Boolean(
        enabled));
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    final Boolean oldValue = this.visible;
    this.visible = visible;
    fireActionPropertyChange(ActionProperty.VISIBLE, oldValue, new Boolean(
        visible));
  }

  /**
   * Adds an action listener that will be invoked to do the real work.
   * 
   * @param listener the action listener
   */
  public void addActionListener(ActionListener listener) {
    if (listeners == null) {
      listeners = new ArrayList<ActionListener>();
    }
    if (listeners.indexOf(listener) == -1) {
      listeners.add(listener);
    }
  }

  /**
   * Remove an action listener from the listeners list.
   * 
   * @param listener the action listener
   * @return <code>true</code> if the listener list contained the specified
   *         element
   */
  public boolean removeActionListener(ActionListener listener) {
    if (listeners != null) {
      return listeners.remove(listener);
    }
    return false;
  }

  /**
   * Remove all action listeners from the listeners list.
   */
  public void removeAllActionListeners() {
    if (listeners != null) {
      listeners.clear();
    }
  }

  /**
   * Checks if the listeners list contains the listener.
   * 
   * @param listener the listener to check for
   * @return <code>true</code> if the listeners list contains the listener,
   *         <code>false</code> otherwise
   */
  public boolean hasActionListener(ActionListener listener) {
    if (listeners != null) {
      return listeners.indexOf(listener) != -1;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public UIObject createWidget(Class widget) {
    final UIObject uiObject = widgetFactory.createWidget(this, widget);
    if (uiObject != null) {
      if (uiObjects == null) {
        uiObjects = new ArrayList<UIObject>();
      }
      uiObjects.add(uiObject);
    }
    return uiObject;
  }

  public boolean releaseWidget(Widget w) {
    if (uiObjects != null) {
      return uiObjects.remove(w);
    }
    return false;
  }

  public void releaseAllWidgets() {
    if (uiObjects != null) {
      uiObjects.clear();
    }
  }

  protected void fireActionPropertyChange(int type, Object oldValue,
      Object newValue) {
    if (uiObjects == null || uiObjects.isEmpty()) {
      return;
    }
    final ActionPropertyChangeEvent event = new ActionPropertyChangeEvent(this,
        type, oldValue, newValue);
    for (UIObject uiObj : uiObjects) {
      try {
        widgetFactory.propertyChange(uiObj, event);
      } catch (Exception e) {
        // ignore
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  public void onClick(Widget sender) {
    execute();
  }

}
