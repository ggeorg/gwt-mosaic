package org.mosaic.ui.client.layout;

import com.google.gwt.user.client.ui.DecoratorPanel;

public abstract class LayoutData {

  DecoratorPanel decoratorPanel;

  public boolean hasDecoratorPanel() {
    return decoratorPanel != null;
  }
  
  DecoratorPanel getDecoratorPanel() {
    return decoratorPanel;
  }

}
