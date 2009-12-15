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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.factories.DefaultWidgetFactory;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.TextLabel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates a <em>pure</em> use of the FormLayout. Columns and rows are
 * specified before the panel is filled with components. And the panel is filled
 * without a builder.
 * <p>
 * This panel building style is simple but not recommended. Other panel building
 * styles use a builder to fill the panel and/or create form rows dynamically.
 * See the {@link PanelBuilderExample} for a slightly better panel building
 * style that can use the builder to create text labels and separators.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwPlainExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwPlainExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates the very basic approach to add components to a panel. "
        + "Defines columns and rows statically, and adds components to the container without helper classes.";
  }

  @Override
  public String getName() {
    return "Plain";
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

    LayoutPanel panel = new ScrollLayoutPanel(layout);
    // panel.setBorder(Borders.DIALOG_BORDER);

    new CellConstraints();
    panel.add(createSeparator("Manufacturer"), CellConstraints.xyw(1, 1, 7));
    new CellConstraints();
    panel.add(newLabel("Company:"), CellConstraints.xy(1, 3));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xyw(3, 3, 5));
    new CellConstraints();
    panel.add(newLabel("Contact:"), CellConstraints.xy(1, 5));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xyw(3, 5, 5));
    new CellConstraints();
    panel.add(newLabel("Order No:"), CellConstraints.xy(1, 7));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xy(3, 7));

    new CellConstraints();
    panel.add(createSeparator("Inspector"), CellConstraints.xyw(1, 9, 7));
    new CellConstraints();
    panel.add(newLabel("Name:"), CellConstraints.xy(1, 11));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xyw(3, 11, 5));
    new CellConstraints();
    panel.add(newLabel("Reference No:"), CellConstraints.xy(1, 13));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xy(3, 13));
    new CellConstraints();
    panel.add(newLabel("Status:"), CellConstraints.xy(1, 15));
    new CellConstraints();
    panel.add(createApprovalStatusComboBox(), CellConstraints.xy(3, 15));

    new CellConstraints();
    panel.add(createSeparator("Ship"), CellConstraints.xyw(1, 17, 7));
    new CellConstraints();
    panel.add(newLabel("Shipyard:"), CellConstraints.xy(1, 19));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xyw(3, 19, 5));
    new CellConstraints();
    panel.add(newLabel("Register No:"), CellConstraints.xy(1, 21));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xy(3, 21));
    new CellConstraints();
    panel.add(newLabel("Hull No:"), CellConstraints.xy(5, 21));
    new CellConstraints();
    panel.add(new TextBox(), CellConstraints.xy(7, 21));
    new CellConstraints();
    panel.add(newLabel("Project type:"), CellConstraints.xy(1, 23));
    new CellConstraints();
    panel.add(createProjectTypeComboBox(), CellConstraints.xy(3, 23));

    return panel;
  }

  /**
   * Creates and returns a combo box for the approval states.
   * 
   * @return a combo box for the approval status
   */
  @ShowcaseSource
  private ListBox createApprovalStatusComboBox() {
    final ListBox listBox = new ListBox();
    listBox.addItem("In Progress");
    listBox.addItem("Finished");
    listBox.addItem("Released");
    return listBox;
  }

  /**
   * Creates and returns a combo box for the project types.
   * 
   * @return a combo box for the project type
   */
  @ShowcaseSource
  private ListBox createProjectTypeComboBox() {
    final ListBox listBox = new ListBox();
    listBox.addItem("New Building");
    listBox.addItem("Conversion");
    listBox.addItem("Repair");
    return listBox;
  }

  /**
   * Creates and returns a separator with a label in the left hand side.
   * 
   * @param textWidthMnemonic the label's text
   * @return a separator with label in the left hand side
   */
  @ShowcaseSource
  private Widget createSeparator(String textWidthMnemonic) {
    return DefaultWidgetFactory.getInstance().createSeparator(textWidthMnemonic);
  }

  /**
   * Creates and returns a label.
   * 
   * @return a label
   */
  @ShowcaseSource
  private Widget newLabel(String string) {
    final TextLabel label = new TextLabel(string);
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    return label;
  }
  
}
