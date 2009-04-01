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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates a typical use of the FormLayout. Columns and rows are specified
 * before the panel is filled with components, and the panel is filled with a
 * PanelBuilder.
 * <p>
 * Unlike the PlainExample, this implementation can delegate the widget creation
 * for text labels and titled separators to the builder.
 * <p>
 * This panel building style is recommended for panels with a medium number of
 * rows and components. If the panel has more rows, you may consider using a row
 * variable to address the current row.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwPanelBuilderExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwPanelBuilderExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates the very basic approach to add components to a panel. "
        + "Defines columns and rows statically, and adds components to the container using a PanelBuilder.";
  }

  @Override
  public String getName() {
    return "PanelBuilder";
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

    // Fill the table with labels and components.
    builder.addSeparator("Manufacturer", CellConstraints.xyw(1, 1, 7));
    builder.addLabel("Company:", CellConstraints.xy(1, 3));
    builder.add(new TextBox(), CellConstraints.xyw(3, 3, 5));
    builder.addLabel("Contact:", CellConstraints.xy(1, 5));
    builder.add(new TextBox(), CellConstraints.xyw(3, 5, 5));
    builder.addLabel("Order No:", CellConstraints.xy(1, 7));
    builder.add(new TextBox(), CellConstraints.xy(3, 7));

    builder.addSeparator("Inspector", CellConstraints.xyw(1, 9, 7));
    builder.addLabel("Name:", CellConstraints.xy(1, 11));
    builder.add(new TextBox(), CellConstraints.xyw(3, 11, 5));
    builder.addLabel("Reference No:", CellConstraints.xy(1, 13));
    builder.add(new TextBox(), CellConstraints.xy(3, 13));
    builder.addLabel("Status:", CellConstraints.xy(1, 15));
    builder.add(createApprovalStatusComboBox(), CellConstraints.xy(3, 15));

    builder.addSeparator("Ship", CellConstraints.xyw(1, 17, 7));
    builder.addLabel("Shipyard:", CellConstraints.xy(1, 19));
    builder.add(new TextBox(), CellConstraints.xyw(3, 19, 5));
    builder.addLabel("Register No:", CellConstraints.xy(1, 21));
    builder.add(new TextBox(), CellConstraints.xy(3, 21));
    builder.addLabel("Hull No:", CellConstraints.xy(5, 21));
    builder.add(new TextBox(), CellConstraints.xy(7, 21));
    builder.addLabel("Project type:", CellConstraints.xy(1, 23));
    builder.add(createProjectTypeComboBox(), CellConstraints.xy(3, 23));

    return builder.getPanel();
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
  
  @Override
  protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
    GWT.runAsync(new RunAsyncCallback() {

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      public void onSuccess() {
        callback.onSuccess(onInitialize());
      }
    });
  }

}
