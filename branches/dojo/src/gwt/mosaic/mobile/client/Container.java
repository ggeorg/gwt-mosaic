package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.HasTouchStartHandlers;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.ComplexPanelHelper;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class Container extends ComplexPanel implements HasTouchStartHandlers {

  /**
   * Creates an empty container.
   */
  protected Container(Element divElement) {
    setElement(divElement);
  }

  /**
   * Adds a new child widget to the panel.
   * 
   * @param w the widget to be added
   */
  @Override
  public void add(Widget w) {
    add(w, getElement());
  }

  @Override
  public void clear() {
    try {
      ComplexPanelHelper.doLogicalClear(this);
    } finally {
      // Remove all existing child nodes.
      Node child = getElement().getFirstChild();
      while (child != null) {
        getElement().removeChild(child);
        child = getElement().getFirstChild();
      }
    }
  }

  public void insert(IsWidget w, int beforeIndex) {
    insert(asWidgetOrNull(w), beforeIndex);
  }

  /**
   * Inserts a widget before the specified index.
   * 
   * @param w the widget to be inserted
   * @param beforeIndex the index before which it will be inserted
   * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of
   *           range
   */
  public void insert(Widget w, int beforeIndex) {
    insert(w, getElement(), beforeIndex, true);
  }
  
  @Override
  public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
    return addDomHandler(handler, TouchStartEvent.getType());
  }

}
