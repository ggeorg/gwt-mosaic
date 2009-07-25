/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic.ui.client.util;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public final class ButtonHelper {

  public static enum ButtonLabelType {
    TEXT_ON_TOP, TEXT_ON_RIGHT, TEXT_ON_BOTTOM, TEXT_ON_LEFT, TEXT_ONLY, NO_TEXT
  }
  
  public static String createButtonLabel(AbstractImagePrototype image, String text,
      ButtonLabelType type) {
    return createButtonLabel(image, text, type, null);
  }
  
  public static String createButtonLabel(AbstractImagePrototype image, String text,
      ButtonLabelType type, String cssName) {
    final HTML html = new HTML(text, false);
    final Image img = image.createImage();
    if (cssName != null) {
      html.addStyleDependentName(cssName);
      img.addStyleDependentName(cssName);
    }
    if (type == ButtonLabelType.TEXT_ONLY) {
      return text;
    } else if (type == ButtonLabelType.NO_TEXT) {
      return image.getHTML();
    } else if (type == ButtonLabelType.TEXT_ON_LEFT
        || type == ButtonLabelType.TEXT_ON_RIGHT) {
      HorizontalPanel hpanel = new HorizontalPanel();
      if (cssName != null) {
        hpanel.addStyleName(cssName);
      }
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
