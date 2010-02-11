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
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.DimensionPolicy;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.MarginPolicy;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwAbsoluteLayout extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwAbsoluteLayout(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "AbsoluteLayout description";
  }

  @Override
  public String getName() {
    return "AbsoluteLayout";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new AbsoluteLayout("32em",
        "24em"));
    layoutPanel.setPadding(0);

    layoutPanel.add(new Button("Icon"), new AbsoluteLayoutData("1em", "1em",
        "8em", "8em", MarginPolicy.RIGHT_BOTTOM, true));
    layoutPanel.add(new Button("This is a message to the user."),
        new AbsoluteLayoutData("10em", "1em", "21em", "19em",
            MarginPolicy.NONE, DimensionPolicy.BOTH));

    layoutPanel.add(new Button("OK"), new AbsoluteLayoutData("14em", "21em",
        "8em", "2em", MarginPolicy.LEFT_TOP));
    layoutPanel.add(new Button("Cancel"), new AbsoluteLayoutData("23em",
        "21em", "8em", "2em", MarginPolicy.LEFT_TOP));

    layoutPanel.add(new Button("Help"), new AbsoluteLayoutData("1em", "21em",
        "8em", "2em", MarginPolicy.RIGHT_TOP));

    return layoutPanel;
  }

}
