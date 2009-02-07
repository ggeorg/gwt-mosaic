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
package org.gwt.mosaic.showcase.client.content.forms.building;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the FormLayout with a PanelBuilder. Columns and rows are
 * specified before the panel is filled with components. Unlike the
 * {@link PlainExample} this class uses a local variable to keep track of the
 * current row. The advantage over fixed numbers is, that it's easier to insert
 * new rows later.
 * <p>
 * This panel building style is simple and works quite well. However, you may
 * consider using a more sophisticated form builder to fill the panel and/or add
 * rows dynamically; see the {@link CwDynamicRowsExample} for this alternative.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwRowCounterExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwRowCounterExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to fill a FormLayout using a PanelBuilder. "
        + "Defines columns and rows statically, adds components to a PanelBuilder, "
        + "and uses a variable to track the current row.";
  }

  @Override
  public String getName() {
    return "Row Counter";
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
    FormLayout layout = new FormLayout(
        "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
            + "right:[40dlu,pref], 3dlu, 70dlu",
        "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
            + "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");
    layout.setRowGroups(new int[][] {{3, 13, 15, 17}});

    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    int row = 1;

    builder.addSeparator("Shaft", CellConstraints.xyw(1, row++, 7));
    row++;

    builder.addLabel("Identifier:", CellConstraints.xy(1, row));
    builder.add(new TextBox(), CellConstraints.xy(3, row++));
    row++;

    builder.addLabel("Power:", CellConstraints.xy(1, row));
    builder.add(new TextBox(), CellConstraints.xy(3, row));
    builder.addLabel("Speed:", CellConstraints.xy(5, row));
    builder.add(new TextBox(), CellConstraints.xy(7, row++));
    row++;

    builder.addLabel("Material:", CellConstraints.xy(1, row));
    builder.add(materialComboBox(), CellConstraints.xy(3, row));
    builder.addLabel("Ice class:", CellConstraints.xy(5, row));
    builder.add(iceClassComboBox(), CellConstraints.xy(7, row++));
    row++;

    builder.addSeparator("Comments", CellConstraints.xyw(1, row++, 7));
    row++;

    builder.addLabel("Machinery:", CellConstraints.xy(1, row));
    builder.add(new TextArea(), CellConstraints.xywh(3, row++, 5, 3, "f, f"));
    row += 3;

    builder.addLabel("Inspection:", CellConstraints.xy(1, row));
    builder.add(new TextArea(), CellConstraints.xywh(3, row++, 5, 3, "f, f"));

    return builder.getPanel();
  }

  /**
   * Builds and returns a combo box for materials.
   * 
   * @return a combo box for different materials
   */
  @ShowcaseSource
  private ListBox materialComboBox() {
    final ListBox listBox = new ListBox();
    listBox.addItem("C45E, ReH=600");
    listBox.addItem("Bolt Material, ReH=800");
    return listBox;
  }

  /**
   * Builds and returns a combo box for ice classes.
   * 
   * @return a combo box for a bunch of ice classes
   */
  @ShowcaseSource
  private ListBox iceClassComboBox() {
    final ListBox listBox = new ListBox();
    listBox.addItem("E");
    listBox.addItem("E1");
    listBox.addItem("E2");
    listBox.addItem("E3");
    listBox.addItem("E4");
    return listBox;
  }

}
