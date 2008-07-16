package org.mosaic.ui.client;

public class DecoratedTabLayoutPanel extends TabLayoutPanel {

  public DecoratedTabLayoutPanel() {
    this(TabBarPosition.TOP, false);
  }
  
  public DecoratedTabLayoutPanel(boolean decorateBody) {
    this(TabBarPosition.TOP, decorateBody);
  }
  
  public DecoratedTabLayoutPanel(TabBarPosition region) {
    this(region, false);
  }
  
  public DecoratedTabLayoutPanel(TabBarPosition region, boolean decorateBody) {
    super(region, true, decorateBody);
  }

}
