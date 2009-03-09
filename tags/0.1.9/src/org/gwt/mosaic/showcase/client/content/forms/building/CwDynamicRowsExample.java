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
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Combines the FormLayout with the PanelBuilder. Columns and rows are specified
 * before the panel is filled with components. The builder's cursor is used to
 * determine the location of the next component. And the builder's convenience
 * methods are used to add labels and separators.
 * <p>
 * This panel building style is intended for learning purposes only. The
 * recommended style is demonstrated in the {@link CwDefaultFormBuilderExample}.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwDynamicRowsExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDynamicRowsExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to add rows dynamically to a FormLayout. "
        + "Defines columns statically and rows dynamically, "
        + "and adds components via the PanelBuilder.";
  }

  @Override
  public String getName() {
    return "Dynamic Rows";
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
            + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
            + "p, 3dlu, p, 3dlu, p, 3dlu, p");

    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    builder.addSeparator("Segment");
    builder.nextLine(2);

    builder.addLabel("Identifier:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextLine(2);

    builder.addLabel("PTI/kW:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextColumn(2);
    builder.addLabel("Power/kW:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextLine(2);

    builder.addLabel("len/mm:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextLine(2);

    builder.addSeparator("Diameters");
    builder.nextLine(2);

    builder.addLabel("da/mm:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextColumn(2);
    builder.addLabel("di/mm:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextLine(2);

    builder.addLabel("da2/mm:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextColumn(2);
    builder.addLabel("di2/mm:");
    builder.nextColumn(2);
    builder.add(new TextBox());

    builder.nextLine(2);
    builder.addLabel("R/mm:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextColumn(2);
    builder.addLabel("D/mm:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextLine(2);

    builder.addSeparator("Criteria");
    builder.nextLine(2);

    builder.addLabel("Location:");
    builder.nextColumn(2);
    builder.add(createLocationComboBox());
    builder.nextColumn(2);
    builder.addLabel("k-factor:");
    builder.nextColumn(2);
    builder.add(new TextBox());
    builder.nextLine(2);

    builder.addLabel("Holes:");
    builder.nextColumn(2);
    builder.setColumnSpan(5);
    ((CheckBox) builder.add(new CheckBox("Has radial holes"))).setChecked(true);
    builder.setColumnSpan(1);
    builder.nextLine(2);

    builder.addLabel("Slots:");
    builder.nextColumn(2);
    builder.setColumnSpan(5);
    builder.add(new CheckBox("Has logitudinal "));
    builder.setColumnSpan(1);

    return builder.getPanel();
  }

  /**
   * Creates and returns a combo box for the project types.
   * 
   * @return a combo box for the project type
   */
  private ListBox createLocationComboBox() {
    final ListBox listBox = new ListBox();
    listBox.addItem("Propeller nut thread");
    listBox.addItem("Stern tube front area");
    listBox.addItem("Shaft taper");
    return listBox;
  }

}
