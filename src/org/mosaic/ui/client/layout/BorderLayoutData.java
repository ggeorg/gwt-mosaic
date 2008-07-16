package org.mosaic.ui.client.layout;

import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.DecoratorPanel;

public class BorderLayoutData extends LayoutData {

  BorderLayoutRegion region;

  double preferredSize = -1.0;
  
  int minSize, maxSize;
  
  boolean resizable, collapse;

  public BorderLayoutData() {
    this(BorderLayoutRegion.CENTER, false);
  }
  
  public BorderLayoutData(boolean decorate) {
    this(BorderLayoutRegion.CENTER, decorate);
  }

  public BorderLayoutData(BorderLayoutRegion region) {
    this(region, false);
  }
  
  public BorderLayoutData(BorderLayoutRegion region, boolean decorate) {
    this.region = region;
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }

  public BorderLayoutData(BorderLayoutRegion region, double preferredSize) {
    this(region, preferredSize, false);
  }
  
  public BorderLayoutData(BorderLayoutRegion region, double preferredSize, boolean decorate) {
    this.region = region;
    this.preferredSize = preferredSize;
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }
  
  public BorderLayoutData(BorderLayoutRegion region, double preferredSize, int minSize, int maxSize) {
    this(region, preferredSize, minSize, maxSize, false);
  }
  
  public BorderLayoutData(BorderLayoutRegion region, double preferredSize, int minSize, int maxSize, boolean decorate) {
    this.region = region;
    this.preferredSize = preferredSize;
    this.minSize = minSize;
    this.maxSize = maxSize;
    this.resizable = true;
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }
}
