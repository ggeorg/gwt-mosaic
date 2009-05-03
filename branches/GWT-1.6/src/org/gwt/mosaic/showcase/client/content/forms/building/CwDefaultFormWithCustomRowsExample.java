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

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Shows three approaches how to add custom rows to a form that is built using a
 * DefaultFormBuilder.
 * <ol>
 * <li>single custom row,
 * <li>standard + custom row,
 * <li>multiple standard rows.
 * </ol>
 * These differ in the position of the 'Comment' label, the alignment of font
 * baselines and the height of the custom row.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwDefaultFormWithCustomRowsExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDefaultFormWithCustomRowsExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Shows three ways to add custom rows to a DefaultFormBuilder: "
        + "1) single custom row, 2) standard + custom row, 3) multiple standard rows. "
        + "They differ in the position of the 'Comment' label, "
        + "the alignment of font baselines and the height of the custom row.";
  }

  @Override
  public String getName() {
    return "Custom Rows";
  }

  @Override
  public boolean hasStyle() {
    return false;
  }

  /**
   * 
   */
  @ShowcaseSource
  private TextBox newTextBox(String text) {
    final TextBox textBox = new TextBox();
    textBox.setText(text);
    return textBox;
  }

  /**
   * 
   */
  @ShowcaseSource
  private TextArea newTextArea(int cols, int rows, String text) {
    final TextArea textArea = new TextArea();
    textArea.setText(text);
    textArea.setCharacterWidth(cols);
    textArea.setVisibleLines(rows);
    return textArea;
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    TextBox name1Field = newTextBox("Name - font baselines shall be aligned");
    TextBox name2Field = newTextBox("Name - font baselines shall be aligned");
    TextBox name3Field = newTextBox("Name - font baselines shall be aligned");
    TextArea comment1Area = newTextArea(2, 2,
        "Comment - likely baselines are unaligned");
    TextArea comment2Area = newTextArea(20, 2,
        "Comment - baselines shall be aligned");
    TextArea comment3Area = newTextArea(2, 2,
        "Comment - baselines shall be aligned");

    // Column specs only, rows will be added dynamically.
    FormLayout layout = new FormLayout("right:pref, 3dlu, min:grow");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    // builder.setDefaultDialogBorder();
    builder.setRowGroupingEnabled(true);

    // In this approach, we add a gap and a custom row.
    // The advantage of this approach is, that we can express
    // the row spec and comment area cell constraints freely.
    // The disadvantage is the misalignment of the leading label.
    // Also the row's height may be inconsistent with other rows.
    builder.appendSeparator("Single Custom Row");
    builder.append("Name:", name1Field);
    builder.appendRow(builder.getLineGapSpec());
    builder.appendRow("top:31dlu"); // Assumes line is 14, gap is 3
    builder.nextLine(2);
    builder.append("Comment:");
    builder.add(comment1Area, CellConstraints.xy(builder.getColumn(),
        builder.getRow(), "fill, fill"));
    builder.nextLine();

    // In this approach, we append a standard row with gap before it.
    // The advantage is, that the leading label is aligned well.
    // The disadvantage is that the comment area now spans
    // multiple cells and is slightly less flexible.
    // Also the row's height may be inconsistent with other rows.
    builder.appendSeparator("Standard + Custom Row");
    builder.append("Name:", name2Field);
    builder.append("Comment:");
    builder.appendRow("17dlu"); // Assumes line is 14, gap is 3
    builder.add(comment2Area, CellConstraints.xywh(builder.getColumn(),
        builder.getRow(), 1, 2));
    builder.nextLine(2);

    // In this approach, we append two standard rows with associated gaps.
    // The advantage is, that the leading label is aligned well,
    // and the height is consistent with other rows.
    // The disadvantage is that the comment area now spans
    // multiple cells and is slightly less flexible.
    builder.appendSeparator("Two Standard Rows");
    builder.append("Name:", name3Field);
    builder.append("Comment:");
    builder.nextLine();
    builder.append("");
    builder.nextRow(-2);
    builder.add(comment3Area, CellConstraints.xywh(builder.getColumn(),
        builder.getRow(), 1, 3));

    return builder.getPanel();
  }
  
}
