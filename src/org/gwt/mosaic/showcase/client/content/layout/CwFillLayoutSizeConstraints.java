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
package org.gwt.mosaic.showcase.client.content.layout;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.FillLayout;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwFillLayoutSizeConstraints extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwFillLayoutSizeConstraints(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "The FillLayout stretches the first available child to the full available size but still respects limits configured by min/max values.";
  }

  @Override
  public String getName() {
    return "Size Constraints";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a scroll layout panel to align the widgets, default is BoxLayout
    final ScrollLayoutPanel layoutPanel = new ScrollLayoutPanel(new FillLayout());
    layoutPanel.setAnimationEnabled(true);

    final Image img = new Image("MeteoraGreece.JPG");
    final Element elem = img.getElement();
    
    img.setSize("100%", "100%");
    
    DOM.setStyleAttribute(elem, "minWidth", "512px");
    DOM.setStyleAttribute(elem, "minHeight", "384px");
    
    DOM.setStyleAttribute(elem, "maxWidth", "640px");
    DOM.setStyleAttribute(elem, "maxHeight", "480px");
    
    layoutPanel.add(img);

    return layoutPanel;
  }

}
