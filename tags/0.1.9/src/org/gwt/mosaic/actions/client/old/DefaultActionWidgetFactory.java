/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopolos.
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

import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.ToolButton;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.UIObject;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @deprecated
 */
public class DefaultActionWidgetFactory implements ActionWidgetFactory {

  @SuppressWarnings("unchecked")
  public UIObject createWidget(Action action, Class uiClass) {
    if (uiClass == Button.class) {
      return createButton(action);
    } else if (uiClass == PushButton.class) {
      return createPushButton(action);
    } else if (uiClass == ToggleButton.class) {
      return createToggleButton(action);
    } else if (uiClass == ToolButton.class) {
      return createToolButton(action);
    } else if (uiClass == ImageButton.class) {
      return createImageButton(action);
    } else if (uiClass == MenuItem.class) {
      return createMenuItem(action);
    }
    return null;
  }

  private Button createButton(Action action) {
    Button button = new Button();
    if (action.getText() != null) {
      button.setText(action.getText());
    }
    if (action.getTitle() != null) {
      button.getTitle();
    }
    button.addClickListener(action);
    return button;
  }

  private PushButton createPushButton(Action action) {
    PushButton pushButton = new PushButton();
    if (action.getText() != null) {
      pushButton.setText(action.getText());
    }
    if (action.getTitle() != null) {
      pushButton.getTitle();
    }
    pushButton.addClickListener(action);
    return pushButton;
  }

  private ToggleButton createToggleButton(Action action) {
    ToggleButton toggleButton = new ToggleButton();
    if (action.getText() != null) {
      toggleButton.setText(action.getText());
    }
    if (action.getTitle() != null) {
      toggleButton.getTitle();
    }
    toggleButton.addClickListener(action);
    return toggleButton;
  }

  private ToolButton createToolButton(Action action) {
    ToolButton toolButton = new ToolButton();
    if (action.getText() != null) {
      toolButton.setText(action.getText());
    }
    if (action.getTitle() != null) {
      toolButton.getTitle();
    }
    toolButton.addClickListener(action);
    return toolButton;
  }

  private ImageButton createImageButton(Action action) {
    ImageButton imageButton = new ImageButton(action.getImage());
    imageButton.addClickListener(action);
    return imageButton;
  }

  private MenuItem createMenuItem(Action action) {
    String html;
    if (action.getImage() != null) {
      html = action.getImage().getHTML() + "&nbsp;" + action.getText();
    } else {
      html = action.getText();
    }
    MenuItem menuItem = new MenuItem(html, true, action);
    if (action.getTitle() != null) {
      menuItem.getTitle();
    }
    return menuItem;
  }

  public void propertyChange(UIObject uiObject, ActionPropertyChangeEvent event) {
    if (uiObject instanceof Button) {
      propertyChange(event, (Button) uiObject);
    } else if (uiObject instanceof PushButton) {
      propertyChange(event, (PushButton) uiObject);
    } else if (uiObject instanceof ToggleButton) {
      propertyChange(event, (ToggleButton) uiObject);
    } else if (uiObject instanceof ToolButton) {
      propertyChange(event, (ToolButton) uiObject);
    } else if (uiObject instanceof ImageButton) {
      propertyChange(event, (ImageButton) uiObject);
    } else if (uiObject instanceof MenuItem) {
      propertyChange(event, (MenuItem) uiObject);
    }
  }

  private void propertyChange(ActionPropertyChangeEvent event, Button button) {
    switch (event.getProperty()) {
      case ActionProperty.TEXT:
        button.setText((String) event.getNewValue());
        break;
    }
  }

  private void propertyChange(ActionPropertyChangeEvent event,
      PushButton pushButton) {
    switch (event.getProperty()) {
      case ActionProperty.TEXT:
        pushButton.setText((String) event.getNewValue());
        break;
    }
  }

  private void propertyChange(ActionPropertyChangeEvent event,
      ToggleButton toggleButton) {
    switch (event.getProperty()) {
      case ActionProperty.TEXT:
        toggleButton.setText((String) event.getNewValue());
        break;
    }
  }

  private void propertyChange(ActionPropertyChangeEvent event,
      ToolButton toolButton) {
    switch (event.getProperty()) {
      case ActionProperty.TEXT:
        toolButton.setText((String) event.getNewValue());
        break;
    }
  }

  private void propertyChange(ActionPropertyChangeEvent event,
      ImageButton imageButton) {
    switch (event.getProperty()) {
      case ActionProperty.TEXT:
        break;
    }
  }

  private void propertyChange(ActionPropertyChangeEvent event, MenuItem menuItem) {
    switch (event.getProperty()) {
      case ActionProperty.TEXT:
        menuItem.setText((String) event.getNewValue());
        break;
    }
  }

}
