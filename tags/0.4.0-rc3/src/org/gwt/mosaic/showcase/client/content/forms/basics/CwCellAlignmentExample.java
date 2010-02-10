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
import org.gwt.mosaic.ui.client.TextLabel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates how FormLayout applies the default column and row alignments to
 * cells, and how to override the defaults.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwCellAlignmentExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwCellAlignmentExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how FormLayout applies the default column and row alignments to cells - "
        + "and how to override the defaults. The buttons show the cell constraint alignments. "
        + "Cells that specify no alignment use the column and row defaults. "
        + "Column default is Fill, row default is Center.";
  }

  @Override
  public String getName() {
    return "Cell Alignments";
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
    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.add(buildHorizontalPanel(), "Horizontal");
    tabPanel.add(buildVerticalPanel(), "Vertical");

    return tabPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildHorizontalPanel() {
    FormLayout layout = new FormLayout(
        "r:p, 4dlu, left:pref:g, center:pref:g, right:pref:g, pref:g",
        "pref, 8dlu, pref, pref, pref, pref, pref");
    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Column Spec: "), CellConstraints.xy(1, 1, "r, c"));
    panel.add(newLabel(" \"left:pref:grow\" "),
        CellConstraints.xy(3, 1, "c, c"));
    panel.add(newLabel(" \"center:pref:grow\" "), CellConstraints.xy(4, 1,
        "c, c"));
    panel.add(newLabel(" \"right:pref:grow\" "), CellConstraints.xy(5, 1,
        "c, c"));
    panel.add(newLabel(" \"pref:grow\" "), CellConstraints.xy(6, 1, "c, c"));

    int row = 3;
    addHorizontalButton(panel, 3, row, CellConstraints.DEFAULT);
    addHorizontalButton(panel, 4, row, CellConstraints.DEFAULT);
    addHorizontalButton(panel, 5, row, CellConstraints.DEFAULT);
    addHorizontalButton(panel, 6, row, CellConstraints.DEFAULT);

    row++;
    addHorizontalButton(panel, 3, row, CellConstraints.FILL);
    addHorizontalButton(panel, 4, row, CellConstraints.FILL);
    addHorizontalButton(panel, 5, row, CellConstraints.FILL);
    addHorizontalButton(panel, 6, row, CellConstraints.FILL);

    row++;
    addHorizontalButton(panel, 3, row, CellConstraints.LEFT);
    addHorizontalButton(panel, 4, row, CellConstraints.LEFT);
    addHorizontalButton(panel, 5, row, CellConstraints.LEFT);
    addHorizontalButton(panel, 6, row, CellConstraints.LEFT);

    row++;
    addHorizontalButton(panel, 3, row, CellConstraints.CENTER);
    addHorizontalButton(panel, 4, row, CellConstraints.CENTER);
    addHorizontalButton(panel, 5, row, CellConstraints.CENTER);
    addHorizontalButton(panel, 6, row, CellConstraints.CENTER);

    row++;
    addHorizontalButton(panel, 3, row, CellConstraints.RIGHT);
    addHorizontalButton(panel, 4, row, CellConstraints.RIGHT);
    addHorizontalButton(panel, 5, row, CellConstraints.RIGHT);
    addHorizontalButton(panel, 6, row, CellConstraints.RIGHT);

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildVerticalPanel() {
    FormLayout layout = new FormLayout("left:pref, 8dlu, p, c:p, p, p, p",
        "p, 4dlu, top:pref:g, center:pref:g, bottom:pref:g, pref:g");
    layout.setColumnGroups(new int[][] {{3, 5, 6, 7}});
    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Row Spec:"), CellConstraints.xy(1, 1, "r, c"));
    panel.add(newLabel("\"top:pref:grow\""), CellConstraints.xy(1, 3, "r, c"));
    panel.add(newLabel("\"center:pref:grow\""),
        CellConstraints.xy(1, 4, "r, c"));
    panel.add(newLabel("\"bottom:pref:grow\""),
        CellConstraints.xy(1, 5, "r, c"));
    panel.add(newLabel("\"pref:grow\""), CellConstraints.xy(1, 6, "r, c"));

    int col = 3;
    addVerticalButton(panel, col, 3, CellConstraints.DEFAULT);
    addVerticalButton(panel, col, 4, CellConstraints.DEFAULT);
    addVerticalButton(panel, col, 5, CellConstraints.DEFAULT);
    addVerticalButton(panel, col, 6, CellConstraints.DEFAULT);

    col++;
    addVerticalButton(panel, col, 3, CellConstraints.FILL);
    addVerticalButton(panel, col, 4, CellConstraints.FILL);
    addVerticalButton(panel, col, 5, CellConstraints.FILL);
    addVerticalButton(panel, col, 6, CellConstraints.FILL);

    col++;
    addVerticalButton(panel, col, 3, CellConstraints.TOP);
    addVerticalButton(panel, col, 4, CellConstraints.TOP);
    addVerticalButton(panel, col, 5, CellConstraints.TOP);
    addVerticalButton(panel, col, 6, CellConstraints.TOP);

    col++;
    addVerticalButton(panel, col, 3, CellConstraints.CENTER);
    addVerticalButton(panel, col, 4, CellConstraints.CENTER);
    addVerticalButton(panel, col, 5, CellConstraints.CENTER);
    addVerticalButton(panel, col, 6, CellConstraints.CENTER);

    col++;
    addVerticalButton(panel, col, 3, CellConstraints.BOTTOM);
    addVerticalButton(panel, col, 4, CellConstraints.BOTTOM);
    addVerticalButton(panel, col, 5, CellConstraints.BOTTOM);
    addVerticalButton(panel, col, 6, CellConstraints.BOTTOM);

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private void addHorizontalButton(LayoutPanel panel, int col, int row,
      CellConstraints.Alignment hAlignment) {
    Button button = new Button(hAlignment.toString());
    panel.add(button, new CellConstraints(col, row, hAlignment,
        CellConstraints.DEFAULT));
  }

  /**
   * 
   */
  @ShowcaseSource
  private void addVerticalButton(LayoutPanel panel, int col, int row,
      CellConstraints.Alignment vAlignment) {
    Button button = new Button(vAlignment.toString());
    panel.add(button, new CellConstraints(col, row, CellConstraints.DEFAULT,
        vAlignment));
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newLabel(String string) {
    final TextLabel label = new TextLabel(string);
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    return label;
  }
  
}
