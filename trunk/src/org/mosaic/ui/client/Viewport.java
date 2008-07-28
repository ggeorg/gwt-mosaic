package org.mosaic.ui.client;

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.layout.FillLayoutData;
import org.mosaic.ui.client.layout.HasLayout;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Viewport extends Composite implements WindowResizeListener,
    WindowCloseListener {

  private static Viewport viewport;

  public final static Viewport get() {
    if (viewport == null) {
      viewport = new Viewport();
    }
    return viewport;
  }

  private Timer delayedResize = new Timer() {

    @Override
    public void run() {
      final Widget widget = Viewport.get().getWidget();

      final int width = Window.getClientWidth();
      final int height = Window.getClientHeight();

      Viewport.this.setBounds(widget, 0, 0, width, height);

      if (widget instanceof HasLayout) {
        final HasLayout layoutWidget = (HasLayout) widget;
        layoutWidget.layout();
      }
    }
  };
  
  /**
   * 
   * @param layoutPanel
   * @param widget
   * @param x
   * @param y
   * @param width
   * @param height
   */
  protected void setBounds(final Widget widget,
      final int x, final int y, final int width, final int height) {
    RootPanel.get().setWidgetPosition(this, x, y);
    setSize(widget, width, height);
  }

  /**
   * 
   * @param widget
   * @param width
   * @param height
   */
  protected void setSize(final Widget widget, int width, int height) {
    Element elem = widget.getElement();

    int[] margins = DOM.getMarginSizes(widget.getElement());
    
    if (width != -1) {
      width -= (margins[1] + margins[3]);
      DOM.setContentAreaWidth(elem, Math.max(0, width));
    }
    if (height != -1) {
      height -= (margins[0] + margins[2]);
      DOM.setContentAreaHeight(elem, Math.max(0, height));
    }
  }

  /**
   * Default constructor.
   */
  protected Viewport() {
    // Nothing to do here!
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#initWidget(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public void initWidget(Widget widget) {
    initWidget(widget, false);
  }

  /**
   * Sets the widget to be wrapped by the composite. The wrapped widget must be
   * set before calling any {@link Widget} methods on this object, or adding it
   * to a panel. This method may only be called once for a given composite.
   * 
   * @param widget the widget to be wrapped
   * @param decorate if the widget should be decorated
   */
  public void initWidget(Widget widget, boolean decorate) {
    LayoutPanel panel = new LayoutPanel();
    panel.add(widget, new FillLayoutData(decorate));
    
    super.initWidget(panel);

    Window.addWindowCloseListener(viewport);
    Window.addWindowResizeListener(viewport);

    Window.enableScrolling(false);

    RootPanel.get().add(viewport);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Widget#onLoad()
   */
  @Override
  protected void onLoad() {
    super.onLoad();

    delayedResize.schedule(1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.WindowCloseListener#onWindowClosed()
   */
  public void onWindowClosed() {
    Window.removeWindowResizeListener(this);
    Window.removeWindowCloseListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.WindowCloseListener#onWindowClosing()
   */
  public String onWindowClosing() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.WindowResizeListener#onWindowResized(int,
   *      int)
   */
  public void onWindowResized(int width, int height) {
    delayedResize.schedule(333);
  }

}
