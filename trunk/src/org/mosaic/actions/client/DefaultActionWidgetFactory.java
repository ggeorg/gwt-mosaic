package org.mosaic.actions.client;

import org.mosaic.ui.client.ImageButton;
import org.mosaic.ui.client.ToolButton;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.UIObject;

public class DefaultActionWidgetFactory implements ActionWidgetFactory {

  @SuppressWarnings("unchecked")
  public UIObject createWidget(Action action, Class uiClass) {
    if (uiClass == Button.class) {

    } else if (uiClass == PushButton.class) {
      
    } else if (uiClass == ToggleButton.class) {

    } else if (uiClass == ToolButton.class) {

    } else if (uiClass == ImageButton.class) {
      
    } else if (uiClass == MenuItem.class) {
      
    }
    return null;
  }

  public void propertyChange(UIObject uiObject, ActionPropertyChangeEvent event) {
    if (uiObject instanceof Button) {

    } else if (uiObject instanceof PushButton) {
      
    } else if (uiObject instanceof ToggleButton) {

    } else if (uiObject instanceof ToolButton) {
      
    } else if (uiObject instanceof ImageButton) {

    } else if (uiObject instanceof MenuItem) {
      
    }
  }

}
