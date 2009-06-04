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
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Compares approaches how to append a custom area at the end of a panel built
 * with the DefaultFormBuilder:
 * <ol>
 * <li>using two custom rows to align the leading label,
 * <li>using a single custom row with label on top,
 * <li>using a separator.
 * </ol>
 * These differ in the position of the leading 'Feedback" label, and in turn in
 * the alignment of font baselines between label and the text area.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwDefaultFormWithCustomAreasExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDefaultFormWithCustomAreasExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Compares approaches how to append a custom area at the end of a panel built with the DefaultFormBuilder. "
        + "1) Using two custom rows to align the leading label, "
        + "2) using a single custom row with label on top, 3) using a separator.";
  }

  @Override
  public String getName() {
    return "Custom Areas";
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
    tabPanel.add(buildCustomAreaWithAlignedLabelPanel(), "Aligned label");
    tabPanel.add(buildCustomAreaWithTopLabelPanel(), "Top label");
    tabPanel.add(buildCustomAreaWithSeparatorPanel(), "Separator");

    return tabPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private DefaultFormBuilder buildPanelHeader() {
    // Column specs only, rows will be added dynamically.
    FormLayout layout = new FormLayout("right:pref, 3dlu, min:grow");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    // builder.setDefaultDialogBorder();
    builder.setRowGroupingEnabled(true);

    builder.appendSeparator("Customer Data");
    builder.append("Last Name:", new TextBox());
    builder.append("First Name:", new TextBox());
    builder.append("Street:", new TextBox());
    builder.append("Email:", new TextBox());

    return builder;
  }

  /**
   * Demonstrates how to append a larger custom area at the end of a panel that
   * is build with a {@link DefaultFormBuilder}.
   * <p>
   * 
   * We add a gap and a single custom row that grows and that is filled
   * vertically (where the default is center vertically). The area uses a
   * standard leading label.
   * 
   * @return the custom area panel with aligned labels
   */
  @ShowcaseSource
  private Widget buildCustomAreaWithAlignedLabelPanel() {
    DefaultFormBuilder builder = buildPanelHeader();

    builder.append("Feedback:");
    builder.appendRow("0:grow");
    TextArea textArea = (TextArea) builder.add(new TextArea(),
        CellConstraints.xywh(builder.getColumn(), builder.getRow(), 1, 2,
            "fill, fill"));
    textArea.setText("Feedback - font baselines shall be aligned");

    return builder.getPanel();
  }

  /**
   * Demonstrates how to append two custom areas at the end of a panel that is
   * build with a DefaultFormBuilder.
   * 
   * @return the custom area panel with label in the top
   */
  @ShowcaseSource
  private Widget buildCustomAreaWithTopLabelPanel() {
    DefaultFormBuilder builder = buildPanelHeader();

    builder.appendRow(builder.getLineGapSpec());
    builder.appendRow("top:28dlu:grow");
    builder.nextLine(2);
    builder.append("Feedback:");
    TextArea textArea = (TextArea) builder.add(new TextArea(),
        CellConstraints.xy(builder.getColumn(), builder.getRow(), "fill, fill"));
    textArea.setText("Feedback - likely the baselines are not aligned");

    return builder.getPanel();
  }

  /**
   * Demonstrates how to append a larger custom area at the end of a panel that
   * is build with a DefaultFormBuilder.
   * <p>
   * 
   * We add a gap and a single custom row that grows and that is filled
   * vertically (where the default is center vertically). The area is separated
   * by a titled separator and it is indented using an empty leading label.
   * 
   * @return the custom area panel with separators
   */
  @ShowcaseSource
  private Widget buildCustomAreaWithSeparatorPanel() {
    DefaultFormBuilder builder = buildPanelHeader();

    builder.appendSeparator("Customer Feedback");
    builder.appendRow(builder.getLineGapSpec());
    builder.appendRow("fill:28dlu:grow");
    builder.nextLine(2);
    builder.append("", new TextArea());

    return builder.getPanel();
  }

}