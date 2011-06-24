package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class Viewport extends Container {

  public Viewport() {
    super(RootPanel.getBodyElement());

    // Setting the panel's position style to 'relative' causes it to be treated
    // as a new positioning context for its children.
    DOM.setStyleAttribute(getElement(), "position", "relative");
    DOM.setStyleAttribute(getElement(), "overflow", "hidden");

    Document.get().getDocumentElement().addClassName("mobile");
    Document.get().getBody().getStyle().setHeight(100, Unit.PCT);
    addClass();
    onAttach();
  }

  /**
   * Adds a theme class name to <body>.
   * <p>
   * Finds the currently applied theme name, such as 'iphone' or 'android' from
   * link elements, and adds it as a class name for body element.
   */
  private void addClass() {
    NodeList<Element> elems = Document.get().getElementsByTagName("link");
    for (int i = 0, len = elems.getLength(); i < len; i++) {
      LinkElement linkElem = elems.getItem(i).cast();
      if (linkElem.getHref().endsWith("android.css")) {
        Document.get().getBody().addClassName("android");
        break;
      }
    }
  }

}
