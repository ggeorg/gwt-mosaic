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
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwBoxLayoutAnimation extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBoxLayoutAnimation(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "BoxLayout transition animations.";
  }

  @Override
  public String getName() {
    return "BoxLayout";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets, default is FillLayout
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    final BoxLayout boxLayout = new BoxLayout();
    final LayoutPanel layoutPanel2 = new LayoutPanel(boxLayout);
    layoutPanel2.setAnimationEnabled(true);

    DOM.setStyleAttribute(layoutPanel2.getElement(), "border",
        "5px dashed #4d4d4d");

    layoutPanel2.add(new Button("1"), new BoxLayoutData(FillStyle.BOTH, true));
    layoutPanel2.add(new Button("2"), new BoxLayoutData(FillStyle.BOTH));
    layoutPanel2.add(new Button("3"), new BoxLayoutData(FillStyle.BOTH));

    layoutPanel.add(layoutPanel2, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new Button("Toggle Orientation", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (boxLayout.getOrientation() == Orientation.HORIZONTAL) {
          boxLayout.setOrientation(Orientation.VERTICAL);
        } else {
          boxLayout.setOrientation(Orientation.HORIZONTAL);
        }
        layoutPanel2.invalidate();
        layoutPanel2.layout();
      }
    }));

    return layoutPanel;
  }

}
