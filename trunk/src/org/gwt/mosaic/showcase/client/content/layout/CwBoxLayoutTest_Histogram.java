/*
 * Copyright 2008 Google Inc.
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
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwBoxLayoutTest_Histogram extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBoxLayoutTest_Histogram(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "A Vertical BoxLayout test";
  }

  @Override
  public String getName() {
    return "Histogram";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());
    ((BoxLayout) layoutPanel.getLayout()).setAlignment(Alignment.END);

    layoutPanel.setWidgetSpacing(1);
    DOM.setStyleAttribute(layoutPanel.getElement(), "border", "1px dotted #000");

    final int nBins = 33;
    final double binWidth = 1.0 / (double) nBins;
    final double mean = (nBins - 1) / 2;

    for (int i = 0; i < nBins; i++) {
      final double value = Math.exp(-Math.pow((i - mean), 2) / (mean * 4));
      final Widget w = new SimplePanel();
      w.setTitle(Math.round(value * 100) + "%");
      DOM.setStyleAttribute(w.getElement(), "border", "1px solid #000");
      DOM.setStyleAttribute(w.getElement(), "background", "#f4f");
      DOM.setStyleAttribute(w.getElement(), "color", "#fff");
      layoutPanel.add(w, new BoxLayoutData(binWidth, value));
    }

    return layoutPanel;
  }

}
