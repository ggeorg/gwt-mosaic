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
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwFillLayoutStretchedImage extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwFillLayoutStretchedImage(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "The FillLayout stretches the first available child to the full available size.";
  }

  @Override
  public String getName() {
    return "Stretched Image";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets, default is FillLayout
    final LayoutPanel layoutPanel = new LayoutPanel();

    final Image img = new Image("MeteoraGreece.JPG");
    layoutPanel.add(img);

    return layoutPanel;
  }

}
