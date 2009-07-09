/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.HTMLLabel;
import org.gwt.mosaic.ui.client.TextLabel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-WindowPanel"})
public class CwFormDialog extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwFormDialog(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Form Dialog description.";
  }

  @Override
  public String getName() {
    return "Form Dialog";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    final WindowPanel windowPanel = new WindowPanel("Form Dialog", false);
    windowPanel.setAnimationEnabled(true);
    windowPanel.getHeader().add(Caption.IMAGES.window().createImage());

    final LayoutPanel windowContent = new LayoutPanel(new FormLayout("pref",
        "pref, 6dlu, pref"));
    windowPanel.setWidget(windowContent);

    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.add(newColumnSpan(), "Column Span");
    tabPanel.add(buildRowSpanPanel(), "Row Span");
    tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
      public void onSelection(SelectionEvent<Integer> event) {
        if (windowPanel.isShowing()) {
          windowPanel.pack();
          windowPanel.layout();
        }
      }
    });
    tabPanel.selectTab(0);
    
    windowPanel.setFooter(buildButtonBarPanel());

    layoutPanel.add(new Button("Click me!", new ClickHandler() {
      public void onClick(ClickEvent event) {
        windowContent.add(
            new HTMLLabel("<b>GWT Mosaic basic form dialog.</b>"),
            CellConstraints.xy(1, 1));
        windowContent.add(tabPanel, CellConstraints.xy(1, 3));
        windowPanel.showModal();
      }
    }));

    return layoutPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newColumnSpan() {
    FormLayout layout = new FormLayout("pref, 8px, 100px, 4px, 200px:grow",
        "pref, 6px, pref, 6px, pref, 6px, pref, 6px, pref");

    LayoutPanel panel = new LayoutPanel(layout);

    panel.add(new TextLabel("Name:"), CellConstraints.xy(1, 1));
    panel.add(new TextBox(), CellConstraints.xyw(3, 1, 3));

    panel.add(new TextLabel("Phone:"), CellConstraints.xy(1, 3));
    panel.add(new TextBox(), CellConstraints.xyw(3, 3, 3));

    panel.add(new TextLabel("ZIP, City:"), CellConstraints.xy(1, 5));
    panel.add(new TextBox(), CellConstraints.xy(3, 5));
    panel.add(new TextBox(), CellConstraints.xy(5, 5));

    panel.add(new TextLabel("Country:"), CellConstraints.xy(1, 7));
    panel.add(new TextBox(), CellConstraints.xyw(3, 7, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildRowSpanPanel() {
    FormLayout layout = new FormLayout("200px, 25px, 200px",
        "2*(pref, 2px, pref, 9px), pref, 2px, pref");

    LayoutPanel panel = new LayoutPanel(layout);

    panel.add(new TextLabel("Name:"), CellConstraints.xy(1, 1));
    panel.add(new TextBox(), CellConstraints.xy(1, 3));

    panel.add(new TextLabel("Phone:"), CellConstraints.xy(1, 5));
    panel.add(new TextBox(), CellConstraints.xy(1, 7));

    panel.add(new TextLabel("Fax:"), CellConstraints.xy(1, 9));
    panel.add(new TextBox(), CellConstraints.xy(1, 11));

    panel.add(new TextLabel("Notes:"), CellConstraints.xy(3, 1));
    panel.add(new TextArea(), CellConstraints.xywh(3, 3, 1, 9));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildButtonBarPanel() {
    LayoutPanel p = ButtonBarFactory.buildHelpOKCancelBar(new Button("Help"),
        new Button("OK"), new Button("Cancel"));
    p.setPadding(5);
    return p;
  }
}
