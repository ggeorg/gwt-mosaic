package org.gwt.mosaic.application.client;

import org.gwt.beansbinding.core.client.util.AbstractBean;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.user.client.ui.Widget;

/**
 * A {@code View} encapsulates a top-level application GUI component, like
 * {@link Viewport}.
 * 
 * @author ggeorg
 * 
 */
public abstract class View extends AbstractBean {

  private Application application;

  private Widget widget = null;
  private Widget toolBox = null;
  private Widget statusBar = null;
  private Widget sideBar = null;

  /**
   * Constructs an empty {@code View} object for the specified
   * {@link Application}.
   * 
   * @param application the {@link Application} responsible for showing/hiding
   *          this {@code View}
   * @see Application#show(View);
   * @see Application#hide(View);
   */
  public View(Application application) {
    assert (application != null);
    this.application = application;
  }

  /**
   * The {@link Application} that's responsible for showing.hiding this {@code
   * View}.
   * 
   * @return the {@link Application} that owns this {@code View}
   */
  public final Application getApplication() {
    return application;
  }

  /**
   * The {@link ApplicationContext} for the {@link Application} that's
   * responsible for showing/hiding this {@code View}. This method is just a
   * shorthand for {@code #getApplication()#getContext()}.
   * 
   * @return the {@link ApplicationContext} that owns this {@code View}
   * @see #getApplication()
   */
  public final ApplicationContext getContext() {
    return getApplication().getContext();
  }

  /**
   * The {@link LayoutPanel} for this {@code View}.
   * 
   * @return the {@code layoutPanel} for this {@code View}
   */
  public abstract LayoutPanel getLayoutPanel();

  /**
   * The main {@code Widget} for this {@code View}.
   * 
   * @return the {@code Widget} for this {@code View}
   */
  public Widget getWidget() {
    return widget;
  }

  private void replaceLayoutPanelChild(Widget oldChild, Widget newChild,
      LayoutData layoutData) {
    LayoutPanel layoutPanel = getLayoutPanel();
    if (oldChild != null) {
      layoutPanel.remove(oldChild);
    }
    if (newChild != null) {
      layoutPanel.add(newChild, layoutData);
    }
    layoutPanel.invalidate(newChild);
    layoutPanel.layout();
  }

  public void setWidget(Widget widget) {
    setWidget(widget, false);
  }

  public void setWidget(Widget widget, boolean decorate) {
    Widget oldValue = this.widget;
    this.widget = widget;
    replaceLayoutPanelChild(oldValue, this.widget, new BorderLayoutData(
        decorate));
    firePropertyChange("widget", oldValue, this.widget);
  }

  public Widget getToolBox() {
    return toolBox;
  }

  public void setToolBox(Widget toolBox) {
    assert (toolBox != null);
    Widget oldValue = this.toolBox;
    this.toolBox = toolBox;
    replaceLayoutPanelChild(oldValue, this.toolBox, new BorderLayoutData(
        Region.NORTH));
    firePropertyChange("toolBox", oldValue, this.toolBox);
  }

  public Widget getStatusBar() {
    return statusBar;
  }

  public void setStatusBar(Widget statusBar) {
    assert (statusBar != null);
    Widget oldValue = this.statusBar;
    this.statusBar = statusBar;
    replaceLayoutPanelChild(oldValue, this.statusBar, new BorderLayoutData(
        Region.SOUTH));
    firePropertyChange("statusBar", oldValue, this.statusBar);
  }

  public Widget getSideBar() {
    return sideBar;
  }

  public void setSideBar(Widget sideBar) {
    setSideBar(sideBar, "20em", false);
  }

  public void setSideBar(Widget sideBar, boolean decorate) {
    setSideBar(sideBar, "20em", decorate);
  }

  public void setSideBar(Widget sideBar, String preferredSize, boolean decorate) {
    assert (sideBar != null);
    Widget oldValue = this.sideBar;
    this.sideBar = sideBar;
    replaceLayoutPanelChild(oldValue, this.sideBar, new BorderLayoutData(
        Region.WEST, preferredSize, "5em", preferredSize, decorate));
    firePropertyChange("sideBar", oldValue, this.sideBar);
  }
}
