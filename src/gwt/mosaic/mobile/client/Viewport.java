package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootPanel;

public class Viewport extends Container implements RequiresResize, ProvidesResize {

  private final Timer resizeTimer = new Timer() {
    @Override
    public void run() {
      Document.get().getBody().getStyle().setHeight(Document.get().getClientHeight(), Unit.PX);
      Viewport.this.onResize();
    }
  };

  public Viewport() {
    super(RootPanel.getBodyElement());

    // Setting the panel's position style to 'relative' causes it to be treated
    // as a new positioning context for its children.
    DOM.setStyleAttribute(getElement(), "position", "relative");
    DOM.setStyleAttribute(getElement(), "overflow", "hidden");

    Document.get().getDocumentElement().addClassName("mobile");
    addClass();

    onAttach();

    Window.addResizeHandler(new ResizeHandler() {
      public void onResize(ResizeEvent event) {
        resizeTimer.schedule(33);
      }
    });
    
    resizeTimer.schedule(1);
    
    Document.get().getBody().getStyle().setVisibility(Visibility.VISIBLE);
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
      } else if (linkElem.getHref().endsWith("ipad.css")) {
        Document.get().getBody().addClassName("ipad");
        break;
      } else if (linkElem.getHref().endsWith("iphone.css")) {
        Document.get().getBody().addClassName("iphone");
        break;
      }
    }
  }

}
