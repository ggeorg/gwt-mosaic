package gwt.mosaic.mobile.client;

import gwt.mosaic.mobile.client.Scrollable.ScrollDir;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A container that has a touch scrolling capability.
 * <p>
 * {@code ScrollableView} is a subclass of {@link View}. Unlike the base {@code
 * View} class, {@code ScrollableView}'s DOM node always stays at the top of the
 * screen and its height is {@code 100%} of the screen. In this fixed DOM node,
 * container node scrolls. Browser's default scrolling behavior is disabled, and
 * the scrolling machinery is re-implemented with JavaScript. Thus the user does
 * not need to use the two-finger operation to scroll an inner DIV (container
 * node). The main purpose of this widget is to realize fixed-position header
 * and/or footer bars.
 */
public class ScrollableView extends View {

  private class ScrollPanel extends Container {
    public ScrollPanel() {
      super(Document.get().createDivElement());
      setStyleName("mblScrollableViewContainer");
      Style elemStyle = getElement().getStyle();
      elemStyle.setPosition(Position.ABSOLUTE);
    }
  }

  private ScrollDir scrollDir;
  private boolean flippable;

  private final ScrollPanel container;

  private final Scrollable scrollable;

  public ScrollableView() {
    addStyleName("mblScrollableView");
    Style elemStyle = getElement().getStyle();
    elemStyle.setOverflow(Overflow.HIDDEN);
    elemStyle.setTop(0, Unit.PX);
    elemStyle.setHeight(100, Unit.PCT);
    container = new ScrollPanel();
    add(container);

    setScrollDir(ScrollDir.VERTICAL);

    scrollable = new Scrollable(this, container);
  }

  public ComplexPanel getContainer() {
    return container;
  }

  public ScrollDir getScrollDir() {
    return scrollDir;
  }

  public void setScrollDir(ScrollDir scrollDir) {
    this.scrollDir = scrollDir;
  }

  public boolean isFlippable() {
    return flippable;
  }

  public void setFlippable(boolean flippable) {
    this.flippable = flippable;
  }

  @Override
  protected void add(Widget child, Element container) {
    if (child == this.container) {
      super.add(child, container);
    } else {
      getContainer().add(child);
    }
  }

  @Override
  protected void insert(Widget child, Element container, int beforeIndex, boolean domInsert) {
    if (child == this.container) {
      super.insert(child, container, beforeIndex, domInsert);
    } else {
      this.container.insert(child, beforeIndex);
    }
  }

  private void _checkFixedBar(Element elem) {
    elem.getStyle().getProperty("fixed");
  }

}
