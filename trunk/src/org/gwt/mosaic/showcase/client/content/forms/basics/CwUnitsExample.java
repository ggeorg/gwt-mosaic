/*
 * Copyright (c) 2009 GWT Mosaic Georgopoulos J. Georgios
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
/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.gwt.mosaic.showcase.client.content.forms.basics;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the basic FormLayout sizes: constant, minimum, preferred.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwUnitsExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwUnitsExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates the different sizing units supported by the FormLayout:"
        + "Pixel, Dialog Units (dlu), Point, Millimeter, Centimeter and Inch.";
  }

  @Override
  public String getName() {
    return "Units";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    // final LayoutPanel layoutPanel = new LayoutPanel();

    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.add(buildHorizontalUnitsPanel(), "Horizontal");
    tabPanel.add(buildHorizontalDluPanel(), "Horizontal Dlu");
    tabPanel.add(buildVerticalUnitsPanel(), "Vertical");
    tabPanel.add(buildVerticalDluPanel(), "Vertical Dlu");

    return tabPanel;
  }

  private Widget buildHorizontalUnitsPanel() {
    FormLayout layout = new FormLayout(
        "right:pref, 1dlu, left:pref, 4dlu, left:pref");
//    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
//    builder.setDefaultDialogBorder();
//
//    builder.append("72", newLabel("pt"), buildHorizontalPanel("72pt"));
//    builder.append("25.4", newLabel("mm"), buildHorizontalPanel("25.4mm"));
//    builder.append("2.54", newLabel("cm"), buildHorizontalPanel("2.54cm"));
//    builder.append("1", newLabel("in"), buildHorizontalPanel("1in"));
//    builder.append("72", newLabel("px"), buildHorizontalPanel("72px"));
//    builder.append("96", newLabel("px"), buildHorizontalPanel("96px"));
//    builder.append("120", newLabel("px"), buildHorizontalPanel("120px"));
//
//    return builder.getPanel();
    return null;
  }

  private Widget buildHorizontalDluPanel() {
    FormLayout layout = new FormLayout(
        "right:pref, 1dlu, left:pref, 4dlu, left:pref");
    //DefaultFormBuilder builder = new DefaultFormBuilder(layout);
//    builder.setDefaultDialogBorder();
//
//    builder.append("9", newLabel("cols"), buildHorizontalPanel(9));
//    builder.append("50", newLabel("dlu"), buildHorizontalPanel("50dlu"));
//    builder.append("75", newLabel("px"), buildHorizontalPanel("75px"));
//    builder.append("88", newLabel("px"), buildHorizontalPanel("88px"));
//    builder.append("100", newLabel("px"), buildHorizontalPanel("100px"));
//
//    return builder.getPanel();
    return null;
  }

  private Widget buildVerticalUnitsPanel() {
    FormLayout layout = new FormLayout("6*(c:p, 4dlu), c:p",
        "pref, 6dlu, top:pref");

    LayoutPanel panel = new LayoutPanel(layout);

    panel.add(newLabel("72 pt"), new CellConstraints().xy(1, 1));
    panel.add(buildVerticalPanel("72pt"), new CellConstraints().xy(1, 3));

    panel.add(newLabel("25.4 mm"), new CellConstraints().xy(3, 1));
    panel.add(buildVerticalPanel("25.4mm"), new CellConstraints().xy(3, 3));

    panel.add(newLabel("2.54 cm"), new CellConstraints().xy(5, 1));
    panel.add(buildVerticalPanel("2.54cm"), new CellConstraints().xy(5, 3));

    panel.add(newLabel("1 in"), new CellConstraints().xy(7, 1));
    panel.add(buildVerticalPanel("1in"), new CellConstraints().xy(7, 3));

    panel.add(newLabel("72 px"), new CellConstraints().xy(9, 1));
    panel.add(buildVerticalPanel("72px"), new CellConstraints().xy(9, 3));

    panel.add(newLabel("96 px"), new CellConstraints().xy(11, 1));
    panel.add(buildVerticalPanel("96px"), new CellConstraints().xy(11, 3));

    panel.add(newLabel("120 px"), new CellConstraints().xy(13, 1));
    panel.add(buildVerticalPanel("120px"), new CellConstraints().xy(13, 3));

    return panel;
  }

  private Widget buildVerticalDluPanel() {
    FormLayout layout = new FormLayout("6*(c:p, 4dlu), c:p",
        "pref, 6dlu, top:pref, 25dlu, pref, 6dlu, top:pref");

    LayoutPanel panel = new LayoutPanel(layout);

    panel.add(newLabel("4 rows"), new CellConstraints().xy(1, 1));
    panel.add(buildVerticalPanel("pref", 4), new CellConstraints().xy(1, 3));

    panel.add(newLabel("42 dlu"), new CellConstraints().xy(3, 1));
    panel.add(buildVerticalPanel("42dlu", 4), new CellConstraints().xy(3, 3));

    panel.add(newLabel("57 px"), new CellConstraints().xy(5, 1));
    panel.add(buildVerticalPanel("57px", 4), new CellConstraints().xy(5, 3));

    panel.add(newLabel("63 px"), new CellConstraints().xy(7, 1));
    panel.add(buildVerticalPanel("63px", 4), new CellConstraints().xy(7, 3));

    panel.add(newLabel("68 px"), new CellConstraints().xy(9, 1));
    panel.add(buildVerticalPanel("68px", 4), new CellConstraints().xy(9, 3));

    panel.add(newLabel("field"), new CellConstraints().xy(1, 5));
    panel.add(createTextBox(4), new CellConstraints().xy(1, 7));

    panel.add(newLabel("14 dlu"), new CellConstraints().xy(3, 5));
    panel.add(buildVerticalPanel("14dlu"), new CellConstraints().xy(3, 7));

    panel.add(newLabel("21 px"), new CellConstraints().xy(5, 5));
    panel.add(buildVerticalPanel("21px"), new CellConstraints().xy(5, 7));

    panel.add(newLabel("23 px"), new CellConstraints().xy(7, 5));
    panel.add(buildVerticalPanel("23px"), new CellConstraints().xy(7, 7));

    panel.add(newLabel("24 px"), new CellConstraints().xy(9, 5));
    panel.add(buildVerticalPanel("24px"), new CellConstraints().xy(9, 7));

    panel.add(newLabel("button"), new CellConstraints().xy(11, 5));
    panel.add(new Button("..."), new CellConstraints().xy(11, 7));

    return panel;
  }

  // Helper Code ************************************************************

  private LayoutPanel buildHorizontalPanel(String width) {
    FormLayout layout = new FormLayout(width, "pref");
    LayoutPanel panel = new LayoutPanel(layout);
    panel.add(new TextBox(), new CellConstraints(1, 1));
    return panel;
  }

  private LayoutPanel buildHorizontalPanel(int columns) {
    FormLayout layout = new FormLayout("pref, 4dlu, pref", "pref");
    LayoutPanel panel = new LayoutPanel(layout);
    CellConstraints cc = new CellConstraints();
    panel.add(createTextBox(columns), cc.xy(1, 1));
    panel.add(newLabel("Width of new JTextField(" + columns + ")"), cc.xy(3, 1));
    return panel;
  }

  private LayoutPanel buildVerticalPanel(String height) {
    return buildVerticalPanel(height, 10);
  }

  private LayoutPanel buildVerticalPanel(String height, int rows) {
    FormLayout layout = new FormLayout("pref", "fill:" + height);
    LayoutPanel panel = new LayoutPanel(layout);
    CellConstraints cc = new CellConstraints();
    panel.add(createTextArea(rows, 5), cc.xy(1, 1));
    return panel;
  }

  // Component Creation *****************************************************

  private Widget createTextBox(int cols) {
    TextBox textBox = new TextBox();
    textBox.setWidth(1 + "em");
    return textBox;
  }

  private Widget createTextArea(int rows, int cols) {
    TextArea textArea = new TextArea();
    textArea.setVisibleLines(rows);
    textArea.setCharacterWidth(cols);
    return textArea;
  }

  private Widget newLabel(String string) {
    final Label label = new Label(string);
    DOM.setStyleAttribute(label.getElement(), "display", "inline");
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    return label;
  }
}
