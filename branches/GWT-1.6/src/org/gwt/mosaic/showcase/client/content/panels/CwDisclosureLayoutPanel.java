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
package org.gwt.mosaic.showcase.client.content.panels;

import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.layout.AbstractLayoutPage;
import org.gwt.mosaic.ui.client.DisclosureLayoutPanel;
import org.gwt.mosaic.ui.client.TextLabel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle( {".mosaic-DisclosureLayoutPanel"})
public class CwDisclosureLayoutPanel extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDisclosureLayoutPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "A Disclosure Panel will show or hide its contents when "
        + "the user clicks on the header text. "
        + "The contents can be simple text, or any Widget, "
        + "such as an image or advanced options in a form.";
  }

  @Override
  public String getName() {
    return "DisclosureLayoutPanel";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    
    DisclosureLayoutPanel panel = new DisclosureLayoutPanel("Contact Address");
    panel.add(createForm2());

    layoutPanel.add(createForm1(), new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(panel, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(new Button("Save Contact..."));
    
    return layoutPanel;
  }

  /**
   * Form part I
   */
  @ShowcaseSource
  private LayoutPanel createForm1() {
    final LayoutPanel layoutPpanel = new LayoutPanel(new FormLayout(
        "[150px,pref], 8px, 100px, 4px, 200px", "pref, 3dlu, pref"));

    layoutPpanel.add(new TextLabel("First Name:"), CellConstraints.xy(1, 1));
    layoutPpanel.add(new TextBox(), CellConstraints.xyw(3, 1, 3));
    
    layoutPpanel.add(new TextLabel("Last Name:"), CellConstraints.xy(1, 3));
    layoutPpanel.add(new TextBox(), CellConstraints.xyw(3, 3, 3));

    return layoutPpanel;
  }

  /**
   * Form part II
   */
  @ShowcaseSource
  private LayoutPanel createForm2() {
    final LayoutPanel layoutPpanel = new LayoutPanel(new FormLayout(
        "[133px,pref], 8px, 100px, 4px, 200px",
        "pref, 3dlu, pref, 3dlu, pref"));

    layoutPpanel.add(new TextLabel("Address:"), CellConstraints.xy(1, 1));
    layoutPpanel.add(new TextBox(), CellConstraints.xyw(3, 1, 3));

    layoutPpanel.add(new TextLabel("ZIP, City:"), CellConstraints.xy(1, 3));
    layoutPpanel.add(new TextBox(), CellConstraints.xy(3, 3));
    layoutPpanel.add(new TextBox(), CellConstraints.xy(5, 3));

    layoutPpanel.add(new TextLabel("Country:"), CellConstraints.xy(1, 5));
    layoutPpanel.add(new TextBox(), CellConstraints.xyw(3, 5, 3));

    return layoutPpanel;
  }

}
