package org.gwt.mosaic.ui.client.util;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class ButtonHelper {

  public static enum ButtonLabelType {
    TEXT_ON_TOP, TEXT_ON_RIGHT, TEXT_ON_BOTTOM, TEXT_ON_LEFT, TEXT_ONLY, NO_TEXT
  }
  
  public static String createButtonLabel(AbstractImagePrototype image, String text,
      ButtonLabelType type) {
    final HTML html = new HTML(text, false);
    final Image img = image.createImage();
    if (type == ButtonLabelType.TEXT_ONLY) {
      return text;
    } else if (type == ButtonLabelType.NO_TEXT) {
      return image.getHTML();
    } else if (type == ButtonLabelType.TEXT_ON_LEFT
        || type == ButtonLabelType.TEXT_ON_RIGHT) {
      HorizontalPanel hpanel = new HorizontalPanel();
      if (type == ButtonLabelType.TEXT_ON_LEFT) {
        hpanel.add(html);
        hpanel.add(new HTML("&nbsp;"));
        hpanel.add(img);
      } else {
        hpanel.add(img);
        hpanel.add(new HTML("&nbsp;"));
        hpanel.add(html);
      }
      hpanel.setCellVerticalAlignment(html, HasVerticalAlignment.ALIGN_MIDDLE);
      hpanel.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
      return hpanel.getElement().getString();
    } else {
      VerticalPanel vpanel = new VerticalPanel();
      if (type == ButtonLabelType.TEXT_ON_TOP) {
        vpanel.add(html);
        vpanel.add(img);
      } else {
        vpanel.add(img);
        vpanel.add(html);
      }
      vpanel.setCellHorizontalAlignment(html,
          HasHorizontalAlignment.ALIGN_CENTER);
      vpanel.setCellHorizontalAlignment(img,
          HasHorizontalAlignment.ALIGN_CENTER);
      return vpanel.getElement().getString();
    }
  }
  
}
