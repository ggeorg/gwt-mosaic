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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwBorderLayoutTest_Frames extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBorderLayoutTest_Frames(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "BorderLayout is using an invisible GlassPanel so that dragging a SplitBar widget over an IFRAME is possible.";
  }

  @Override
  public String getName() {
    return "IFrames";
  }

  /**
   * The content widget's layout panel.
   */
  @ShowcaseData
  final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    layoutPanel.setPadding(0);

    // north panel

    layoutPanel.add(new Frame("http://www.google.com"), new BorderLayoutData(
        Region.NORTH, 100, 10, 250));

    // south panel

    layoutPanel.add(new Frame("http://www.google.com"), new BorderLayoutData(
        Region.SOUTH, 100, 10, 250));

    // west panel

    layoutPanel.add(new Frame("http://www.google.com"), new BorderLayoutData(
        Region.WEST, 100, 10, 250));

    // east panel

    layoutPanel.add(new Frame("http://www.google.com"), new BorderLayoutData(
        Region.EAST, 100, 10, 250));

    // center panel

    layoutPanel.add(new Frame("http://www.google.com"));

    return layoutPanel;
  }
}
