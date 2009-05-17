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
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.Label;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the basic FormLayout sizes: constant, minimum, preferred.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwBasicSizesExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBasicSizesExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates the basic FormLayout sizing options: "
        + "constant, minimum and preferred. "
        + "The text components have different minimum and preferred sizes.";
  }

  @Override
  public String getName() {
    return "Basic Sizes";
  }

  @Override
  public boolean hasStyle() {
    return false;
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
    tabPanel.add(buildHorizontalSizesPanel(), "Horizontal");
    tabPanel.add(buildVerticalSizesPanel(), "Vertical");

    return tabPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildHorizontalSizesPanel() {
    FormLayout layout = new FormLayout(
        "pref, 12px, 75px, 25px, min, 25px, pref", "pref, 12px, pref");

    // Create a panel that uses the layout.
    LayoutPanel panel = new ScrollLayoutPanel(layout);

    // Add components to the panel.
    panel.add(newLabel("75px"), CellConstraints.xy(3, 1));
    panel.add(newLabel("Min"), CellConstraints.xy(5, 1));
    panel.add(newLabel("Pref"), CellConstraints.xy(7, 1));

    panel.add(newLabel("new TextBox(15)"), CellConstraints.xy(1, 3));

    panel.add(createTextBox(15), CellConstraints.xy(3, 3));
    panel.add(createTextBox(15), CellConstraints.xy(5, 3));
    panel.add(createTextBox(15), CellConstraints.xy(7, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget createTextBox(int cols) {
    TextBox textBox = new TextBox();
    textBox.setWidth(cols + "em");
    return textBox;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildVerticalSizesPanel() {
    FormLayout layout = new FormLayout("pref, 12px, pref",
        "pref, 12px, 45px, 12px, min, 12px, pref");

    // Create a panel that uses the layout.
    LayoutPanel panel = new ScrollLayoutPanel(layout);

    // Add components to the panel.
    panel.add(newLabel("new TextArea() 10x40"), CellConstraints.xy(3, 1));

    panel.add(newLabel("45px"), CellConstraints.xy(1, 3));
    panel.add(newLabel("Min"), CellConstraints.xy(1, 5));
    panel.add(newLabel("Pref"), CellConstraints.xy(1, 7));

    panel.add(createTextArea(10, 40), CellConstraints.xy(3, 3));
    panel.add(createTextArea(10, 40), CellConstraints.xy(3, 5));
    panel.add(createTextArea(10, 40), CellConstraints.xy(3, 7));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget createTextArea(int rows, int cols) {
    TextArea textArea = new TextArea();
    textArea.setVisibleLines(rows);
    textArea.setCharacterWidth(cols);
    return textArea;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newLabel(String string) {
    final Label label = new Label(string);
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    return label;
  }
  
}
