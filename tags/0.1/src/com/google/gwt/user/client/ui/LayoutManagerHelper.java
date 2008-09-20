package com.google.gwt.user.client.ui;

public abstract class LayoutManagerHelper {

  /**
   * Gets the panel-defined layout data associated with this widget.
   * 
   * @param widget the widget
   * @return the widget's layout data
   */
  protected static Object getLayoutData(Widget widget) {
    return widget.getLayoutData();
  }

  /**
   * Sets the panel-defined layout data associated with this widget. Only the
   * panel that currently contains a widget should ever set this value. It
   * serves as a place to store layout bookkeeping data associated with a
   * widget.
   * 
   * @param widget the widget
   * @param layoutData the widget's layout data
   */
  protected static void setLayoutData(Widget widget, Object layoutData) {
    widget.setLayoutData(layoutData);
  }

}
