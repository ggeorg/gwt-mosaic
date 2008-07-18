package org.mosaic.ui.client.layout;

import org.mosaic.core.client.DOM;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.LayoutManagerHelper;
import com.google.gwt.user.client.ui.Widget;

public abstract class BaseLayout extends LayoutManagerHelper implements LayoutManager {

  /**
   * Gets the panel-defined layout data associated with this widget.
   * 
   * @param widget the widget
   * @return the widget's layout data
   */
  protected final static Object getLayoutData(Widget widget) {
    return LayoutManagerHelper.getLayoutData(widget);
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
  protected final static void setLayoutData(Widget widget, Object layoutData) {
    LayoutManagerHelper.setLayoutData(widget, layoutData);
  }

  private int margin = 0;

  protected int getFlowHeight(Widget child) {
    final int[] b = DOM.getBorderSizes(child.getElement());
    int flowHeight;
    if (child instanceof LayoutPanel) {
      final LayoutPanel lp = (LayoutPanel) child;
      final int[] preferredSize = lp.getLayout().getPreferredSize(lp);
      flowHeight = preferredSize[1] + b[0] + b[2];
    } else {
      flowHeight = child.getOffsetHeight() + b[0] + b[2];
    }
    return flowHeight;
  }

  protected int getFlowWidth(Widget child) {
    final int[] b = DOM.getBorderSizes(child.getElement());
    int flowWidth;
    if (child instanceof LayoutPanel) {
      final LayoutPanel lp = (LayoutPanel) child;
      final int[] preferredSize = lp.getLayout().getPreferredSize(lp);
      flowWidth = preferredSize[0] + b[1] + b[3];
    } else {
      flowWidth = child.getOffsetWidth() + b[1] + b[3];
    }
    return flowWidth;
  }

  public int getMargin() {
    return margin;
  }

  protected void setBounds(final LayoutPanel layoutPanel, final DecoratorPanel decPanel,
      final int x, final int y, int width, int height) {
    setXY(layoutPanel, decPanel, x, y);
    setSize(decPanel.getWidget(), width, height);
  }

  protected void setBounds(final LayoutPanel layoutPanel, final Widget widget,
      final int x, final int y, int width, int height) {
    int[] margins = DOM.getMarginSizes(widget.getElement());
    if (width != -1) {
      width -= (margins[1] + margins[3]);
    }
    if (height != -1) {
      height -= (margins[0] + margins[2]);
    }

    setXY(layoutPanel, widget, x, y);
    setSize(widget, width, height);
  }

  public void setMargin(int margin) {
    this.margin = margin;
  }

  private void setSize(final Widget widget, int width, int height) {
    Element elem = widget.getElement();

    if (width != -1) {
      DOM.setWidth(elem, Math.max(0, width));
    }
    if (height != -1) {
      DOM.setHeight(elem, Math.max(0, height));
    }
  }

  private void setXY(final LayoutPanel layoutPanel, final Widget widget, final int x,
      final int y) {
    layoutPanel.setWidgetPosition(widget, x, y);
  }

}
