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

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
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
    return "The InfoPanel widget is used for notifications and can be used to indicate non-critical events "
        + "while interrupting the user as little as possible.";
  }

  @Override
  public String getName() {
    return "Info Panel";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new ScrollLayoutPanel();
    layoutPanel.setPadding(0);

    final TextBox captionInput = new TextBox();
    captionInput.setText("Caption");

    final TextBox descriptionInput = new TextBox();
    descriptionInput.setText("Description");

    final CheckBox typeInput = new CheckBox("Humanized message");

    final FlexTable table = new FlexTable();
    table.getColumnFormatter().setWidth(0, "30%");
    table.getColumnFormatter().setWidth(1, "70%");
    table.setText(0, 0, "Caption:");
    table.setWidget(0, 1, captionInput);
    table.setText(1, 0, "Description:");
    table.setWidget(1, 1, descriptionInput);

    final Button btn = new Button("Show InfoPanel", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (typeInput.getValue()) {
          InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE,
              captionInput.getText(), descriptionInput.getText());
        } else {
          InfoPanel.show(captionInput.getText(), descriptionInput.getText());
        }
      }
    });

    final HorizontalPanel hpanel = new HorizontalPanel();
    hpanel.add(btn);
    hpanel.add(new HTML("&nbsp;"));
    hpanel.add(typeInput);

    layoutPanel.add(table, new BoxLayoutData(FillStyle.HORIZONTAL, true));
    layoutPanel.add(hpanel);

    return layoutPanel;
  }

}
