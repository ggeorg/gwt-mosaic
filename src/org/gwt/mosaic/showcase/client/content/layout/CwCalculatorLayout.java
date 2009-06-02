/*
 * Copyright 2008 IT Mill Ltd.
 * 
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
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwCalculatorLayout extends ContentWidget implements ClickListener {

  /** The label used as the display */
  @ShowcaseData
  private TextBox display = null;

  /** Last completed result */
  @ShowcaseData
  private double stored = 0.0;

  /** The number being currently edited. */
  @ShowcaseData
  private double current = 0.0;

  /** Last activated operation. */
  @ShowcaseData
  private String operation = "C";

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwCalculatorLayout(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "A GridLayout demo";
  }

  @Override
  public String getName() {
    return "Calculator";
  }

  /**
   * The button listener method called any time a button is pressed.
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @ShowcaseSource
  public void onClick(Widget sender) {
    try {
      current = current * 10 + Double.parseDouble(((Button) sender).getText());
      display.setText(Double.toString(current));
      display.setTextAlignment(TextBox.ALIGN_RIGHT);
    } catch (NumberFormatException e) {
      // Operation button pressed
      if (operation.equals("+")) {
        stored += current;
      }
      if (operation.equals("-")) {
        stored -= current;
      }
      if (operation.equals("*")) {
        stored *= current;
      }
      if (operation.equals("/")) {
        stored /= current;
      }
      if (operation.equals("C")) {
        stored = current;
      }
      if (((Button) sender).getText().equals("C")) {
        stored = 0.0;
      }
      operation = ((Button) sender).getText();
      current = 0.0;
      display.setText(Double.toString(stored));
    }
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new GridLayout(4, 5));

    display = new TextBox();
    display.setReadOnly(true);

    layoutPanel.add(display, new GridLayoutData(3, 1, true));
    layoutPanel.add(new Button("<b>C</b>", this), new GridLayoutData(true));

    layoutPanel.add(new Button("7", this));
    layoutPanel.add(new Button("8", this));
    layoutPanel.add(new Button("9", this));
    layoutPanel.add(new Button("<b>/</b>", this), new GridLayoutData(true));

    layoutPanel.add(new Button("4", this));
    layoutPanel.add(new Button("5", this));
    layoutPanel.add(new Button("6", this));
    layoutPanel.add(new Button("<b>*</b>", this), new GridLayoutData(true));

    layoutPanel.add(new Button("1", this));
    layoutPanel.add(new Button("2", this));
    layoutPanel.add(new Button("3", this));
    layoutPanel.add(new Button("<b>-</b>", this), new GridLayoutData(true));

    layoutPanel.add(new Button("0", this));
    layoutPanel.add(new Button("<b>=</b>", this), new GridLayoutData(2, 1, true));
    layoutPanel.add(new Button("<b>+</b>", this), new GridLayoutData(true));

    return layoutPanel;
  }

}
