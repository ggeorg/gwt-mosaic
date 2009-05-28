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

import org.gwt.mosaic.forms.client.builder.DefaultFormBuilder;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Uses the FormLayout and the <code>DefaultFormBuilder</code>.
 * Columns are specified before the panel is filled with components,
 * rows are added dynamically. The builder is used to hold a cursor,
 * to add rows dynamically, and to fill components.
 * The builder's convenience methods are used to add labels and separators.<p>
 *
 * This panel building style is recommended unless you have a more
 * powerful builder or don't want to add rows dynamically.
 * See the {@link DynamicRowsExample} for an implementation that specifies
 * rows before the panel is filled with components.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwDefaultFormBuilderExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDefaultFormBuilderExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to fill a FormLayout using the DefaultFormBuilder. "
        + "Defines columns statically and rows dynamically, "
        + "and appends widgets via the DefaultFormBuilder.";
  }

  @Override
  public String getName() {
    return "Default Form";
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
    // Column specs only, rows will be added dynamically.
    FormLayout layout = new FormLayout(
        "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
            + "right:[40dlu,pref], 3dlu, 70dlu");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    // builder.setDefaultDialogBorder();

    builder.appendSeparator("Flange");

    builder.append("Identifier:", new TextBox());
    builder.nextLine();

    builder.append("PTI/kW:", new TextBox());
    builder.append("Power/kW:", new TextBox());

    builder.append("s/mm:", new TextBox());
    builder.nextLine();

    builder.appendSeparator("Diameters");

    builder.append("da/mm:", new TextBox());
    builder.append("di/mm:", new TextBox());

    builder.append("da2/mm:", new TextBox());
    builder.append("di2/mm:", new TextBox());

    builder.append("R/mm:", new TextBox());
    builder.append("D/mm:", new TextBox());

    builder.appendSeparator("Criteria");

    builder.append("Location:", createLocationComboBox());
    builder.append("k-factor:", new TextBox());

    return builder.getPanel();
  }

  /**
   * Creates and returns a combo box for the project types.
   * 
   * @return a combo box for the project type
   */
  @ShowcaseSource
  private ListBox createLocationComboBox() {
    final ListBox listBox = new ListBox();
    listBox.addItem("Propeller nut thread");
    listBox.addItem("Stern tube front area");
    listBox.addItem("Shaft taper");
    return listBox;
  }

}