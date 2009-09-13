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
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwFillLayoutAnimation extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwFillLayoutAnimation(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "FillLayout transition animations.";
  }

  @Override
  public String getName() {
    return "FillLayout";
  }

  /**
   * Horizonal alignment values.
   */
  @ShowcaseData
  private HorizontalAlignmentConstant[] halign = {
      null, HasHorizontalAlignment.ALIGN_LEFT,
      HasHorizontalAlignment.ALIGN_CENTER, HasHorizontalAlignment.ALIGN_RIGHT};

  /**
   * Vertical alignment values.
   */
  @ShowcaseData
  private VerticalAlignmentConstant[] valign = {
      null, HasVerticalAlignment.ALIGN_TOP, HasVerticalAlignment.ALIGN_MIDDLE,
      HasVerticalAlignment.ALIGN_BOTTOM};

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets, default is FillLayout
    final LayoutPanel layoutPanel = new LayoutPanel();
    layoutPanel.setAnimationEnabled(true);

    DOM.setStyleAttribute(layoutPanel.getElement(), "border",
        "5px dashed #4d4d4d");

    final Button btn = new Button("Click me!");

    final FillLayoutData layoutData = new FillLayoutData(false);
    //layoutData.setPreferredSize("50%", "50%");
    layoutData.xsetPreferredHeight("50%"); // TODO remove x
    layoutPanel.add(btn, layoutData);

    btn.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        FillLayoutData layoutData = (FillLayoutData) layoutPanel.getLayoutData(btn);

        layoutData.setHorizontalAlignment(halign[(int) Math.round(Math.random() * 3)]);
        layoutData.setVerticalAlignment(valign[(int) Math.round(Math.random() * 3)]);

        layoutPanel.invalidate();
        layoutPanel.layout();
      }
    });

    return layoutPanel;
  }

}
