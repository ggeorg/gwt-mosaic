package org.mosaic.ui.client.layout;

import com.google.gwt.user.client.ui.DecoratorPanel;

public abstract class LayoutData {

  int minSize = 0, maxSize = -1;

  double preferredSize = -1.0;

  DecoratorPanel decoratorPanel;

  DecoratorPanel getDecoratorPanel() {
    return decoratorPanel;
  }

  public int getMaxSize() {
    return maxSize;
  }

  public int getMinSize() {
    return minSize;
  }

  public double getPreferredSize() {
    return preferredSize;
  }

  public boolean hasDecoratorPanel() {
    return decoratorPanel != null;
  }

  protected void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  protected void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  protected void setPreferredSize(double preferredSize) {
    this.preferredSize = preferredSize;
  }

}
