package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.ComplexPanelHelper;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class RoundRect extends ComplexPanel {

  private boolean shadow = false;

  public RoundRect() {
    setElement(Document.get().createDivElement());
    setStyleName("mblRoundRect");
  }

  public boolean isShadow() {
    return shadow;
  }

  public void setShadow(boolean shadow) {
    if ((this.shadow = shadow)) {
      addStyleName("mblShadow");
    } else {
      removeStyleName("mblShadow");
    }
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

}
