/*
 * Copyright 2008 Georgios J. Georgopoulos
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

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.gwt.mosaic.ui.client.infopanel.TrayInfoPanelNotifier;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle( {".mosaic-InfoPanel"})
public class CwInfoPanel extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwInfoPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "InfoPanel description";
  }

  @Override
  public String getName() {
    return "InfoPanel";
  }
  
  
  private TrayInfoPanelNotifier eventNotifier = new TrayInfoPanelNotifier();
  
  private int cpt;

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
 // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    final TextBox caption1 = new TextBox();
    caption1.setText("Caption");

    final TextBox description1 = new TextBox();
    description1.setText("Description");

    final CheckBox type1 = new CheckBox("Humanized message");

    FlexTable layout1 = new FlexTable();
    layout1.getColumnFormatter().setWidth(0, "30%");
    layout1.getColumnFormatter().setWidth(1, "70%");
    layout1.setText(0, 0, "Caption:");
    layout1.setWidget(0, 1, caption1);
    layout1.setText(1, 0, "Description:");
    layout1.setWidget(1, 1, description1);

    Button btn1 = new Button("Show InfoPanel");
    btn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (type1.isChecked()) {
            eventNotifier.show(InfoPanelType.HUMANIZED_MESSAGE, caption1.getText(),
              description1.getText());
        } else {
            eventNotifier.show(InfoPanelType.TRAY_NOTIFICATION,caption1.getText(), description1.getText());
        }
      }
    });

    HorizontalPanel hpanel1 = new HorizontalPanel();
    hpanel1.add(btn1);
    hpanel1.add(new HTML("&nbsp;"));
    hpanel1.add(type1);

    layoutPanel.add(layout1, new BoxLayoutData(FillStyle.HORIZONTAL, true));
    layoutPanel.add(hpanel1);

    // ---

    layoutPanel.add(new HTML("&nbsp;"));

    // ---

    final TextBox name = new TextBox();
    name.setText("Maria");

    final TextBox message = new TextBox();
    message.setText("I love you");

    final String caption2 = "Formated Text";
    final String description2 = "Hello {0}! {1}.";

    final CheckBox type2 = new CheckBox("Humanized message");

    FlexTable layout2 = new FlexTable();
    layout2.getColumnFormatter().setWidth(0, "30%");
    layout2.getColumnFormatter().setWidth(1, "70%");
    layout2.setText(0, 0, "Caption:");
    layout2.setHTML(0, 1, "<em>" + caption2 + "</em>");
    layout2.setText(1, 0, "Description:");
    layout2.setHTML(1, 1, "<em>" + description2 + "</em>");
    layout2.setHTML(2, 0, "1<sup>st</sup> parameter:");
    layout2.setWidget(2, 1, name);
    layout2.setHTML(3, 0, "2<sup>nd</sup> parameter:");
    layout2.setWidget(3, 1, message);

    Button btn2 = new Button("Show InfoPanel");
    btn2.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (type2.isChecked()) {
            eventNotifier.show(InfoPanelType.HUMANIZED_MESSAGE, caption2,
              description2);
        } else {
            eventNotifier.show(InfoPanelType.TRAY_NOTIFICATION, caption2, description2);
        }
      }
    });

    HorizontalPanel hpanel2 = new HorizontalPanel();
    hpanel2.add(btn2);
    hpanel2.add(new HTML("&nbsp;"));
    hpanel2.add(type2);

    layoutPanel.add(layout2, new BoxLayoutData(FillStyle.HORIZONTAL, true));
    layoutPanel.add(hpanel2);

    return layoutPanel;
  }

}
