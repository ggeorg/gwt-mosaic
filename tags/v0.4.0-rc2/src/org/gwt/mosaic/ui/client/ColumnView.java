/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.ColumnLayout;
import org.gwt.mosaic.ui.client.layout.ColumnLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T>
 */
public class ColumnView<T> extends LayoutComposite {
  private static final String DEFAULT_CLASSNAME = "mosaic-ColumnView";

  private final ScrollLayoutPanel scrollPanel = new ScrollLayoutPanel(
      new BoxLayout(Orientation.HORIZONTAL));

  private final LayoutPanel columnView = new LayoutPanel(new ColumnLayout()) {
    @Override
    protected void onLayout() {
      super.onLayout();
      if (scrollPanel.needsLayout()) {
        scrollPanel.layout();
      }
    }
  };

  private final List<ColumnViewItem<T>> columns = new ArrayList<ColumnViewItem<T>>();
  private final ColumnViewItem<T> resizeHotSpot = new ColumnViewItem<T>();

  public ColumnView() {
    super();

    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.add(scrollPanel);

    scrollPanel.add(columnView, new BoxLayoutData(FillStyle.VERTICAL));
    columnView.add(resizeHotSpot, new ColumnLayoutData("20em"));

    // TODO move to CSS {
    Style style = scrollPanel.getElement().getStyle();
    style.setPaddingTop(0, Unit.PX);
    style.setPaddingRight(0, Unit.PX);
    style.setPaddingLeft(0, Unit.PX);
    columnView.setPadding(0);
    // }

    setStyleName(DEFAULT_CLASSNAME);
  }

  public void setPadding(int padding) {
    getLayoutPanel().setPadding(padding);
  }

  /**
   * Inserts the specified {@link ColumnViewItem} at the specified position.
   * Removes the {@link ColumnViewItem} element currently at that position (if
   * any) and any subsequent elements to the right.
   * 
   * @param beforeIndex index at which the specified {@link ColumnViewItem} is
   *          to be inserted
   * @param column {@link ColumnViewItem} to be inserted
   * @param width the width of the inserted {@link ColumnViewItem} element
   */
  public void addColumn(int beforeIndex, final ColumnViewItem<T> column,
      String width) {

    // delete any elements on the left
    for (int i = columns.size() - 1; i >= beforeIndex; i--) {
      columns.remove(i);
      columnView.remove(i);
    }

    // physical add
    columns.add(beforeIndex, column);
    columnView.insert(column, new ColumnLayoutData(width), beforeIndex);

    // focus
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        scrollPanel.ensureVisible(column);
      }
    });
  }

  /**
   * Removes the {@link ColumnViewItem} at the specified position and any
   * subsequent elements to the right.
   * 
   * @param index index at which the {@link ColumnViewItem} is to be removed
   */
  public void deleteColumn(int index) {
    // delete any elements on the left
    for (int i = columns.size() - 1; i >= index; i--) {
      columns.remove(i);
      columnView.remove(i);
    }
  }

  // Query Operations

  /**
   * Returns the number of {@link ColumnViewItem} elements in this view.
   * 
   * @return the number of {@link ColumnViewItem} elements
   */
  public int size() {
    return columns.size();
  }

  // Positional Access Operations

  /**
   * Returns the {@link ColumnViewItem} element at the specified position in
   * this list.
   * 
   * @param index index of {@link ColumnViewItem} element to return
   * @return the {@link ColumnViewItem} at the specified position
   * 
   * @throws IndexOutOfBoundsException if the index is out of range (index &lt;
   *           0 || index &gt;= size()).
   */
  public ColumnViewItem<T> get(int index) {
    return columns.get(index);
  }

  // Search Operations

  /**
   * Returns the index in this {@code ColumnView} of the first occurrence of the
   * specified {@link CloumnViewItem} element, or -1 if this {@code ColumnView}
   * does not contain this {@link ColumnViewItem} element.
   * 
   * @param column {@link ColumnViewItem} element to search for
   * @param the index in this {@ColumnView} of the specified
   *          {@link ColumnViewItem} element,or -1 if this {@code ColumnView}
   *          does not contain this {@link ColumnViewItem} element
   */
  public int indexOf(ColumnViewItem<T> column) {
    return columns.indexOf(column);
  }

  // ColumnViewItem class

  public static class ColumnViewItem<T> extends LayoutComposite {
    private T data;
    private Widget widget;

    ColumnViewItem() {
      // Nothing to do here!
    }

    public ColumnViewItem(T data) {
      this.data = data;
    }

    /**
     * @return the widget
     */
    public Widget getWidget() {
      return widget;
    }

    /**
     * @param widget the widget to set
     */
    public void setWidget(Widget widget) {
      if (this.widget != widget) {
        getLayoutPanel().remove(widget);
      }
      this.widget = widget;
      if (widget != null) {
        getLayoutPanel().add(widget);
      }
    }

    /**
     * @return the data
     */
    public T getData() {
      return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
      this.data = data;
    }
  }
}
