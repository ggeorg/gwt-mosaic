package org.gwt.mosaic.core.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Tests standard DOM operations in the {@link DOM} class.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DOMTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "org.gwt.mosaic.core.Core";
  }

  public void testElementBoxSize() {
    final int[] margin = {1, 2, 3, 4};
    final int[] border = {11, 12, 13, 14};
    final int[] padding = {21, 22, 23, 24};

    final int width = 100;
    final int height = 100;

    final Element elem = DOM.createDiv();
    DOM.appendChild(RootPanel.getBodyElement(), elem);

    DOM.setStyleAttribute(elem, "position", "absolute");
    DOM.setStyleAttribute(elem, "top", "0px");
    DOM.setStyleAttribute(elem, "left", "0px");

    DOM.setStyleAttribute(elem, "marginTop", margin[0] + "px");
    DOM.setStyleAttribute(elem, "marginRight", margin[1] + "px");
    DOM.setStyleAttribute(elem, "marginBottom", margin[2] + "px");
    DOM.setStyleAttribute(elem, "marginLeft", margin[3] + "px");

    DOM.setStyleAttribute(elem, "border", "1px solid #000");
    DOM.setStyleAttribute(elem, "borderTopWidth", border[0] + "px");
    DOM.setStyleAttribute(elem, "borderRightWidth", border[1] + "px");
    DOM.setStyleAttribute(elem, "borderBottomWidth", border[2] + "px");
    DOM.setStyleAttribute(elem, "borderLeftWidth", border[3] + "px");

    DOM.setStyleAttribute(elem, "paddingTop", padding[0] + "px");
    DOM.setStyleAttribute(elem, "paddingRight", padding[1] + "px");
    DOM.setStyleAttribute(elem, "paddingBottom", padding[2] + "px");
    DOM.setStyleAttribute(elem, "paddingLeft", padding[3] + "px");

    DOM.setStyleAttribute(elem, "width", width + "px");
    DOM.setStyleAttribute(elem, "height", height + "px");

    // delayTestFinish(1000);

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        assertEquals(new Dimension(width + margin[1] + margin[3] + border[1]
            + border[3] + padding[1] + padding[3], height + margin[0]
            + margin[2] + border[0] + border[2] + padding[0] + padding[2]),
            DOM.getBoxSize(elem));
        assertEquals(new Dimension(width + (margin[1] + margin[3]), height
            + (margin[0] + margin[2])), new Dimension(elem.getOffsetWidth(),
            elem.getOffsetHeight()));
      }
    });
  }
  
  public void testButtonElementBoxSize() {
    final int[] margin = {1, 2, 3, 4};
    final int[] border = {11, 12, 13, 14};
    final int[] padding = {21, 22, 23, 24};

    final int width = 100;
    final int height = 100;

    final Element elem = DOM.createButton();
    DOM.appendChild(RootPanel.getBodyElement(), elem);

    DOM.setStyleAttribute(elem, "position", "absolute");
    DOM.setStyleAttribute(elem, "top", "0px");
    DOM.setStyleAttribute(elem, "left", "0px");

    DOM.setStyleAttribute(elem, "marginTop", margin[0] + "px");
    DOM.setStyleAttribute(elem, "marginRight", margin[1] + "px");
    DOM.setStyleAttribute(elem, "marginBottom", margin[2] + "px");
    DOM.setStyleAttribute(elem, "marginLeft", margin[3] + "px");

    DOM.setStyleAttribute(elem, "border", "1px solid #000");
    DOM.setStyleAttribute(elem, "borderTopWidth", border[0] + "px");
    DOM.setStyleAttribute(elem, "borderRightWidth", border[1] + "px");
    DOM.setStyleAttribute(elem, "borderBottomWidth", border[2] + "px");
    DOM.setStyleAttribute(elem, "borderLeftWidth", border[3] + "px");

    DOM.setStyleAttribute(elem, "paddingTop", padding[0] + "px");
    DOM.setStyleAttribute(elem, "paddingRight", padding[1] + "px");
    DOM.setStyleAttribute(elem, "paddingBottom", padding[2] + "px");
    DOM.setStyleAttribute(elem, "paddingLeft", padding[3] + "px");

    DOM.setStyleAttribute(elem, "width", width + "px");
    DOM.setStyleAttribute(elem, "height", height + "px");

    // delayTestFinish(1000);

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        assertEquals(new Dimension(width + margin[1] + margin[3] + border[1]
            + border[3] + padding[1] + padding[3], height + margin[0]
            + margin[2] + border[0] + border[2] + padding[0] + padding[2]),
            DOM.getBoxSize(elem));
        assertEquals(new Dimension(width + (margin[1] + margin[3]), height
            + (margin[0] + margin[2])), new Dimension(elem.getOffsetWidth(),
            elem.getOffsetHeight()));
      }
    });
  }
}
