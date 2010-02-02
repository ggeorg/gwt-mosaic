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
package org.gwt.mosaic.showcase.client.content.popups;

import com.google.gwt.core.client.GWT;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.DecoratedLayoutPopupPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.SheetLayoutPanel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import org.gwt.mosaic.ui.client.layout.AbsoluteLayout;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.DimensionPolicy;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.MarginPolicy;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {
    ".mosaic-Caption", ".mosaic-TitledLayoutPanel", ".mosaic-WindowPanel",
    ".dragdrop-positioner", ".dragdrop-draggable", ".dragdrop-handle",
    ".dragdrop-movable-panel"})
public class CwSheetPanel extends ContentWidget {

  public interface Resources extends SheetLayoutPanel.Resources {

  };
    
  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwSheetPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "SheetPanel description";
  }

  @Override
  public String getName() {
    return "Sheet Panel";
  }

  @ShowcaseSource
  protected Widget createSheetContent(final SheetLayoutPanel sheetPanel) {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new AbsoluteLayout("32em",
        "24em"));
    layoutPanel.setPadding(0);

    layoutPanel.add(new Button("Icon"), new AbsoluteLayoutData("1em", "1em",
        "8em", "8em", MarginPolicy.RIGHT_BOTTOM, true));
    layoutPanel.add(new Button("This is a message to the user."),
        new AbsoluteLayoutData("10em", "1em", "21em", "19em",
            MarginPolicy.NONE, DimensionPolicy.BOTH));

    final ClickHandler clickHandler = new ClickHandler() {
	public void onClick(ClickEvent clickEvent) {
	  sheetPanel.hide();
	}
      };

    layoutPanel.add(new Button("OK", clickHandler), new AbsoluteLayoutData("14em", "21em",
        "8em", "2em", MarginPolicy.LEFT_TOP));
    layoutPanel.add(new Button("Cancel", clickHandler), new AbsoluteLayoutData("23em",
        "21em", "8em", "2em", MarginPolicy.LEFT_TOP));

    layoutPanel.add(new Button("Help"), new AbsoluteLayoutData("1em", "21em",
        "8em", "2em", MarginPolicy.RIGHT_TOP));

    return layoutPanel;
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {

    final Resources resources = GWT.create(Resources.class);
    StyleInjector.inject(resources.sheetPanelCss().getText());

    final SheetLayoutPanel sheetPanel = new SheetLayoutPanel(resources);
    sheetPanel.setWidth("500px");
    sheetPanel.setHeight("400px");
    sheetPanel.setWidget(createSheetContent(sheetPanel));

    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    layoutPanel.add(new Button("Show sheet...", new ClickHandler() {
      public void onClick(ClickEvent event) {
	sheetPanel.show();
      }
    }));

    return layoutPanel;
  }
  
}