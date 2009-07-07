/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopolos.
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
package org.gwt.mosaic.forms.client.builder;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.factories.FormFactory;
import org.gwt.mosaic.forms.client.layout.ConstantSize;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.forms.client.layout.RowSpec;
import org.gwt.mosaic.ui.client.Label;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;

import com.google.gwt.user.client.ui.Widget;

/**
 * Provides a means to build form-oriented panels quickly and consistently using
 * the {@link FormLayout}. This builder combines frequently used panel building
 * steps: add a new row, add a label, proceed to the next data column, then add
 * a component.
 * <p>
 * 
 * The extra value lies in the <code>#append</code> methods that append gap rows
 * and component rows if necessary and then add the given components. They are
 * built upon the superclass behavior <code>#appendRow</code> and the set of
 * <code>#add</code> methods. A set of component appenders allows to add a
 * textual label and associated component in a single step.
 * <p>
 * 
 * This builder can map resource keys to internationalized (i15d) texts when
 * creating text labels, titles and titled separators. Therefore you must
 * specify a <code>ResourceBundle</code> in the constructor. The builder methods
 * throw an <code>IllegalStateException</code> if one of the mapping builder
 * methods is invoked and no bundle has been set.
 * <p>
 * 
 * You can configure the build process by setting a leading column, enabling the
 * row grouping and by modifying the gaps between normal lines and between
 * paragraphs. The leading column will be honored if the cursor proceeds to the
 * next row. All appended components start in the specified lead column, except
 * appended separators that span all columns.
 * <p>
 * 
 * It is temptive to use the DefaultFormBuilder all the time and to let it add
 * rows automatically. Use a simpler style if it increases the code readability.
 * Explicit row specifications and cell constraints make your layout easier to
 * understand - but harder to maintain. See also the accompanying tutorial
 * sources and the Tips &amp; Tricks that are part of the Forms documentation.
 * <p>
 * 
 * Sometimes a form consists of many standardized rows but has a few rows that
 * require a customization. The DefaultFormBuilder can do everything that the
 * superclasses
 * {@link org.gwt.mosaic.ui.client.layout.builder.jgoodies.forms.builder.AbstractFormBuilder}
 * and
 * {@link org.gwt.mosaic.ui.client.layout.builder.jgoodies.forms.builder.PanelBuilder}
 * can do; among other things: appending new rows and moving the cursor. Again,
 * ask yourself if the DefaultFormBuilder is the appropriate builder. As a rule
 * of thumb you should have more components than builder commands. There are
 * different ways to add custom rows. Find below example code that presents and
 * compares the pros and cons of three approaches.
 * <p>
 * 
 * The texts used in methods <code>#append(String, ...)</code> and
 * <code>#appendTitle(String)</code> as well as the localized texts used in
 * methods <code>#appendI15d</code> and <code>#appendI15dTitle</code> can
 * contain an optional mnemonic marker. The mnemonic and mnemonic index are
 * indicated by a single ampersand (<tt>&amp;</tt>). For example
 * <tt>&quot;&amp;Save&quot</tt>, or <tt>&quot;Save&nbsp;&amp;as&quot</tt>. To
 * use the ampersand itself, duplicate it, for example
 * <tt>&quot;Look&amp;&amp;Feel&quot</tt>.
 * <p>
 * 
 * <strong>Example:</strong>
 * 
 * <pre>
 * public void build() {
 *   FormLayout layout = new FormLayout(
 *       &quot;right:max(40dlu;pref), 3dlu, 80dlu, 7dlu, &quot; // 1st major colum
 *           + &quot;right:max(40dlu;pref), 3dlu, 80dlu&quot;, // 2nd major column
 *       &quot;&quot;); // add rows dynamically
 *   DefaultFormBuilder builder = new DefaultFormBuilder(layout);
 *   builder.setDefaultDialogBorder();
 *   builder.appendSeparator(&quot;Flange&quot;);
 *   builder.append(&quot;Identifier&quot;, identifierField);
 *   builder.nextLine();
 *   builder.append(&quot;PTI [kW]&quot;, new JTextField());
 *   builder.append(&quot;Power [kW]&quot;, new JTextField());
 *   builder.append(&quot;s [mm]&quot;, new JTextField());
 *   builder.nextLine();
 *   builder.appendSeparator(&quot;Diameters&quot;);
 *   builder.append(&quot;da [mm]&quot;, new JTextField());
 *   builder.append(&quot;di [mm]&quot;, new JTextField());
 *   builder.append(&quot;da2 [mm]&quot;, new JTextField());
 *   builder.append(&quot;di2 [mm]&quot;, new JTextField());
 *   builder.append(&quot;R [mm]&quot;, new JTextField());
 *   builder.append(&quot;D [mm]&quot;, new JTextField());
 *   builder.appendSeparator(&quot;Criteria&quot;);
 *   builder.append(&quot;Location&quot;, buildLocationComboBox());
 *   builder.append(&quot;k-factor&quot;, new JTextField());
 *   builder.appendSeparator(&quot;Bolts&quot;);
 *   builder.append(&quot;Material&quot;, ViewerUIFactory.buildMaterialComboBox());
 *   builder.nextLine();
 *   builder.append(&quot;Numbers&quot;, new JTextField());
 *   builder.nextLine();
 *   builder.append(&quot;ds [mm]&quot;, new JTextField());
 * }
 * </pre>
 * <p>
 * 
 * <strong>Custom Row Example:</strong>
 * 
 * <pre>
 * public JComponent buildPanel() {
 *   initComponents();
 *   FormLayout layout = new FormLayout(&quot;right:pref, 3dlu, default:grow&quot;, &quot;&quot;);
 *   DefaultFormBuilder builder = new DefaultFormBuilder(layout);
 *   builder.setDefaultDialogBorder();
 *   builder.setRowGroupingEnabled(true);
 *   CellConstraints cc = new CellConstraints();
 *   // In this approach, we add a gap and a custom row.
 *   // The advantage of this approach is, that we can express
 *   // the row spec and comment area cell constraints freely.
 *   // The disadvantage is the misalignment of the leading label.
 *   // Also the row's height may be inconsistent with other rows.
 *   builder.appendSeparator(&quot;Single Custom Row&quot;);
 *   builder.append(&quot;Name&quot;, name1Field);
 *   builder.appendRow(builder.getLineGapSpec());
 *   builder.appendRow(new RowSpec(&quot;top:31dlu&quot;)); // Assumes line is 14, gap is 3
 *   builder.nextLine(2);
 *   builder.append(&quot;Comment&quot;);
 *   builder.add(new JScrollPane(comment1Area), cc.xy(builder.getColumn(),
 *       builder.getRow(), &quot;fill, fill&quot;));
 *   builder.nextLine();
 *   // In this approach, we append a standard row with gap before it.
 *   // The advantage is, that the leading label is aligned well.
 *   // The disadvantage is that the comment area now spans
 *   // multiple cells and is slightly less flexible.
 *   // Also the row's height may be inconsistent with other rows.
 *   builder.appendSeparator(&quot;Standard + Custom Row&quot;);
 *   builder.append(&quot;Name&quot;, name2Field);
 *   builder.append(&quot;Comment&quot;);
 *   builder.appendRow(new RowSpec(&quot;17dlu&quot;)); // Assumes line is 14, gap is 3
 *   builder.add(new JScrollPane(comment2Area), cc.xywh(builder.getColumn(),
 *       builder.getRow(), 1, 2));
 *   builder.nextLine(2);
 *   // In this approach, we append two standard rows with associated gaps.
 *   // The advantage is, that the leading label is aligned well,
 *   // and the height is consistent with other rows.
 *   // The disadvantage is that the comment area now spans
 *   // multiple cells and is slightly less flexible.
 *   builder.appendSeparator(&quot;Two Standard Rows&quot;);
 *   builder.append(&quot;Name&quot;, name3Field);
 *   builder.append(&quot;Comment&quot;);
 *   builder.nextLine();
 *   builder.append(&quot;&quot;);
 *   builder.nextRow(-2);
 *   builder.add(new JScrollPane(comment3Area), cc.xywh(builder.getColumn(),
 *       builder.getRow(), 1, 3));
 *   return builder.getPanel();
 * }
 * </pre>
 * <p>
 * 
 * TODO: Consider adding a method for appending a component that spans the
 * remaining columns in the current row. Method name candidates are
 * <code>#appendFullSpan</code> and <code>#appendRemaining</code>.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see org.gwt.mosaic.ui.client.layout.builder.jgoodies.forms.builder.AbstractFormBuilder
 * @see com.jgoodies.forms.factories.FormFactory
 * @see com.FormLayout.forms.layout.FormLayout
 */
public final class DefaultFormBuilder extends PanelBuilder {

  /**
   * Holds the row specification that is reused to describe rows that are
   * intended for labels and components.
   * 
   * @see #setDefaultRowSpec(RowSpec)
   */
  private RowSpec defaultRowSpec = FormFactory.PREF_ROWSPEC;

  /**
   * Holds the row specification that is reused to describe the constant gaps
   * between component lines.
   * 
   * @see #setLineGapSize(ConstantSize)
   */
  private RowSpec lineGapSpec = FormFactory.LINE_GAP_ROWSPEC;

  /**
   * Holds the row specification that describes the constant gaps between
   * paragraphs.
   * 
   * @see #setParagraphGapSize(ConstantSize)
   */
  private RowSpec paragraphGapSpec = FormFactory.PARAGRAPH_GAP_ROWSPEC;

  /**
   * Holds the offset of the leading column - often 0 or 1.
   * 
   * @see #getLeadingColumnOffset()
   * @see #setLeadingColumnOffset(int)
   * @see #getLeadingColumn()
   */
  private int leadingColumnOffset = 0;

  /**
   * Determines whether new data rows are being grouped or not.
   * 
   * @see #isRowGroupingEnabled()
   * @see #setRowGroupingEnabled(boolean)
   */
  private boolean rowGroupingEnabled = false;

  // Instance Creation ****************************************************

  /**
   * Constructs a <code>DefaultFormBuilder</code> for the given layout.
   * 
   * @param layout the <code>FormLayout</code> to be used
   */
  public DefaultFormBuilder(FormLayout layout) {
    this(layout, new LayoutPanel(new BoxLayout(Orientation.VERTICAL)));
  }

  /**
   * Constructs a <code>DefaultFormBuilder</code> for the given layout and
   * panel.
   * 
   * @param layout the <code>FormLayout</code> to be used
   * @param panel the layout container
   */
  public DefaultFormBuilder(FormLayout layout, LayoutPanel panel) {
    super(layout, panel);
  }

  // Configuration **********************************************************

  /**
   * Returns the row specification that is used for component rows.
   * 
   * @return the {@code RowSpec} used for component rows
   */
  public RowSpec getDefaultRowSpec() {
    return defaultRowSpec;
  }

  /**
   * Sets the row specification that shall be used for component rows. It is
   * {@link FormFactory#PREF_ROWSPEC} by default.
   * 
   * @param defaultRowSpec the RowSpec to be used for component rows
   */
  public void setDefaultRowSpec(RowSpec defaultRowSpec) {
    this.defaultRowSpec = defaultRowSpec;
  }

  /**
   * Returns the row specification that is used to separate component row.
   * 
   * @return the <code>RowSpec</code> that is used to separate component rows
   */
  public RowSpec getLineGapSpec() {
    return lineGapSpec;
  }

  /**
   * Sets the size of gaps between component lines using the given constant
   * size.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * builder.setLineGapSize(Sizes.ZERO);
   * builder.setLineGapSize(Sizes.DLUY9);
   * builder.setLineGapSize(Sizes.pixel(1));
   * </pre>
   * 
   * @param lineGapSize the <code>ConstantSize</code> that describes the size of
   *          the gaps between component lines
   */
  public void setLineGapSize(ConstantSize lineGapSize) {
    RowSpec rowSpec = RowSpec.createGap(lineGapSize);
    this.lineGapSpec = rowSpec;
  }

  /**
   * Sets the size of gaps between paragraphs using the given constant size.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * builder.setParagraphGapSize(Sizes.DLUY14);
   * builder.setParagraphGapSize(Sizes.dluY(22));
   * builder.setParagraphGapSize(Sizes.pixel(42));
   * </pre>
   * 
   * @param paragraphGapSize the <code>ConstantSize</code> that describes the
   *          size of the gaps between paragraphs
   */
  public void setParagraphGapSize(ConstantSize paragraphGapSize) {
    RowSpec rowSpec = RowSpec.createGap(paragraphGapSize);
    this.paragraphGapSpec = rowSpec;
  }

  /**
   * Returns the offset of the leading column, often 0 or 1.
   * 
   * @return the offset of the leading column
   */
  public int getLeadingColumnOffset() {
    return leadingColumnOffset;
  }

  /**
   * Sets the offset of the leading column, often 0 or 1.
   * 
   * @param columnOffset the new offset of the leading column
   */
  public void setLeadingColumnOffset(int columnOffset) {
    this.leadingColumnOffset = columnOffset;
  }

  /**
   * Returns whether new data rows are being grouped or not.
   * 
   * @return true indicates grouping enabled, false disabled
   */
  public boolean isRowGroupingEnabled() {
    return rowGroupingEnabled;
  }

  /**
   * Enables or disables the grouping of new data rows.
   * 
   * @param enabled indicates grouping enabled, false disabled
   */
  public void setRowGroupingEnabled(boolean enabled) {
    rowGroupingEnabled = enabled;
  }

  // Filling Columns ******************************************************

  /**
   * Adds a component to the panel using the default constraints with a column
   * span of 1. Then proceeds to the next data column.
   * 
   * @param widget the component to add
   */
  public void append(Widget widget) {
    append(widget, 1);
  }

  /**
   * Adds a component to the panel using the default constraints with the given
   * columnSpan. Proceeds to the next data column.
   * 
   * @param widget the component to append
   * @param columnSpan the column span used to add
   */
  public void append(Widget widget, int columnSpan) {
    ensureCursorColumnInGrid();
    ensureHasGapRow(lineGapSpec);
    ensureHasComponentLine();

    add(widget, createLeftAdjustedConstraints(columnSpan));
    nextColumn(columnSpan + 1);
  }

  /**
   * Adds two components to the panel; each component will span a single data
   * column. Proceeds to the next data column.
   * 
   * @param c1 the first component to add
   * @param c2 the second component to add
   */
  public void append(Widget c1, Widget c2) {
    append(c1);
    append(c2);
  }

  /**
   * Adds three components to the panel; each component will span a single data
   * column. Proceeds to the next data column.
   * 
   * @param c1 the first component to add
   * @param c2 the second component to add
   * @param c3 the third component to add
   */
  public void append(Widget c1, Widget c2, Widget c3) {
    append(c1);
    append(c2);
    append(c3);
  }

  // Appending Labels with optional components ------------------------------

  /**
   * Adds a text label to the panel and proceeds to the next column.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @return the added label
   */
  public Label append(String textWithMnemonic) {
    Label label = getComponentFactory().createLabel(textWithMnemonic);
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    append(label);
    return label;
  }

  /**
   * Adds a text label and component to the panel. Then proceeds to the next
   * data column.
   * <p>
   * 
   * The created label is labeling the given component; so the component gets
   * the focus if the (optional) label mnemonic is pressed.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @param component the component to add
   * @return the added label
   */
  public Label append(String textWithMnemonic, Widget component) {
    return append(textWithMnemonic, component, 1);
  }

  /**
   * Adds a text label and component to the panel; the component will span the
   * specified number columns. Proceeds to the next data column, and goes to the
   * next line if the boolean flag is set.
   * <p>
   * 
   * The created label is labeling the given component; so the component gets
   * the focus if the (optional) label mnemonic is pressed.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @param c the component to add
   * @param nextLine true forces a next line
   * @return the added label
   * @see JLabel#setLabelFor(java.awt.Component)
   */
  public Label append(String textWithMnemonic, Widget c, boolean nextLine) {
    Label label = append(textWithMnemonic, c);
    if (nextLine) {
      nextLine();
    }
    return label;
  }

  /**
   * Adds a text label and component to the panel; the component will span the
   * specified number columns. Proceeds to the next data column.
   * <p>
   * 
   * The created label is labeling the given component; so the component gets
   * the focus if the (optional) label mnemonic is pressed.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @param c the component to add
   * @param columnSpan number of columns the component shall span
   * @return the added label
   * @see Label#setLabelFor(java.awt.Component)
   */
  public Label append(String textWithMnemonic, Widget c, int columnSpan) {
    Label label = append(textWithMnemonic);
    String refId = c.getElement().getId();
    if (refId == null || refId.length() == 0) {
      refId = DOM.createUniqueId();
      c.getElement().setId(refId);
    }
    label.setLabelFor(refId);
    append(c, columnSpan);
    return label;
  }

  /**
   * Adds a text label and two components to the panel; each component will span
   * a single column. Proceeds to the next data column.
   * <p>
   * 
   * The created label is labeling the first component; so the component gets
   * the focus if the (optional) label mnemonic is pressed.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @param c1 the first component to add
   * @param c2 the second component to add
   * @return the added label
   */
  public Label append(String textWithMnemonic, Widget c1, Widget c2) {
    Label label = append(textWithMnemonic, c1);
    append(c2);
    return label;
  }

  /**
   * Adds a text label and two components to the panel; each component will span
   * a single column. Proceeds to the next data column.
   * <p>
   * 
   * The created label is labeling the first component; so the component gets
   * the focus if the (optional) label mnemonic is pressed.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @param c1 the first component to add
   * @param c2 the second component to add
   * @param colSpan the column span for the second component
   * @return the created label
   */
  public Label append(String textWithMnemonic, Widget c1, Widget c2, int colSpan) {
    Label label = append(textWithMnemonic, c1);
    append(c2, colSpan);
    return label;
  }

  /**
   * Adds a text label and three components to the panel; each component will
   * span a single column. Proceeds to the next data column.
   * <p>
   * 
   * The created label is labeling the first component; so the component gets
   * the focus if the (optional) label mnemonic is pressed.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @param c1 the first component to add
   * @param c2 the second component to add
   * @param c3 the third component to add
   * @return the added label
   */
  public Label append(String textWithMnemonic, Widget c1, Widget c2, Widget c3) {
    Label label = append(textWithMnemonic, c1, c2);
    append(c3);
    return label;
  }

  /**
   * Adds a text label and four components to the panel; each component will
   * span a single column. Proceeds to the next data column.
   * <p>
   * 
   * The created label is labeling the first component; so the component gets
   * the focus if the (optional) label mnemonic is pressed.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @param c1 the first component to add
   * @param c2 the second component to add
   * @param c3 the third component to add
   * @param c4 the fourth component to add
   * @return the added label
   */
  public Label append(String textWithMnemonic, Widget c1, Widget c2, Widget c3,
      Widget c4) {
    Label label = append(textWithMnemonic, c1, c2, c3);
    append(c4);
    return label;
  }

  // Adding Titles ----------------------------------------------------------

  /**
   * Adds a title label to the panel and proceeds to the next column.
   * 
   * @param textWithMnemonic the label's text - may mark a mnemonic
   * @return the added title label
   */
  // public Label appendTitle(String textWithMnemonic) {
  // Label titleLabel = getComponentFactory().createTitle(textWithMnemonic);
  // append(titleLabel);
  // return titleLabel;
  // }
  // Appending Separators ---------------------------------------------------
  /**
   * Adds a separator without text that spans all columns.
   * 
   * @return the added titled separator
   */
  public Widget appendSeparator() {
    return appendSeparator("");
  }

  /**
   * Adds a separator with the given text that spans all columns.
   * 
   * @param text the separator title text
   * @return the added titled separator
   */
  public Widget appendSeparator(String text) {
    ensureCursorColumnInGrid();
    ensureHasGapRow(paragraphGapSpec);
    ensureHasComponentLine();

    setColumn(super.getLeadingColumn());
    int columnSpan = getColumnCount();
    setColumnSpan(getColumnCount());
    Widget titledSeparator = addSeparator(text);
    setColumnSpan(1);
    nextColumn(columnSpan);
    return titledSeparator;
  }

  // Overriding Superclass Behavior ***************************************

  /**
   * Returns the leading column. Unlike the superclass this method honors the
   * column offset.
   * 
   * @return the leading column
   */
  protected int getLeadingColumn() {
    int column = super.getLeadingColumn();
    return column + getLeadingColumnOffset() * getColumnIncrementSign();
  }

  // Adding Rows **********************************************************

  /**
   * Ensures that the cursor is in the grid. In case it's beyond the form's
   * right hand side, the cursor is moved to the leading column of the next
   * line.
   */
  private void ensureCursorColumnInGrid() {
    if ((isLeftToRight() && (getColumn() > getColumnCount()))
        || (!isLeftToRight() && (getColumn() < 1))) {
      nextLine();
    }
  }

  /**
   * Ensures that we have a gap row before the next component row. Checks if the
   * current row is the given <code>RowSpec</code> and appends this row spec if
   * necessary.
   * 
   * @param gapRowSpec the row specification to check for
   */
  private void ensureHasGapRow(RowSpec gapRowSpec) {
    if ((getRow() == 1) || (getRow() <= getRowCount()))
      return;

    if (getRow() <= getRowCount()) {
      RowSpec rowSpec = getCursorRowSpec();
      if ((rowSpec == gapRowSpec))
        return;
    }
    appendRow(gapRowSpec);
    nextLine();
  }

  /**
   * Ensures that the form has a component row. Adds a component row if the
   * cursor is beyond the form's bottom.
   */
  private void ensureHasComponentLine() {
    if (getRow() <= getRowCount())
      return;
    appendRow(getDefaultRowSpec());
    if (isRowGroupingEnabled()) {
      getLayout().addGroupedRow(getRow());
    }
  }

  /**
   * Looks up and returns the row specification of the current row.
   * 
   * @return the row specification of the current row
   */
  private RowSpec getCursorRowSpec() {
    return getLayout().getRowSpec(getRow());
  }

}
