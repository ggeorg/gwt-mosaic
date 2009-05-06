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
package org.gwt.mosaic.forms.client.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Rectangle;
import org.gwt.mosaic.forms.client.util.FormUtils;
import org.gwt.mosaic.ui.client.layout.BaseLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * FormLayout is a powerful, flexible and precise general purpose layout
 * manager. It aligns components vertically and horizontally in a dynamic
 * rectangular grid of cells, with each component occupying one or more cells. A
 * <a href="../../../../../whitepaper.pdf" target="secondary">whitepaper</a>
 * about the FormLayout ships with the product documentation and is available <a
 * href="http://www.jgoodies.com/articles/forms.pdf">online</a>.
 * <p>
 * 
 * To use FormLayout you first define the grid by specifying the columns and
 * rows. In a second step you add components to the grid. You can specify
 * columns and rows via human-readable String descriptions or via arrays of
 * {@link ColumnSpec} and {@link RowSpec} instances.
 * <p>
 * 
 * Each component managed by a FormLayout is associated with an instance of
 * {@link CellConstraints}. The constraints object specifies where a component
 * should be located on the form's grid and how the component should be
 * positioned. In addition to its constraints object the <code>FormLayout</code>
 * also considers each component's minimum and preferred sizes in order to
 * determine a component's size.
 * <p>
 * 
 * FormLayout has been designed to work with non-visual builders that help you
 * specify the layout and fill the grid. For example, the
 * {@link com.jgoodies.forms.builder.ButtonBarBuilder2} assists you in building
 * button bars; it creates a standardized FormLayout and provides a minimal API
 * that specializes in adding buttons and Actions. Other builders can create
 * frequently used panel design, for example a form that consists of rows of
 * label-component pairs.
 * <p>
 * 
 * FormLayout has been prepared to work with different types of sizes as defined
 * by the {@link Size} interface.
 * <p>
 * 
 * <strong>Example 1</strong> (Plain FormLayout):<br>
 * The following example creates a panel with 3 data columns and 3 data rows;
 * the columns and rows are specified before components are added to the form.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;, // columns
 *     &quot;pref, 3dlu, pref, 3dlu, pref&quot;); // rows
 * CellConstraints cc = new CellConstraints();
 * JPanel panel = new JPanel(layout);
 * panel.add(new JLabel(&quot;Label1&quot;), cc.xy(1, 1));
 * panel.add(new JTextField(), cc.xywh(3, 1, 3, 1));
 * panel.add(new JLabel(&quot;Label2&quot;), cc.xy(1, 3));
 * panel.add(new JTextField(), cc.xy(3, 3));
 * panel.add(new JLabel(&quot;Label3&quot;), cc.xy(1, 5));
 * panel.add(new JTextField(), cc.xy(3, 5));
 * panel.add(new JButton(&quot;/u2026&quot;), cc.xy(5, 5));
 * return panel;
 * </pre>
 * <p>
 * 
 * <strong>Example 2</strong> (Using PanelBuilder):<br>
 * This example creates the same panel as above using the
 * {@link org.gwt.mosaic.ui.client.layout.builder.jgoodies.forms.builder.PanelBuilder}
 * to add components to the form.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;, // columns
 *     &quot;pref, 3dlu, pref, 3dlu, pref&quot;); // rows
 * PanelBuilder builder = new PanelBuilder(layout);
 * CellConstraints cc = new CellConstraints();
 * builder.addLabel(&quot;Label1&quot;, cc.xy(1, 1));
 * builder.add(new JTextField(), cc.xywh(3, 1, 3, 1));
 * builder.addLabel(&quot;Label2&quot;, cc.xy(1, 3));
 * builder.add(new JTextField(), cc.xy(3, 3));
 * builder.addLabel(&quot;Label3&quot;, cc.xy(1, 5));
 * builder.add(new JTextField(), cc.xy(3, 5));
 * builder.add(new JButton(&quot;/u2026&quot;), cc.xy(5, 5));
 * return builder.getPanel();
 * </pre>
 * <p>
 * 
 * <strong>Example 3</strong> (Using DefaultFormBuilder):<br>
 * This example utilizes the
 * {@link com.jgoodies.forms.builder.DefaultFormBuilder} that ships with the
 * source distribution.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;); // 5 columns; add rows later
 * DefaultFormBuilder builder = new DefaultFormBuilder(layout);
 * builder.append(&quot;Label1&quot;, new JTextField(), 3);
 * builder.append(&quot;Label2&quot;, new JTextField());
 * builder.append(&quot;Label3&quot;, new JTextField());
 * builder.append(new JButton(&quot;/u2026&quot;));
 * return builder.getPanel();
 * </pre>
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see ColumnSpec
 * @see RowSpec
 * @see CellConstraints
 * @see org.gwt.mosaic.ui.client.layout.builder.jgoodies.forms.builder.AbstractFormBuilder
 * @see com.jgoodies.forms.builder.ButtonBarBuilder
 * @see com.jgoodies.forms.builder.DefaultFormBuilder
 * @see com.jgoodies.forms.factories.FormFactory
 * @see Size
 * @see Sizes
 */
public final class FormLayout extends BaseLayout implements Serializable {
  private static final long serialVersionUID = 7911952585430389626L;

  // Instance Fields ********************************************************

  /**
   * Holds the column specifications.
   * 
   * @see ColumnSpec
   * @see #getColumnCount()
   * @see #getColumnSpec(int)
   * @see #appendColumn(ColumnSpec)
   * @see #insertColumn(int, ColumnSpec)
   * @see #removeColumn(int)
   */
  private final List<ColumnSpec> colSpecs;

  /**
   * Holds the row specifications.
   * 
   * @see RowSpec
   * @see #getRowCount()
   * @see #getRowSpec(int)
   * @see #appendRow(RowSpec)
   * @see #insertRow(int, RowSpec)
   * @see #removeRow(int)
   */
  private final List<RowSpec> rowSpecs;

  /**
   * Holds the column groups as an array of arrays of column indices.
   * 
   * @see #getColumnGroups()
   * @see #setColumnGroups(int[][])
   * @see #addGroupedColumn(int)
   */
  private int[][] colGroupIndices;

  /**
   * Holds the row groups as an array of arrays of row indices.
   * 
   * @see #getRowGroups()
   * @see #setRowGroups(int[][])
   * @see #addGroupedRow(int)
   */
  private int[][] rowGroupIndices;

  /**
   * Maps components to their associated <code>CellConstraints</code>.
   * 
   * @see CellConstraints
   * @see #getConstraints(Component)
   * @see #setConstraints(Component, CellConstraints)
   */
  private final Map<Widget, CellConstraints> constraintMap;

  private boolean honorsVisibility = true;

  // Fields used by the Layout Algorithm ************************************

  /**
   * Holds the widgets that occupy exactly one column. For each column we keep a
   * list of these widgets.
   */
  private transient List<Widget>[] colWidgets;

  /**
   * Holds the widgets that occupy exactly one row. For each row we keep a list
   * of these widgets.
   */
  private transient List<Widget>[] rowWidgets;

  /**
   * Caches component minimum and preferred sizes. All requests for component
   * sizes shall be directed to the cache.
   */
  private final ComponentSizeCache componentSizeCache;

  /**
   * These functional objects are used to measure component sizes. They abstract
   * from horizontal and vertical orientation and so, allow to implement the
   * layout algorithm for both orientations with a single set of methods.
   */
  private final Measure minimumWidthMeasure;
  private final Measure minimumHeightMeasure;
  private final Measure preferredWidthMeasure;
  private final Measure preferredHeightMeasure;

  // Instance Creation ****************************************************

  /**
   * Constructs an empty {@code FormLayout}.
   * <p>
   * This constructor is intended to be used in environments that add columns
   * and rows dynamically.
   */
  public FormLayout() {
    this(new ColumnSpec[0], new RowSpec[0]);
  }

  /**
   * Constructs a {@code FormLayout} using the given encoded column
   * specifications. The constructed layout has no rows. The string decoding
   * uses the default LayoutMap.
   * <p>
   * This constructor is intended to be used with builder classes that add rows
   * dynamically, such as the {@link DefaultFormBuilder}.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * // Label, gap, widget
   * FormLayout layout = new FormLayout(&quot;pref, 4dlu, pref&quot;);
   * // Right-aligned label, gap, widget, gap, widget
   * FormLayout layout = new FormLayout(&quot;right:pref, 4dlu, 50dlu, 4dlu, 50dlu&quot;);
   * // Left-aligned labels, gap, widgets, gap, widgets
   * FormLayout layout = new FormLayout(&quot;left:pref, 4dlu, pref, 4dlu, pref&quot;);
   * </pre>
   * 
   * See the class comment for more examples.
   * 
   * @param encodedColumnSpecs comma separated encoded column specifications
   * 
   * @throws NullPointerException if encodedColumnSpecs is {@code null}
   * 
   * @see LayoutMap#getRoot()
   */
  public FormLayout(String encodedColumnSpecs) {
    this(encodedColumnSpecs, LayoutMap.getRoot());
  }

  /**
   * Constructs a {@code FormLayout} using the given encoded column
   * specifications and LayoutMap. The constructed layout has no rows.
   * <p>
   * This constructor is intended to be used with builder classes that add rows
   * dynamically, such as the {@link DefaultFormBuilder}.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * // Label, gap, component
   * FormLayout layout = new FormLayout(&quot;pref, 4dlu, pref&quot;, myLayoutMap);
   * // Right-aligned label, gap, component, gap, component
   * FormLayout layout = new FormLayout(&quot;right:pref, @lcgap, 50dlu, 4dlu, 50dlu&quot;,
   *     myLayoutMap);
   * // Left-aligned labels, gap, components, gap, components
   * FormLayout layout = new FormLayout(&quot;left:pref, @lcgap, pref, @myGap, pref&quot;,
   *     myLayoutMap);
   * </pre>
   * 
   * See the class comment for more examples.
   * 
   * @param encodedColumnSpecs comma separated encoded column specifications
   * @param layoutMap expands layout column and row variables
   * 
   * @throws NullPointerException if {@code encodedColumnSpecs} or {@code
   *           layoutMap} is {@code null}
   * 
   * @see LayoutMap#getRoot()
   */
  public FormLayout(String encodedColumnSpecs, LayoutMap layoutMap) {
    this(ColumnSpec.decodeSpecs(encodedColumnSpecs, layoutMap), new RowSpec[0]);
  }

  /**
   * Constructs a {@code FormLayout} using the given encoded column and row
   * specifications and the default LayoutMap.
   * <p>
   * This constructor is recommended for most hand-coded layouts.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * FormLayout layout = new FormLayout(&quot;pref, 4dlu, pref&quot;, // columns
   *     &quot;p, 3dlu, p&quot;); // rows
   * FormLayout layout = new FormLayout(&quot;right:pref, 4dlu, pref&quot;, // columns
   *     &quot;p, 3dlu, p, 3dlu, fill:p:grow&quot;); // rows
   * FormLayout layout = new FormLayout(&quot;left:pref, 4dlu, 50dlu&quot;, // columns
   *     &quot;p, 2px, p, 3dlu, p, 9dlu, p&quot;); // rows
   * FormLayout layout = new FormLayout(&quot;max(75dlu;pref), 4dlu, default&quot;, // columns
   *     &quot;p, 3dlu, p, 3dlu, p, 3dlu, p&quot;); // rows
   * </pre>
   * 
   * See the class comment for more examples.
   * 
   * @param encodedColumnSpecs comma separated encoded column specifications
   * @param encodedRowSpecs comma separated encoded row specifications
   * 
   * @throws NullPointerException if encodedColumnSpecs or encodedRowSpecs is
   *           {@code null}
   * 
   * @see LayoutMap#getRoot()
   */
  public FormLayout(String encodedColumnSpecs, String encodedRowSpecs) {
    this(encodedColumnSpecs, encodedRowSpecs, LayoutMap.getRoot());
  }

  /**
   * Constructs a FormLayout using the given encoded column and row
   * specifications and the given LayoutMap.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * FormLayout layout = new FormLayout(&quot;pref, 4dlu, pref&quot;, // columns
   *     &quot;p, 3dlu, p&quot;, // rows
   *     myLayoutMap); // custom LayoutMap
   * FormLayout layout = new FormLayout(&quot;right:pref, 4dlu, pref&quot;, // columns
   *     &quot;p, @lgap, p, @lgap, fill:p:grow&quot;,// rows
   *     myLayoutMap); // custom LayoutMap
   * FormLayout layout = new FormLayout(&quot;left:pref, 4dlu, 50dlu&quot;, // columns
   *     &quot;p, 2px, p, 3dlu, p, 9dlu, p&quot;, // rows
   *     myLayoutMap); // custom LayoutMap
   * FormLayout layout = new FormLayout(&quot;max(75dlu;pref), 4dlu, default&quot;, // columns
   *     &quot;p, 3dlu, p, 3dlu, p, 3dlu, p&quot;, // rows
   *     myLayoutMap); // custom LayoutMap
   * </pre>
   * 
   * See the class comment for more examples.
   * 
   * @param encodedColumnSpecs comma separated encoded column specifications
   * @param encodedRowSpecs comma separated encoded row specifications
   * @param layoutMap expands layout column and row variables
   * 
   * @throws NullPointerException if {@code encodedColumnSpecs}, {@code
   *           encodedRowSpecs}, or {@code layoutMap} is {@code null}
   */
  public FormLayout(String encodedColumnSpecs, String encodedRowSpecs,
      LayoutMap layoutMap) {
    this(ColumnSpec.decodeSpecs(encodedColumnSpecs, layoutMap),
        RowSpec.decodeSpecs(encodedRowSpecs, layoutMap));
  }

  /**
   * Constructs a FormLayout using the given column specifications. The
   * constructed layout has no rows; these must be added before components can
   * be added to the layout container.
   * 
   * @param colSpecs an array of column specifications.
   * @throws NullPointerException if {@code colSpecs} is {@code null}
   */
  public FormLayout(ColumnSpec[] colSpecs) {
    this(colSpecs, new RowSpec[] {});
  }

  /**
   * Constructs a FormLayout using the given column and row specifications.
   * 
   * @param colSpecs an array of column specifications.
   * @param rowSpecs an array of row specifications.
   * @throws NullPointerException if colSpecs or rowSpecs is {@code null}
   */
  public FormLayout(ColumnSpec[] colSpecs, RowSpec[] rowSpecs) {
    if (colSpecs == null)
      throw new NullPointerException(
          "The column specifications must not be null.");
    if (rowSpecs == null)
      throw new NullPointerException("The row specifications must not be null.");

    this.colSpecs = new ArrayList<ColumnSpec>(Arrays.asList(colSpecs));
    this.rowSpecs = new ArrayList<RowSpec>(Arrays.asList(rowSpecs));
    colGroupIndices = new int[][] {};
    rowGroupIndices = new int[][] {};
    int initialCapacity = colSpecs.length * rowSpecs.length / 4;
    constraintMap = new HashMap<Widget, CellConstraints>(initialCapacity);
    componentSizeCache = new ComponentSizeCache(initialCapacity);
    minimumWidthMeasure = new MinimumWidthMeasure(componentSizeCache);
    minimumHeightMeasure = new MinimumHeightMeasure(componentSizeCache);
    preferredWidthMeasure = new PreferredWidthMeasure(componentSizeCache);
    preferredHeightMeasure = new PreferredHeightMeasure(componentSizeCache);
  }

  // Accessing the Column and Row Specifications **************************

  /**
   * Returns the number of columns in this layout.
   * 
   * @return the number of columns
   */
  public int getColumnCount() {
    return colSpecs.size();
  }

  /**
   * Returns the number of rows in this layout.
   * 
   * @return the number of rows
   */
  public int getRowCount() {
    return rowSpecs.size();
  }

  /**
   * Returns the <code>ColumnSpec</code> at the specified column index.
   * 
   * @param columnIndex the column index of the requested
   *          <code>ColumnSpec</code>
   * @return the <code>ColumnSpec</code> at the specified column
   * @throws IndexOutOfBoundsException if the column index is out of range
   */
  public ColumnSpec getColumnSpec(int columnIndex) {
    return (ColumnSpec) colSpecs.get(columnIndex - 1);
  }

  /**
   * Sets the <code>ColumnSpec</code> at the specified column index.
   * 
   * @param columnIndex the index of the column to be changed
   * @param columnSpec the <code>ColumnSpec</code> to be set
   * @throws NullPointerException if the column specification is null
   * @throws IndexOutOfBoundsException if the column index is out of range
   */
  public void setColumnSpec(int columnIndex, ColumnSpec columnSpec) {
    if (columnSpec == null) {
      throw new NullPointerException("The column spec must not be null.");
    }
    colSpecs.set(columnIndex - 1, columnSpec);
  }

  /**
   * Returns the <code>RowSpec</code> at the specified row index.
   * 
   * @param rowIndex the row index of the requested <code>RowSpec</code>
   * @return the <code>RowSpec</code> at the specified row
   * @throws IndexOutOfBoundsException if the row index is out of range
   */
  public RowSpec getRowSpec(int rowIndex) {
    return (RowSpec) rowSpecs.get(rowIndex - 1);
  }

  /**
   * Sets the <code>RowSpec</code> at the specified row index.
   * 
   * @param rowIndex the index of the row to be changed
   * @param rowSpec the <code>RowSpec</code> to be set
   * @throws NullPointerException if the row specification is null
   * @throws IndexOutOfBoundsException if the row index is out of range
   */
  public void setRowSpec(int rowIndex, RowSpec rowSpec) {
    if (rowSpec == null) {
      throw new NullPointerException("The row spec must not be null.");
    }
    rowSpecs.set(rowIndex - 1, rowSpec);
  }

  /**
   * Appends the given column specification to the right hand side of all
   * columns.
   * 
   * @param columnSpec the column specification to be added
   * @throws NullPointerException if the column specification is null
   */
  public void appendColumn(ColumnSpec columnSpec) {
    if (columnSpec == null) {
      throw new NullPointerException("The column spec must not be null.");
    }
    colSpecs.add(columnSpec);
  }

  /**
   * Inserts the specified column at the specified position. Shifts components
   * that intersect the new column to the right hand side and readjusts column
   * groups.
   * <p>
   * 
   * The component shift works as follows: components that were located on the
   * right hand side of the inserted column are shifted one column to the right;
   * component column span is increased by one if it intersects the new column.
   * <p>
   * 
   * Column group indices that are greater or equal than the given column index
   * will be increased by one.
   * 
   * @param columnIndex index of the column to be inserted
   * @param columnSpec specification of the column to be inserted
   * @throws IndexOutOfBoundsException if the column index is out of range
   */
  public void insertColumn(int columnIndex, ColumnSpec columnSpec) {
    if (columnIndex < 1 || columnIndex > getColumnCount()) {
      throw new IndexOutOfBoundsException("The column index " + columnIndex
          + "must be in the range [1, " + getColumnCount() + "].");
    }
    colSpecs.add(columnIndex - 1, columnSpec);
    shiftComponentsHorizontally(columnIndex, false);
    adjustGroupIndices(colGroupIndices, columnIndex, false);
  }

  /**
   * Removes the column with the given column index from the layout. Components
   * will be rearranged and column groups will be readjusted. Therefore, the
   * column must not contain components and must not be part of a column group.
   * <p>
   * 
   * The component shift works as follows: components that were located on the
   * right hand side of the removed column are moved one column to the left;
   * component column span is decreased by one if it intersects the removed
   * column.
   * <p>
   * 
   * Column group indices that are greater than the column index will be
   * decreased by one.
   * <p>
   * 
   * <strong>Note:</strong> If one of the constraints mentioned above is
   * violated, this layout's state becomes illegal and it is unsafe to work with
   * this layout. A typical layout implementation can ensure that these
   * constraints are not violated. However, in some cases you may need to check
   * these conditions before you invoke this method. The Forms extras contain
   * source code for class <code>FormLayoutUtils</code> that provides the
   * required test methods:<br>
   * <code>#columnContainsComponents(Container, int)</code> and<br>
   * <code>#isGroupedColumn(FormLayout, int)</code>.
   * 
   * @param columnIndex index of the column to remove
   * @throws IndexOutOfBoundsException if the column index is out of range
   * @throws IllegalStateException if the column contains components or if the
   *           column is already grouped
   * 
   * @see com.jgoodies.forms.extras.FormLayoutUtils#columnContainsComponent(Container,
   *      int)
   * @see com.jgoodies.forms.extras.FormLayoutUtils#isGroupedColumn(FormLayout,
   *      int)
   */
  public void removeColumn(int columnIndex) {
    if (columnIndex < 1 || columnIndex > getColumnCount()) {
      throw new IndexOutOfBoundsException("The column index " + columnIndex
          + " must be in the range [1, " + getColumnCount() + "].");
    }
    colSpecs.remove(columnIndex - 1);
    shiftComponentsHorizontally(columnIndex, true);
    adjustGroupIndices(colGroupIndices, columnIndex, true);
  }

  /**
   * Appends the given row specification to the bottom of all rows.
   * 
   * @param rowSpec the row specification to be added to the form layout
   * @throws NullPointerException if the rowSpec is null
   */
  public void appendRow(RowSpec rowSpec) {
    if (rowSpec == null) {
      throw new NullPointerException("The row spec must not be null.");
    }
    rowSpecs.add(rowSpec);
  }

  /**
   * Inserts the specified column at the specified position. Shifts components
   * that intersect the new column to the right and readjusts column groups.
   * <p>
   * 
   * The component shift works as follows: components that were located on the
   * right hand side of the inserted column are shifted one column to the right;
   * component column span is increased by one if it intersects the new column.
   * <p>
   * 
   * Column group indices that are greater or equal than the given column index
   * will be increased by one.
   * 
   * @param rowIndex index of the row to be inserted
   * @param rowSpec specification of the row to be inserted
   * @throws IndexOutOfBoundsException if the row index is out of range
   */
  public void insertRow(int rowIndex, RowSpec rowSpec) {
    if (rowIndex < 1 || rowIndex > getRowCount()) {
      throw new IndexOutOfBoundsException("The row index " + rowIndex
          + " must be in the range [1, " + getRowCount() + "].");
    }
    rowSpecs.add(rowIndex - 1, rowSpec);
    shiftComponentsVertically(rowIndex, false);
    adjustGroupIndices(rowGroupIndices, rowIndex, false);
  }

  /**
   * Removes the row with the given row index from the layout. Components will
   * be rearranged and row groups will be readjusted. Therefore, the row must
   * not contain components and must not be part of a row group.
   * <p>
   * 
   * The component shift works as follows: components that were located below
   * the removed row are moved up one row; component row span is decreased by
   * one if it intersects the removed row.
   * <p>
   * 
   * Row group indices that are greater than the row index will be decreased by
   * one.
   * <p>
   * 
   * <strong>Note:</strong> If one of the constraints mentioned above is
   * violated, this layout's state becomes illegal and it is unsafe to work with
   * this layout. A typical layout implementation can ensure that these
   * constraints are not violated. However, in some cases you may need to check
   * these conditions before you invoke this method. The Forms extras contain
   * source code for class <code>FormLayoutUtils</code> that provides the
   * required test methods:<br>
   * <code>#rowContainsComponents(Container, int)</code> and<br>
   * <code>#isGroupedRow(FormLayout, int)</code>.
   * 
   * @param rowIndex index of the row to remove
   * @throws IndexOutOfBoundsException if the row index is out of range
   * @throws IllegalStateException if the row contains components or if the row
   *           is already grouped
   * 
   * @see com.jgoodies.forms.extras.FormLayoutUtils#rowContainsComponent(Container,
   *      int)
   * @see com.jgoodies.forms.extras.FormLayoutUtils#isGroupedRow(FormLayout,
   *      int)
   */
  public void removeRow(int rowIndex) {
    if (rowIndex < 1 || rowIndex > getRowCount()) {
      throw new IndexOutOfBoundsException("The row index " + rowIndex
          + "must be in the range [1, " + getRowCount() + "].");
    }
    rowSpecs.remove(rowIndex - 1);
    shiftComponentsVertically(rowIndex, true);
    adjustGroupIndices(rowGroupIndices, rowIndex, true);
  }

  /**
   * Shifts components horizontally, either to the right if a column has been
   * inserted or to the left if a column has been removed.
   * 
   * @param columnIndex index of the column to remove
   * @param remove true for remove, false for insert
   * @throws IllegalStateException if a removed column contains components
   */
  private void shiftComponentsHorizontally(int columnIndex, boolean remove) {
    final int offset = remove ? -1 : 1;
    for (Iterator i = constraintMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      CellConstraints constraints = (CellConstraints) entry.getValue();
      int x1 = constraints.gridX;
      int w = constraints.gridWidth;
      int x2 = x1 + w - 1;
      if (x1 == columnIndex && remove) {
        throw new IllegalStateException("The removed column " + columnIndex
            + " must not contain component origins.\n" + "Illegal component="
            + entry.getKey());
      } else if (x1 >= columnIndex) {
        constraints.gridX += offset;
      } else if (x2 >= columnIndex) {
        constraints.gridWidth += offset;
      }
    }
  }

  /**
   * Shifts components vertically, either to the bottom if a row has been
   * inserted or to the top if a row has been removed.
   * 
   * @param rowIndex index of the row to remove
   * @param remove true for remove, false for insert
   * @throws IllegalStateException if a removed column contains components
   */
  private void shiftComponentsVertically(int rowIndex, boolean remove) {
    final int offset = remove ? -1 : 1;
    for (Iterator i = constraintMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      CellConstraints constraints = (CellConstraints) entry.getValue();
      int y1 = constraints.gridY;
      int h = constraints.gridHeight;
      int y2 = y1 + h - 1;
      if (y1 == rowIndex && remove) {
        throw new IllegalStateException("The removed row " + rowIndex
            + " must not contain component origins.\n" + "Illegal component="
            + entry.getKey());
      } else if (y1 >= rowIndex) {
        constraints.gridY += offset;
      } else if (y2 >= rowIndex) {
        constraints.gridHeight += offset;
      }
    }
  }

  /**
   * Adjusts group indices. Shifts the given groups to left, right, up, down
   * according to the specified remove or add flag.
   * 
   * @param allGroupIndices the groups to be adjusted
   * @param modifiedIndex the modified column or row index
   * @param remove true for remove, false for add
   * @throws IllegalStateException if we remove and the index is grouped
   */
  private void adjustGroupIndices(int[][] allGroupIndices, int modifiedIndex,
      boolean remove) {
    final int offset = remove ? -1 : +1;
    for (int group = 0; group < allGroupIndices.length; group++) {
      int[] groupIndices = allGroupIndices[group];
      for (int i = 0; i < groupIndices.length; i++) {
        int index = groupIndices[i];
        if (index == modifiedIndex && remove) {
          throw new IllegalStateException("The removed index " + modifiedIndex
              + " must not be grouped.");
        } else if (index >= modifiedIndex) {
          groupIndices[i] += offset;
        }
      }
    }
  }

  // Accessing Constraints ************************************************

  /**
   * Looks up and returns the constraints for the specified component. TODO A
   * copy of the actual <code>CellConstraints</code> object is returned.
   * 
   * @param widget the component to be queried
   * @return the <code>CellConstraints</code> for the specified component
   * @throws NullPointerException if component is <code>null</code> or has not
   *           been added to the container
   */
  public CellConstraints getConstraints(Widget widget) {
    return (CellConstraints) getConstraints0(widget).clone();
  }

  private CellConstraints getConstraints0(Widget widget) {
    if (widget == null)
      throw new NullPointerException("The widget must not be null.");

    CellConstraints constraints = constraintMap.get(widget);
    if (constraints == null)
      throw new NullPointerException(
          "The component has not been added to the container.");

    return constraints;
  }

  /**
   * Sets the constraints for the specified component in this layout.
   * 
   * @param widget the component to be modified
   * @param constraints the constraints to be applied
   * @throws NullPointerException if the component or constraints object is
   *           <code>null</code>
   */
  public void setConstraints(Widget widget, CellConstraints constraints) {
    if (widget == null)
      throw new NullPointerException("The widget must not be null.");
    if (constraints == null)
      throw new NullPointerException("The constraints must not be null.");

    constraints.ensureValidGridBounds(getColumnCount(), getRowCount());
    constraintMap.put(widget, (CellConstraints) constraints.clone());
  }

  /**
   * Removes the constraints for the specified component in this layout.
   * 
   * @param widget the component to be modified
   */
  private void removeConstraints(Widget widget) {
    constraintMap.remove(widget);
    componentSizeCache.removeEntry(widget);
  }

  // Accessing Column and Row Groups **************************************

  /**
   * Returns a deep copy of the column groups.
   * 
   * @return the column groups as two-dimensional int array
   */
  public int[][] getColumnGroups() {
    return deepClone(colGroupIndices);
  }

  /**
   * Sets the column groups, where each column in a group gets the same group
   * wide width. Each group is described by an array of integers that are
   * interpreted as column indices. The parameter is an array of such group
   * descriptions.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * // Group columns 1, 3 and 4.
   * setColumnGroups(new int[][] {{1, 3, 4}});
   * // Group columns 1, 3, 4, and group columns 7 and 9
   * setColumnGroups(new int[][] { {1, 3, 4}, {7, 9}});
   * </pre>
   * 
   * @param colGroupIndices a two-dimensional array of column groups indices
   * @throws IndexOutOfBoundsException if an index is outside the grid
   * @throws IllegalArgumentException if a column index is used twice
   */
  public void setColumnGroups(int[][] colGroupIndices) {
    int maxColumn = getColumnCount();
    boolean[] usedIndices = new boolean[maxColumn + 1];
    for (int group = 0; group < colGroupIndices.length; group++) {
      for (int j = 0; j < colGroupIndices[group].length; j++) {
        int colIndex = colGroupIndices[group][j];
        if (colIndex < 1 || colIndex > maxColumn) {
          throw new IndexOutOfBoundsException("Invalid column group index "
              + colIndex + " in group " + (group + 1));
        }
        if (usedIndices[colIndex]) {
          throw new IllegalArgumentException("Column index " + colIndex
              + " must not be used in multiple column groups.");
        }
        usedIndices[colIndex] = true;
      }
    }
    this.colGroupIndices = deepClone(colGroupIndices);
  }

  /**
   * Adds the specified column index to the last column group. In case there are
   * no groups, a new group will be created.
   * 
   * @param columnIndex the column index to be set grouped
   */
  public void addGroupedColumn(int columnIndex) {
    int[][] newColGroups = getColumnGroups();
    // Create a group if none exists.
    if (newColGroups.length == 0) {
      newColGroups = new int[][] {{columnIndex}};
    } else {
      int lastGroupIndex = newColGroups.length - 1;
      int[] lastGroup = newColGroups[lastGroupIndex];
      int groupSize = lastGroup.length;
      int[] newLastGroup = new int[groupSize + 1];
      System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
      newLastGroup[groupSize] = columnIndex;
      newColGroups[lastGroupIndex] = newLastGroup;
    }
    setColumnGroups(newColGroups);
  }

  /**
   * Returns a deep copy of the row groups.
   * 
   * @return the row groups as two-dimensional int array
   */
  public int[][] getRowGroups() {
    return deepClone(rowGroupIndices);
  }

  /**
   * Sets the row groups, where each row in such a group gets the same group
   * wide height. Each group is described by an array of integers that are
   * interpreted as row indices. The parameter is an array of such group
   * descriptions.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * // Group rows 1 and 2.
   * setRowGroups(new int[][] {{1, 2}});
   * // Group rows 1 and 2, and group rows 5, 7, and 9.
   * setRowGroups(new int[][] { {1, 2}, {5, 7, 9}});
   * </pre>
   * 
   * @param rowGroupIndices a two-dimensional array of row group indices.
   * @throws IndexOutOfBoundsException if an index is outside the grid
   */
  public void setRowGroups(int[][] rowGroupIndices) {
    int rowCount = getRowCount();
    boolean[] usedIndices = new boolean[rowCount + 1];
    for (int i = 0; i < rowGroupIndices.length; i++) {
      for (int j = 0; j < rowGroupIndices[i].length; j++) {
        int rowIndex = rowGroupIndices[i][j];
        if (rowIndex < 1 || rowIndex > rowCount) {
          throw new IndexOutOfBoundsException("Invalid row group index "
              + rowIndex + " in group " + (i + 1));
        }
        if (usedIndices[rowIndex]) {
          throw new IllegalArgumentException("Row index " + rowIndex
              + " must not be used in multiple row groups.");
        }
        usedIndices[rowIndex] = true;
      }
    }
    this.rowGroupIndices = deepClone(rowGroupIndices);
  }

  /**
   * Adds the specified row index to the last row group. In case there are no
   * groups, a new group will be created.
   * 
   * @param rowIndex the index of the row that should be grouped
   */
  public void addGroupedRow(int rowIndex) {
    int[][] newRowGroups = getRowGroups();
    // Create a group if none exists.
    if (newRowGroups.length == 0) {
      newRowGroups = new int[][] {{rowIndex}};
    } else {
      int lastGroupIndex = newRowGroups.length - 1;
      int[] lastGroup = newRowGroups[lastGroupIndex];
      int groupSize = lastGroup.length;
      int[] newLastGroup = new int[groupSize + 1];
      System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
      newLastGroup[groupSize] = rowIndex;
      newRowGroups[lastGroupIndex] = newLastGroup;
    }
    setRowGroups(newRowGroups);
  }

  // Other Accessors ********************************************************

  /**
   * Returns whether invisible components shall be taken into account by this
   * layout. This container-wide setting can be overridden per component. See
   * {@link #setHonorsVisibility(boolean)} for details.
   * 
   * @return <code>true</code> if the component visibility is honored by this
   *         FormLayout, <code>false</code> if it is ignored. This setting can
   *         be overridden by individual CellConstraints using
   *         {@link #setHonorsVisibility(Component, Boolean)}.
   * 
   * @since 1.2
   */
  public boolean getHonorsVisibility() {
    return honorsVisibility;
  }

  /**
   * Specifies whether invisible components shall be taken into account by this
   * layout for computing the layout size and setting component bounds. If set
   * to <code>true</code> invisible components will be ignored by the layout. If
   * set to <code>false</code> components will be taken into account regardless
   * of their visibility. Visible components are always used for sizing and
   * positioning.
   * <p>
   * 
   * The default value for this setting is <code>true</code>. It is useful to
   * set the value to <code>false</code> (in other words to ignore the
   * visibility) if you switch the component visibility dynamically and want the
   * container to retain the size and component positions.
   * <p>
   * 
   * This container-wide default setting can be overridden per component using
   * {@link #setHonorsVisibility(Component, Boolean)}.
   * <p>
   * 
   * Components are taken into account, if
   * <ol>
   * <li>they are visible, or
   * <li>they have no individual setting and the container-wide settings ignores
   * the visibility (honorsVisibility set to <code>false</code>), or
   * <li>the individual component ignores the visibility.
   * </ol>
   * 
   * @param b <code>true</code> to honor the visibility, i.e. to exclude
   *          invisible components from the sizing and positioning,
   *          <code>false</code> to ignore the visibility, in other words to
   *          layout visible and invisible components
   */
  public void setHonorsVisibility(boolean b) {
    boolean oldHonorsVisibility = getHonorsVisibility();
    if (oldHonorsVisibility == b)
      return;
    honorsVisibility = b;
    Set componentSet = constraintMap.keySet();
    if (componentSet.isEmpty())
      return;
    Widget firstComponent = (Widget) componentSet.iterator().next();
    Widget container = firstComponent.getParent();
    // XXX invalidateAndRepaint(container);
  }

  /**
   * Specifies whether the given component shall be taken into account for
   * sizing and positioning. This setting overrides the container-wide default.
   * See {@link #setHonorsVisibility(boolean)} for details.
   * 
   * @param widget the component that shall get an individual setting
   * @param b <code>Boolean.TRUE</code> to override the container default and
   *          honor the visibility for the given component,
   *          <code>Boolean.FALSE</code> to override the container default and
   *          ignore the visibility for the given component, <code>null</code>
   *          to use the container default value as specified by
   *          {@link #getHonorsVisibility()}.
   * 
   * @since 1.2
   */
  public void setHonorsVisibility(Widget widget, Boolean b) {
    CellConstraints constraints = getConstraints0(widget);
    if (FormUtils.equals(b, constraints.honorsVisibility))
      return;
    constraints.honorsVisibility = b;
    // XXX invalidateAndRepaint(widget.getParent());
  }

  // Implementing the LayoutManager and LayoutManager2 Interfaces *********

  /**
   * Adds the specified component to the layout, using the specified
   * <code>constraints</code> object. Note that constraints are mutable and are,
   * therefore, cloned when cached.
   * 
   * @param comp the component to be added
   * @param constraints the component's cell constraints
   * @throws NullPointerException if <code>constraints</code> is
   *           <code>null</code>
   * @throws IllegalArgumentException if <code>constraints</code> is not a
   *           <code>CellConstraints</code> or a String that cannot be used to
   *           construct a <code>CellConstraints</code>
   */
  public void addLayoutComponent(Widget comp, Object constraints) {
    if (constraints instanceof String) {
      setConstraints(comp, new CellConstraints((String) constraints));
    } else if (constraints instanceof CellConstraints) {
      setConstraints(comp, (CellConstraints) constraints);
    } else if (constraints == null) {
      throw new NullPointerException("The constraints must not be null.");
    } else {
      throw new IllegalArgumentException("Illegal constraint type "
          + constraints.getClass());
    }
  }

  /**
   * Removes the specified component from this layout.
   * <p>
   * 
   * Most applications do not call this method directly.
   * 
   * @param comp the component to be removed.
   * @see Container#remove(java.awt.Component)
   * @see Container#removeAll()
   */
  public void removeLayoutComponent(Widget comp) {
    removeConstraints(comp);
  }

  // Layout Requests ******************************************************

  /**
   * Determines the minimum size of the <code>parent</code> container using this
   * form layout.
   * <p>
   * 
   * Most applications do not call this method directly.
   * 
   * @param layoutPanel the container in which to do the layout
   * @return the minimum size of the <code>parent</code> container
   * 
   * @see Container#doLayout()
   */
  public Dimension minimumLayoutSize(LayoutPanel layoutPanel) {
    return computeLayoutSize(layoutPanel, minimumWidthMeasure,
        minimumHeightMeasure);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.
   * mosaic.ui.client.layout.LayoutPanel)
   */
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    try {
      if (!(layoutPanel == null || !init(layoutPanel))) {
        final Dimension d = computeLayoutSize(layoutPanel,
            preferredWidthMeasure, preferredHeightMeasure);
        return d;
      }
    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".getPreferredSize(): "
          + e.getLocalizedMessage());
    }
    return new Dimension();
  }

  /**
   * Returns the maximum dimensions for this layout given the components in the
   * specified target container.
   * 
   * @param target the container which needs to be laid out
   * @see Container
   * @see #minimumLayoutSize(Container)
   * @see #preferredLayoutSize(Container)
   * @return the maximum dimensions for this layout
   */
  public Dimension maximumLayoutSize(Widget target) {
    return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Returns the alignment along the x axis. This specifies how the component
   * would like to be aligned relative to other components. The value should be
   * a number between 0 and 1 where 0 represents alignment along the origin, 1
   * is aligned the farthest away from the origin, 0.5 is centered, etc.
   * 
   * @param parent the parent container
   * @return the value <code>0.5f</code> to indicate center alignment
   */
  public float getLayoutAlignmentX(Widget parent) {
    return 0.5f;
  }

  /**
   * Returns the alignment along the y axis. This specifies how the component
   * would like to be aligned relative to other components. The value should be
   * a number between 0 and 1 where 0 represents alignment along the origin, 1
   * is aligned the farthest away from the origin, 0.5 is centered, etc.
   * 
   * @param parent the parent container
   * @return the value <code>0.5f</code> to indicate center alignment
   */
  public float getLayoutAlignmentY(Widget parent) {
    return 0.5f;
  }

  /**
   * Invalidates the layout, indicating that if the layout manager has cached
   * information it should be discarded.
   * 
   * @param target the container that holds the layout to be invalidated
   */
  public void invalidateLayout(Widget target) {
    invalidateCaches();
  }

  /**
   * Lays out the specified {@link LayoutPanel} using this form layout. This
   * method reshapes widgets in the specified {@link LayoutPanel} in order to
   * satisfy the constraints of this {@code FormLayout} object.
   * <p>
   * Most applications do not call this method directly.
   * <p>
   * The form layout performs the following steps:
   * <ol>
   * <li>find components that occupy exactly one column or row
   * <li>compute minimum widths and heights
   * <li>compute preferred widths and heights
   * <li>give cols and row equal size if they share a group
   * <li>compress default columns and rows if total is less than pref size
   * <li>give cols and row equal size if they share a group
   * <li>distribute free space
   * <li>set components bounds
   * </ol>
   * 
   * @param layoutPanel the {@code LayoutPanel} in which to do the layout
   * @see LayoutPanel
   * @see LayoutPanel#layout()
   */
  public void layoutPanel(final LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());

      final int left = paddings[3];
      final int top = paddings[0];
      int width = box.width - (paddings[1] + paddings[3]);
      int height = box.height - (paddings[0] + paddings[2]);

      int[] x = computeGridOrigins(layoutPanel, width, left, colSpecs,
          colWidgets, colGroupIndices, minimumWidthMeasure,
          preferredWidthMeasure);
      int[] y = computeGridOrigins(layoutPanel, height, top, rowSpecs,
          rowWidgets, rowGroupIndices, minimumHeightMeasure,
          preferredHeightMeasure);

      layoutComponents(layoutPanel, x, y);

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
      throw new RuntimeException(e);
    }

    // if (runTwice()) {
    // recalculate(componentSizeCache.minimumSizes);
    // }

  }

  // Layout Algorithm *****************************************************

  /**
   * Initializes two lists for columns and rows that hold a column's or row's
   * widgets that span only this column or row.
   * <p>
   * Iterates over all widgets and their associated constraints; every widget
   * that has a column span or row span of 1 is put into the column's or row's
   * widget list.
   */
  private void initializeColAndRowWidgetLists() {
    colWidgets = new List[getColumnCount()];
    for (int i = 0, n = getColumnCount(); i < n; i++) {
      colWidgets[i] = new ArrayList<Widget>();
    }

    rowWidgets = new List[getRowCount()];
    for (int i = 0, n = getRowCount(); i < n; i++) {
      rowWidgets[i] = new ArrayList<Widget>();
    }

    for (Iterator<Map.Entry<Widget, CellConstraints>> i = constraintMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry<Widget, CellConstraints> entry = i.next();
      Widget component = entry.getKey();
      CellConstraints constraints = entry.getValue();
      if (takeIntoAccount(component, constraints)) {
        if (constraints.gridWidth == 1)
          colWidgets[constraints.gridX - 1].add(component);

        if (constraints.gridHeight == 1)
          rowWidgets[constraints.gridY - 1].add(component);
      }
    }
  }

  /**
   * Computes and returns the layout size of the given <code>parent</code>
   * layout panel using the specified measures.
   * 
   * @param layoutPanel the layout panel in which to do the layout
   * @param defaultWidthMeasure the measure used to compute the default width
   * @param defaultHeightMeasure the measure used to compute the default height
   * @return the layout size of the <code>parent</code> layout panel
   */
  private Dimension computeLayoutSize(LayoutPanel layoutPanel,
      Measure defaultWidthMeasure, Measure defaultHeightMeasure) {
    int[] colWidths = maximumSizes(layoutPanel, colSpecs, colWidgets,
        minimumWidthMeasure, preferredWidthMeasure, defaultWidthMeasure);
    int[] rowHeights = maximumSizes(layoutPanel, rowSpecs, rowWidgets,
        minimumHeightMeasure, preferredHeightMeasure, defaultHeightMeasure);
    int[] groupedWidths = groupedSizes(colGroupIndices, colWidths);
    int[] groupedHeights = groupedSizes(rowGroupIndices, rowHeights);

    // Convert sizes to origins.
    int[] xOrigins = computeOrigins(groupedWidths, 0);
    int[] yOrigins = computeOrigins(groupedHeights, 0);

    int width1 = sum(groupedWidths);
    int height1 = sum(groupedHeights);
    int maxWidth = width1;
    int maxHeight = height1;

    /*
     * Take components that span multiple columns or rows into account. This
     * shall be done if and only if a component spans an interval that can grow.
     */
    // First computes the maximum number of cols/rows a component
    // can span without spanning a growing column.
    int[] maxFixedSizeColsTable = computeMaximumFixedSpanTable(colSpecs);
    int[] maxFixedSizeRowsTable = computeMaximumFixedSpanTable(rowSpecs);

    for (Iterator<Map.Entry<Widget, CellConstraints>> i = constraintMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry<Widget, CellConstraints> entry = i.next();
      Widget component = entry.getKey();
      CellConstraints constraints = entry.getValue();
      if (!takeIntoAccount(component, constraints))
        continue;

      if ((constraints.gridWidth > 1)
          && (constraints.gridWidth > maxFixedSizeColsTable[constraints.gridX - 1])) {
        // int compWidth = minimumWidthMeasure.sizeOf(component);
        int compWidth = defaultWidthMeasure.sizeOf(component);
        // int compWidth = preferredWidthMeasure.sizeOf(component);
        int gridX1 = constraints.gridX - 1;
        int gridX2 = gridX1 + constraints.gridWidth;
        int lead = xOrigins[gridX1];
        int trail = width1 - xOrigins[gridX2];
        int myWidth = lead + compWidth + trail;
        if (myWidth > maxWidth) {
          maxWidth = myWidth;
        }
      }

      if ((constraints.gridHeight > 1)
          && (constraints.gridHeight > maxFixedSizeRowsTable[constraints.gridY - 1])) {
        // int compHeight = minimumHeightMeasure.sizeOf(component);
        int compHeight = defaultHeightMeasure.sizeOf(component);
        // int compHeight = preferredHeightMeasure.sizeOf(component);
        int gridY1 = constraints.gridY - 1;
        int gridY2 = gridY1 + constraints.gridHeight;
        int lead = yOrigins[gridY1];
        int trail = height1 - yOrigins[gridY2];
        int myHeight = lead + compHeight + trail;
        if (myHeight > maxHeight) {
          maxHeight = myHeight;
        }
      }
    }

    // XXX Insets insets = layoutPanel.getInsets();

    maxWidth += (margins[1] + margins[3]);
    maxHeight += (margins[0] + margins[2]);

    maxWidth += paddings[1] + paddings[3];
    maxHeight += paddings[0] + paddings[2];

    maxWidth += borders[1] + borders[3];
    maxHeight += borders[0] + borders[2];

    return new Dimension(maxWidth, maxHeight);
  }

  /**
   * Computes and returns the grid's origins.
   * 
   * @param layoutPanel the layout panel
   * @param totalSize the total size to assign
   * @param offset the offset from left or top margin
   * @param formSpecs the column or row specs, resp.
   * @param widgetLists the widgets list for each col/row
   * @param minMeasure the measure used to determine min sizes
   * @param prefMeasure the measure used to determine pre sizes
   * @param groupIndices the group specification
   * @return an int array with the origins
   */
  private int[] computeGridOrigins(LayoutPanel layoutPanel, int totalSize,
      int offset, List formSpecs, List<Widget>[] widgetLists,
      int[][] groupIndices, Measure minMeasure, Measure prefMeasure) {
    /*
     * For each spec compute the minimum and preferred size that is the maximum
     * of all component minimum and preferred sizes resp.
     */
    int[] minSizes = maximumSizes(layoutPanel, formSpecs, widgetLists,
        minMeasure, prefMeasure, minMeasure);
    int[] prefSizes = maximumSizes(layoutPanel, formSpecs, widgetLists,
        minMeasure, prefMeasure, prefMeasure);

    int[] groupedMinSizes = groupedSizes(groupIndices, minSizes);
    int[] groupedPrefSizes = groupedSizes(groupIndices, prefSizes);
    int totalMinSize = sum(groupedMinSizes);
    int totalPrefSize = sum(groupedPrefSizes);
    int[] compressedSizes = compressedSizes(formSpecs, totalSize, totalMinSize,
        totalPrefSize, groupedMinSizes, prefSizes);
    int[] groupedSizes = groupedSizes(groupIndices, compressedSizes);
    int totalGroupedSize = sum(groupedSizes);
    int[] sizes = distributedSizes(formSpecs, totalSize, totalGroupedSize,
        groupedSizes);
    return computeOrigins(sizes, offset);
  }

  /**
   * Computes origins from sizes taking the specified offset into account.
   * 
   * @param sizes the array of sizes
   * @param offset an offset for the first origin
   * @return an array of origins
   */
  private int[] computeOrigins(int[] sizes, int offset) {
    int count = sizes.length;
    int[] origins = new int[count + 1];
    origins[0] = offset;
    for (int i = 1; i <= count; i++) {
      origins[i] = origins[i - 1] + sizes[i - 1];
    }
    return origins;
  }

  /**
   * Lays out the components using the given x and y origins, the column and row
   * specifications, and the component constraints.
   * <p>
   * 
   * The actual computation is done by each component's form constraint object.
   * We just compute the cell, the cell bounds and then hand over the component,
   * cell bounds, and measure to the form constraints. This will allow potential
   * subclasses of <code>CellConstraints</code> to do special micro-layout
   * corrections. For example, such a subclass could map JComponent classes to
   * visual layout bounds that may lead to a slightly different bounds.
   * 
   * @param x an int array of the horizontal origins
   * @param y an int array of the vertical origins
   */
  private void layoutComponents(LayoutPanel layoutPanel, int[] x, int[] y) {
    Rectangle cellBounds = new Rectangle();
    for (Iterator<Map.Entry<Widget, CellConstraints>> i = constraintMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry<Widget, CellConstraints> entry = i.next();
      Widget component = entry.getKey();
      CellConstraints constraints = entry.getValue();

      int gridX = constraints.gridX - 1;
      int gridY = constraints.gridY - 1;
      int gridWidth = constraints.gridWidth;
      int gridHeight = constraints.gridHeight;
      cellBounds.x = x[gridX];
      cellBounds.y = y[gridY];
      cellBounds.width = x[gridX + gridWidth] - cellBounds.x;
      cellBounds.height = y[gridY + gridHeight] - cellBounds.y;

      constraints.setBounds(layoutPanel, component, this, cellBounds,
          minimumWidthMeasure, minimumHeightMeasure, preferredWidthMeasure,
          preferredHeightMeasure);
    }
  }

  /**
   * Invalidates the component size caches.
   */
  private void invalidateCaches() {
    componentSizeCache.invalidate();
  }

  /**
   * Computes and returns the sizes for the given form specs, widget lists and
   * measures for minimum, preferred, and default size.
   * 
   * @param layoutPanel the layout panel
   * @param formSpecs the column or row specs, resp.
   * @param widgetLists the widgets list for each col/row
   * @param minMeasure the measure used to determine min sizes
   * @param prefMeasure the measure used to determine pre sizes
   * @param defaultMeasure the measure used to determine default sizes
   * @return the column or row sizes
   */
  private int[] maximumSizes(LayoutPanel layoutPanel, List formSpecs,
      List<Widget>[] widgetLists, Measure minMeasure, Measure prefMeasure,
      Measure defaultMeasure) {
    FormSpec formSpec;
    int size = formSpecs.size();
    int[] result = new int[size];
    for (int i = 0; i < size; i++) {
      formSpec = (FormSpec) formSpecs.get(i);
      result[i] = formSpec.maximumSize(layoutPanel, widgetLists[i], minMeasure,
          prefMeasure, defaultMeasure);
    }
    return result;
  }

  /**
   * Computes and returns the compressed sizes. Compresses space for columns and
   * rows if the available space is less than the total preferred size but more
   * than the total minimum size.
   * <p>
   * Only columns and rows that are specified to be compressible will be
   * affected. You can specify a column and row as compressible by giving it the
   * component size <tt>default</tt>.
   * 
   * @param formSpecs the column or row specs to use
   * @param totalSize the total available size
   * @param totalMinSize the sum of all minimum sizes
   * @param totalPrefSize the sum of all preferred sizes
   * @param minSizes an int array of column/row minimum sizes
   * @param prefSizes an int array of column/row preferred sizes
   * @return an int array of compressed column/row sizes
   */
  private int[] compressedSizes(List<FormSpec> formSpecs, int totalSize,
      int totalMinSize, int totalPrefSize, int[] minSizes, int[] prefSizes) {

    // If we have less space than the total min size, answer the min sizes.
    if (totalSize < totalMinSize)
      return minSizes;
    // If we have more space than the total pref size, answer the pref sizes.
    if (totalSize >= totalPrefSize)
      return prefSizes;

    int count = formSpecs.size();
    int[] sizes = new int[count];

    double totalCompressionSpace = totalPrefSize - totalSize;
    double maxCompressionSpace = totalPrefSize - totalMinSize;
    double compressionFactor = totalCompressionSpace / maxCompressionSpace;

    // System.out.println("Total compression space=" + totalCompressionSpace);
    // System.out.println("Max compression space  =" + maxCompressionSpace);
    // System.out.println("Compression factor     =" + compressionFactor);

    for (int i = 0; i < count; i++) {
      FormSpec formSpec = (FormSpec) formSpecs.get(i);
      sizes[i] = prefSizes[i];
      if (formSpec.getSize().compressible()) {
        sizes[i] -= (int) Math.round((prefSizes[i] - minSizes[i])
            * compressionFactor);
      }
    }
    return sizes;
  }

  /**
   * Computes and returns the grouped sizes. Gives grouped columns and rows the
   * same size.
   * 
   * @param groups the group specification
   * @param rawSizes the raw sizes before the grouping
   * @return the grouped sizes
   */
  private int[] groupedSizes(int[][] groups, int[] rawSizes) {
    // Return the compressed sizes if there are no groups.
    if (groups == null || groups.length == 0) {
      return rawSizes;
    }

    // Initialize the result with the given compressed sizes.
    int[] sizes = new int[rawSizes.length];
    for (int i = 0; i < sizes.length; i++) {
      sizes[i] = rawSizes[i];
    }

    // For each group equalize the sizes.
    for (int group = 0; group < groups.length; group++) {
      int[] groupIndices = groups[group];
      int groupMaxSize = 0;
      // Compute the group's maximum size.
      for (int i = 0; i < groupIndices.length; i++) {
        int index = groupIndices[i] - 1;
        groupMaxSize = Math.max(groupMaxSize, sizes[index]);
      }
      // Set all sizes of this group to the group's maximum size.
      for (int i = 0; i < groupIndices.length; i++) {
        int index = groupIndices[i] - 1;
        sizes[index] = groupMaxSize;
      }
    }
    return sizes;
  }

  /**
   * Distributes free space over columns and rows and returns the sizes after
   * this distribution process.
   * 
   * @param formSpecs the column/row specifications to work with
   * @param totalSize the total available size
   * @param totalPrefSize the sum of all preferred sizes
   * @param inputSizes the input sizes
   * @return the distributed sizes
   */
  private int[] distributedSizes(List<FormSpec> formSpecs, int totalSize,
      int totalPrefSize, int[] inputSizes) {
    double totalFreeSpace = totalSize - totalPrefSize;
    // Do nothing if there's no free space.
    if (totalFreeSpace < 0)
      return inputSizes;

    // Compute the total weight.
    int count = formSpecs.size();
    double totalWeight = 0.0;
    for (int i = 0; i < count; i++) {
      FormSpec formSpec = (FormSpec) formSpecs.get(i);
      totalWeight += formSpec.getResizeWeight();
    }

    // Do nothing if there's no resizing column.
    if (totalWeight == 0.0)
      return inputSizes;

    int[] sizes = new int[count];

    double restSpace = totalFreeSpace;
    int roundedRestSpace = (int) totalFreeSpace;
    for (int i = 0; i < count; i++) {
      FormSpec formSpec = (FormSpec) formSpecs.get(i);
      double weight = formSpec.getResizeWeight();
      if (weight == FormSpec.NO_GROW) {
        sizes[i] = inputSizes[i];
      } else {
        double roundingCorrection = restSpace - roundedRestSpace;
        double extraSpace = totalFreeSpace * weight / totalWeight;
        double correctedExtraSpace = extraSpace - roundingCorrection;
        int roundedExtraSpace = (int) Math.round(correctedExtraSpace);
        sizes[i] = inputSizes[i] + roundedExtraSpace;
        restSpace -= extraSpace;
        roundedRestSpace -= roundedExtraSpace;
      }
    }
    return sizes;
  }

  /**
   * Computes and returns a table that maps a column/row index to the maximum
   * number of columns/rows that a component can span without spanning a growing
   * column.
   * <p>
   * 
   * Iterates over the specs from right to left/bottom to top, sets the table
   * value to zero if a spec can grow, otherwise increases the span by one.
   * <p>
   * 
   * <strong>Examples:</strong>
   * 
   * <pre>
   * &quot;pref, 4dlu, pref, 2dlu, p:grow, 2dlu,      pref&quot; -&gt;
   * [4,    3,    2,    1,    0,      MAX_VALUE, MAX_VALUE]
   * &quot;p:grow, 4dlu, p:grow, 9dlu,      pref&quot; -&gt;
   * [0,      1,    0,      MAX_VALUE, MAX_VALUE]
   * &quot;p, 4dlu, p, 2dlu, 0:grow&quot; -&gt;
   * [4, 3,    2, 1,    0]
   * </pre>
   * 
   * @param formSpecs the column specs or row specs
   * @return a table that maps a spec index to the maximum span for fixed size
   *         specs
   */
  private int[] computeMaximumFixedSpanTable(List formSpecs) {
    int size = formSpecs.size();
    int[] table = new int[size];
    int maximumFixedSpan = Integer.MAX_VALUE; // Could be 1
    for (int i = size - 1; i >= 0; i--) {
      FormSpec spec = (FormSpec) formSpecs.get(i); // ArrayList access
      if (spec.canGrow()) {
        maximumFixedSpan = 0;
      }
      table[i] = maximumFixedSpan;
      if (maximumFixedSpan < Integer.MAX_VALUE)
        maximumFixedSpan++;
    }
    return table;
  }

  // Helper Code ************************************************************

  /**
   * Computes and returns the sum of integers in the given array of ints.
   * 
   * @param sizes an array of ints to sum up
   * @return the sum of ints in the array
   */
  private static int sum(int[] sizes) {
    int sum = 0;
    for (int i = sizes.length - 1; i >= 0; i--) {
      sum += sizes[i];
    }
    return sum;
  }

  // private static void invalidateAndRepaint(LayoutPanel container) {
  // if (container == null)
  // return;
  // if (container instanceof JComponent) {
  // ((JComponent) container).revalidate();
  // } else {
  // container.invalidate();
  // }
  // container.repaint();
  // }

  /**
   * Components are taken into account, if a) they are visible, or b) they have
   * no individual setting and the container-wide settings ignores the
   * visibility, or c) the individual component ignores the visibility.
   * 
   * @param component
   * @param cc
   * @return <code>true</code> if the component shall be taken into account,
   *         <code>false</code> otherwise
   */
  private boolean takeIntoAccount(Widget component, CellConstraints cc) {
    return component.isVisible()
        || ((cc.honorsVisibility == null) && !getHonorsVisibility())
        || Boolean.FALSE.equals(cc.honorsVisibility);
  }

  // Measuring Component Sizes ********************************************

  /**
   * An interface that describes how to measure a {@code Widget}. Used to
   * abstract from horizontal and vertical dimensions as well as minimum and
   * preferred sizes.
   */
  public static interface Measure {

    /**
     * Computes and returns the size of the given {@code Component}.
     * 
     * @param widget the widget to measure
     * @return the widget's size
     */
    int sizeOf(Widget widget);
  }

  /**
   * An abstract implementation of the <code>Measure</code> interface that
   * caches widget sizes.
   */
  private abstract static class CachingMeasure implements Measure, Serializable {
    private static final long serialVersionUID = 127202010383163822L;

    /**
     * Holds previously requested widget sizes. Used to minimize size requests
     * to subwidgets.
     */
    protected final ComponentSizeCache cache;

    private CachingMeasure(ComponentSizeCache cache) {
      this.cache = cache;
    }

  }

  /**
   * Measures a widget by computing its minimum width.
   */
  private static final class MinimumWidthMeasure extends CachingMeasure {
    private static final long serialVersionUID = 5292170041706270613L;

    private MinimumWidthMeasure(ComponentSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget c) {
      return cache.getMinimumSize(c).width;
    }
  }

  /**
   * Measures a widget by computing its minimum height.
   */
  private static final class MinimumHeightMeasure extends CachingMeasure {
    private static final long serialVersionUID = 6309128736400196915L;

    private MinimumHeightMeasure(ComponentSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget c) {
      return cache.getMinimumSize(c).height;
    }
  }

  /**
   * Measures a widget by computing its preferred width.
   */
  private static final class PreferredWidthMeasure extends CachingMeasure {
    private static final long serialVersionUID = -7722605116642790850L;

    private PreferredWidthMeasure(ComponentSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget c) {
      return cache.getPreferredSize(c).width;
    }
  }

  /**
   * Measures a widget by computing its preferred height.
   */
  private static final class PreferredHeightMeasure extends CachingMeasure {
    private static final long serialVersionUID = -7294928543479708370L;

    private PreferredHeightMeasure(ComponentSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget c) {
      return cache.getPreferredSize(c).height;
    }
  }

  // Caching Component Sizes **********************************************

  /**
   * A cache for widget minimum and preferred sizes. Used to reduce the requests
   * to determine a widget's size.
   */
  private static final class ComponentSizeCache implements Serializable {
    private static final long serialVersionUID = -209607124276425146L;

    /** Maps components to their minimum sizes. */
    private final Map<Widget, Dimension> minimumSizes;

    /** Maps components to their preferred sizes. */
    private final Map<Widget, Dimension> preferredSizes;

    /**
     * Constructs a <code>ComponentSizeCache</code>.
     * 
     * @param initialCapacity the initial cache capacity
     */
    private ComponentSizeCache(int initialCapacity) {
      minimumSizes = new HashMap<Widget, Dimension>(initialCapacity);
      preferredSizes = new HashMap<Widget, Dimension>(initialCapacity);
      // preferredSizes = minimumSizes;
    }

    /**
     * Invalidates the cache. Clears all stored size information.
     */
    void invalidate() {
      minimumSizes.clear();
      preferredSizes.clear();
    }

    /**
     * Returns the minimum size for the given widget. Tries to look up the value
     * from the cache; lazily creates the value if it has not been requested
     * before.
     * 
     * @param widget the widget to compute the minimum size
     * @return the widget's minimum size
     */
    Dimension getMinimumSize(Widget widget) {
      Dimension size = minimumSizes.get(widget);
      if (size == null) {
        final String minWidth = DOM.getComputedStyleAttribute(
            widget.getElement(), "minWidth");
        final String minHeight = DOM.getComputedStyleAttribute(
            widget.getElement(), "minHeight");
        size = new Dimension(minWidth == null ? 1 : DOM.toPixelSize(minWidth),
            minHeight == null ? 1 : DOM.toPixelSize(minHeight));
        minimumSizes.put(widget, size);
      }
      return size;
    }

    /**
     * Returns the preferred size for the given widget. Tries to look up the
     * value from the cache; lazily creates the value if it has not been
     * requested before.
     * 
     * @param widget the widget to compute the preferred size
     * @return the widget's preferred size
     */
    Dimension getPreferredSize(Widget widget) {
      Dimension size = preferredSizes.get(widget);
      if (size == null) {
        size = WidgetHelper.getPreferredSize(widget);// widget.getPreferredSize();
        preferredSizes.put(widget, size);
      }
      return size;
    }

    void removeEntry(Widget component) {
      minimumSizes.remove(component);
      preferredSizes.remove(component);
    }
  }

  // Exposing the Layout Information **************************************

  /**
   * Computes and returns the horizontal and vertical grid origins. Performs the
   * same layout process as <code>#layoutContainer</code> but does not layout
   * the components.
   * <p>
   * 
   * This method has been added only to make it easier to debug the form layout.
   * <strong>You must not call this method directly; It may be removed in a
   * future release or the visibility may be reduced.</strong>
   * 
   * @param parent the <code>Container</code> to inspect
   * @return an object that comprises the grid x and y origins
   */
  public LayoutInfo getLayoutInfo(LayoutPanel parent) {
    initializeColAndRowWidgetLists();
    final Dimension size = WidgetHelper.getOffsetSize(parent);

    // FIXME
    // Insets insets = parent.getInsets();
    int totalWidth = size.width;// - insets.left - insets.right;
    int totalHeight = size.height;// - insets.top - insets.bottom;

    int[] x = computeGridOrigins(parent, totalWidth, 0/* insets.left */,
        colSpecs, colWidgets, colGroupIndices, minimumWidthMeasure,
        preferredWidthMeasure);
    int[] y = computeGridOrigins(parent, totalHeight, 0/* insets.top */,
        rowSpecs, rowWidgets, rowGroupIndices, minimumHeightMeasure,
        preferredHeightMeasure);
    return new LayoutInfo(x, y);
  }

  /**
   * Stores column and row origins.
   */
  public static final class LayoutInfo {

    /**
     * Holds the origins of the columns.
     */
    public final int[] columnOrigins;

    /**
     * Holds the origins of the rows.
     */
    public final int[] rowOrigins;

    private LayoutInfo(int[] xOrigins, int[] yOrigins) {
      this.columnOrigins = xOrigins;
      this.rowOrigins = yOrigins;
    }

    /**
     * Returns the layout's horizontal origin, the origin of the first column.
     * 
     * @return the layout's horizontal origin, the origin of the first column.
     */
    public int getX() {
      return columnOrigins[0];
    }

    /**
     * Returns the layout's vertical origin, the origin of the first row.
     * 
     * @return the layout's vertical origin, the origin of the first row.
     */
    public int getY() {
      return rowOrigins[0];
    }

    /**
     * Returns the layout's width, the size between the first and the last
     * column origin.
     * 
     * @return the layout's width.
     */
    public int getWidth() {
      return columnOrigins[columnOrigins.length - 1] - columnOrigins[0];
    }

    /**
     * Returns the layout's height, the size between the first and last row.
     * 
     * @return the layout's height.
     */
    public int getHeight() {
      return rowOrigins[rowOrigins.length - 1] - rowOrigins[0];
    }

  }

  // Helper Code **********************************************************

  /**
   * Creates and returns a deep copy of the given array. Unlike
   * <code>#clone</code> that performs a shallow copy, this method copies both
   * array levels.
   * 
   * @param array the array to clone
   * @return a deep copy of the given array
   * 
   * @see Object#clone()
   */
  private int[][] deepClone(int[][] array) {
    int[][] result = new int[array.length][];
    for (int i = 0; i < result.length; i++) {
      result[i] = new int[array[i].length];
      for (int j = 0; j < array[i].length; j++) {
        result[i][j] = array[i][j];
      }
    }
    return result;
  }

  // GWT Mosaic (NEW CODE) ************************************************

  // private Map<Widget, Dimension> widgetSizes = new HashMap<Widget,
  // Dimension>();

  @Override
  public void flushCache() {
    // widgetSizes.clear();
    invalidateCaches();
    initialized = false;
  }

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    constraintMap.clear();

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget widget = iter.next();
      addLayoutComponent(widget, getLayoutData(widget));
    }

    initializeColAndRowWidgetLists();

    return initialized = true;
  }

  @Override
  public boolean runTwice() {
    return true; // Safari
  }
}
