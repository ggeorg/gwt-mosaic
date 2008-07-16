package org.mosaic.ui.client.layout;

import com.google.gwt.user.client.ui.DecoratorPanel;

public class FillLayoutData extends LayoutData {

  public FillLayoutData() {
    // Nothing to do here!
  }
  
  public FillLayoutData(boolean decorate) {
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }

}
