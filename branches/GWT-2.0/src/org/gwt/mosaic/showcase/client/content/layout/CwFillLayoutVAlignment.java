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

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwFillLayoutVAlignment extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwFillLayoutVAlignment(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "The FillLayout with a vertical alignment.";
  }

  @Override
  public String getName() {
    return "Vertical Alignment";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets, default is FillLayout
    final LayoutPanel layoutPanel = new LayoutPanel();
    layoutPanel.setAnimationEnabled(true);
    
    FillLayout layout = (FillLayout) layoutPanel.getLayout();
    layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

    final Image img = new Image("MeteoraGreece.JPG");
    layoutPanel.add(img);

    return layoutPanel;
  }

}
