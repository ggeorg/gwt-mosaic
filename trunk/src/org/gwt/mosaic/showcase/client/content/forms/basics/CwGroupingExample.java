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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the basic FormLayout sizes: constant, minimum, preferred.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwGroupingExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwGroupingExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to give columns and rows equal sizes."
        + "This is an essential feature for balanced and elegant design.";
  }

  @Override
  public String getName() {
    return "Grouping";
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
    tabPanel.add(buildWizardBar(false), "Ungrouped Bar");
    tabPanel.add(buildWizardBar(true), "Grouped Bar");
    tabPanel.add(buildEditorPanel(false), "Ungrouped Rows");
    tabPanel.add(buildEditorPanel(true), "Grouped Rows");

    return tabPanel;
  }

  private Widget buildWizardBar(boolean grouped) {
    FormLayout layout = new FormLayout(
        "pref, 6px:grow, pref, pref, 12px, pref, 6px, pref", "pref");
    if (grouped) {
      layout.setColumnGroups(new int[][] {{1, 3, 4, 6, 8}});
    }
    LayoutPanel panel = new LayoutPanel(layout);

    panel.add(new Button("Hilfe"), new CellConstraints().xy(1, 1));
    panel.add(new Button("< Zur\u00Fcck"), new CellConstraints().xy(3, 1));
    panel.add(new Button("Vor >"), new CellConstraints().xy(4, 1));
    panel.add(new Button("Beenden"), new CellConstraints().xy(6, 1));
    panel.add(new Button("Abbrechen"), new CellConstraints().xy(8, 1));

    return panel;
  }

  private Widget buildEditorPanel(boolean grouped) {
    FormLayout layout = new FormLayout(
        "pref, 3dlu, 35dlu, 2dlu, 35dlu, 2dlu, 35dlu, 2dlu, 35dlu",
        "8*(p, 2dlu), p");
    if (grouped) {
      layout.setRowGroups(new int[][] {{1, 3, 5, 7, 9, 11, 13, 15, 17}});
    }

    LayoutPanel panel = new LayoutPanel(layout);

    panel.add(newLabel("File number:"), new CellConstraints().xy(1, 1));
    panel.add(new TextBox(), new CellConstraints().xyw(3, 1, 7));
    panel.add(newLabel("BL/MBL number:"), new CellConstraints().xy(1, 3));
    panel.add(new TextBox(), new CellConstraints().xy(3, 3));
    panel.add(new TextBox(), new CellConstraints().xy(5, 3));
    panel.add(newLabel("Entry date:"), new CellConstraints().xy(1, 5));
    panel.add(new TextBox(), new CellConstraints().xy(3, 5));
    panel.add(newLabel("RFQ number:"), new CellConstraints().xy(1, 7));
    panel.add(new TextBox(), new CellConstraints().xyw(3, 7, 7));
    panel.add(newLabel("Goods:"), new CellConstraints().xy(1, 9));
    panel.add(new CheckBox("Dangerous"), new CellConstraints().xyw(3, 9, 7));
    panel.add(newLabel("Shipper:"), new CellConstraints().xy(1, 11));
    panel.add(new TextBox(), new CellConstraints().xyw(3, 11, 7));
    panel.add(newLabel("Customer:"), new CellConstraints().xy(1, 13));
    panel.add(new TextBox(), new CellConstraints().xyw(3, 13, 5));
    panel.add(new Button("\u2026"), new CellConstraints().xy(9, 13));
    panel.add(newLabel("Port of loading:"), new CellConstraints().xy(1, 15));
    panel.add(new TextBox(), new CellConstraints().xyw(3, 15, 7));
    panel.add(newLabel("Destination:"), new CellConstraints().xy(1, 17));
    panel.add(new TextBox(), new CellConstraints().xyw(3, 17, 7));

    return panel;
  }

  private Widget newLabel(String string) {
    final Label label = new Label(string);
    DOM.setStyleAttribute(label.getElement(), "display", "inline");
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    return label;
  }
}
