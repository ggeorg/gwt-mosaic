package org.mosaic.ui.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mosaic.core.client.DOM;
import org.mosaic.core.client.model.PropertyChangeEvent;
import org.mosaic.core.client.model.PropertyChangeListener;
import org.mosaic.ui.client.event.TableColumnModelEvent;
import org.mosaic.ui.client.event.TableColumnModelListener;
import org.mosaic.ui.client.event.TableModelEvent;
import org.mosaic.ui.client.event.TableModelListener;
import org.mosaic.ui.client.event.TableModelEvent.TableModelEventType;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.HasLayout;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;
import org.mosaic.ui.client.table.DefaultTableColumnModel;
import org.mosaic.ui.client.table.TableColumn;
import org.mosaic.ui.client.table.TableColumnModel;
import org.mosaic.ui.client.table.TableModel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class Table extends Composite implements HasLayout, TableModelListener,
    TableColumnModelListener {

  public class HeaderCell extends Widget implements HasLayout, PropertyChangeListener {

    private static final int DRAG_WIDGET_WIDTH = 4;

    private static final int MINIMUM_COL_WIDTH = 20;

    private final TableColumn column;

    Element td = DOM.createTD();
    Element captionContainer = DOM.createDiv();
    Element colResizeWidget = DOM.createDiv();
    Element floatingCopyOfHeaderCell;

    private boolean dragging;

    private int dragStartX;
    private int colIndex;
    private int originalWidth;

    private boolean isResizing;

    private int headerX;

    private boolean moved;

    private int closestSlot;

    public HeaderCell(TableColumn column) {
      this.column = column;
      this.column.addPropertyChangeListener(this);

      DOM.setElementProperty(colResizeWidget, "className", DEFAULT_STYLENAME + "-resizer");
      DOM.setStyleAttribute(colResizeWidget, "width", DRAG_WIDGET_WIDTH + "px");
      DOM.sinkEvents(colResizeWidget, Event.MOUSEEVENTS);

      // TODO call a label provider
      setText(column.getHeaderValue().toString());

      DOM.appendChild(td, colResizeWidget);

      DOM.setElementProperty(captionContainer, "className", DEFAULT_STYLENAME
          + "-caption-container");

      // ensure no clipping initially (problem on column additions)
      // DOM.setStyleAttribute(captionContainer, "overflow", "visible");
      DOM.setStyleAttribute(captionContainer, "overflow", "hidden");

      DOM.sinkEvents(captionContainer, Event.MOUSEEVENTS);

      DOM.appendChild(td, captionContainer);
      DOM.sinkEvents(td, Event.MOUSEEVENTS);

      setElement(td);

      setStyleName(DEFAULT_STYLENAME + "-header-cell");
    }

    private void createFloatingCopy() {
      floatingCopyOfHeaderCell = DOM.createDiv();
      DOM.setInnerHTML(floatingCopyOfHeaderCell, DOM.getInnerHTML(td));
      floatingCopyOfHeaderCell = DOM.getChild(floatingCopyOfHeaderCell, 1);
      DOM.setElementProperty(floatingCopyOfHeaderCell, "className", DEFAULT_STYLENAME
          + "-header-drag");
      updateFloatingCopysPosition(DOM.getAbsoluteLeft(td), DOM.getAbsoluteTop(td));
      DOM.appendChild(RootPanel.get().getElement(), floatingCopyOfHeaderCell);
    }

    public String getCaption() {
      return DOM.getInnerText(captionContainer);
    }

    public int getWidth() {
      return column.getWidth();
    }

    protected void handleCaptionEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
          if (Table.this.isReorderingAllowed()) {
            dragging = true;
            moved = false;
            colIndex = Table.this.getColumnModel().getColumnIndex(column);
            DOM.setCapture(getElement());
            headerX = tableHeader.getAbsoluteLeft();
            DOM.eventPreventDefault(event);
          }
          break;
        case Event.ONMOUSEUP:
          if (Table.this.isReorderingAllowed()) {
            dragging = false;
            DOM.releaseCapture(getElement());
            if (moved) {
              hideFloatingCopy();
              Table.this.tableHeader.removeSlotFocus();
              if (closestSlot != colIndex && closestSlot != (colIndex + 1)) {
                if (closestSlot > colIndex) {
                  Table.this.reOrderColumn(column, closestSlot - 1);
                } else {
                  Table.this.reOrderColumn(column, closestSlot);
                }
              }
            }
          }

          if (!moved) {
            // mouse event was a click to header -> sort column
            // if (sortable) {
            // TODO
            // }
          }
          break;
        case Event.ONMOUSEMOVE:
          if (dragging) {
            if (!moved) {
              createFloatingCopy();
              moved = true;
            }
            final int x = DOM.eventGetClientX(event)
                + DOM.getElementPropertyInt(tableHeader.hTableWrapper, "scrollLeft");
            int slotX = headerX;
            closestSlot = colIndex;
            int closestDistance = -1;
            int start = 0;
            final int visibleCellCount = Table.this.getColumnModel().getColumnCount();
            for (int i = start; i <= visibleCellCount; i++) {
              if (i > 0) {
                final int colIdx = Table.this.getColumnModel().getColumnIndex(column);
                slotX += Table.this.getColWidth(colIdx);
              }
              final int dist = Math.abs(x - slotX);
              if (closestDistance == -1 || dist < closestDistance) {
                closestDistance = dist;
                closestSlot = i;
              }
            }
            Table.this.tableHeader.focusSlot(closestSlot);

            updateFloatingCopysPosition(DOM.eventGetClientX(event), -1);
          }
          break;
        default:
          break;
      }
    }

    private void hideFloatingCopy() {
      DOM.removeChild(RootPanel.get().getElement(), floatingCopyOfHeaderCell);
      floatingCopyOfHeaderCell = null;
    }

    public boolean isEnabled() {
      return getParent() != null;
    }

    public void layout() {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    public void onBrowserEvent(Event event) {
      // if (enabled) {
      if (isResizing || event.getTarget() == colResizeWidget) {
        onResizeEvent(event);
      } else {
        handleCaptionEvent(event);
      }
      // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mosaic.core.client.model.PropertyChangeListener#onPropertyChange(org.mosaic.core.client.model.PropertyChangeEvent)
     */
    public void onPropertyChange(PropertyChangeEvent event) {
      if (event.getProperty() == TableColumn.WIDTH) {
        final int width = (Integer) event.getNewValue();
        DOM.setWidth(captionContainer, width - DRAG_WIDGET_WIDTH - 4);
        DOM.setWidth(getElement(), width);
      }
    }

    private void onResizeEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
          isResizing = true;
          DOM.setCapture(getElement());
          dragStartX = DOM.eventGetClientX(event);
          colIndex = column.getModelIndex(); // getColIndexByKey(column);
          originalWidth = getOffsetWidth(); // getWidth();
          DOM.eventPreventDefault(event);
          break;
        case Event.ONMOUSEUP:
          isResizing = false;
          DOM.releaseCapture(getElement());
          break;
        case Event.ONMOUSEMOVE:
          if (isResizing) {
            final int deltaX = DOM.eventGetClientX(event) - dragStartX;
            if (deltaX == 0) {
              return;
            }

            final int newWidth = originalWidth + deltaX;

            Table.this.setColWidth(colIndex, newWidth);
          }
          break;
        default:
          break;
      }
    }

    public void setText(String headerText) {
      DOM.setInnerText(captionContainer, headerText);
    }

    public void setWidth(int width) {
      // TODO check min/max width
      if (width == -1) {
        // go to default mode, clip content if necessary
        // DOM.setStyleAttribute(captionContainer, "overflow", "");
      }

      column.setWidth(width);
    }

    private void updateFloatingCopysPosition(int x, int y) {
      x -= DOM.getElementPropertyInt(floatingCopyOfHeaderCell, "offsetWidth") / 2;
      DOM.setStyleAttribute(floatingCopyOfHeaderCell, "left", x + "px");
      if (y > 0) {
        DOM.setStyleAttribute(floatingCopyOfHeaderCell, "top", (y + 7) + "px");
      }
    }

  }

  public class TableHeader extends Panel implements HasLayout {

    Element div = DOM.createDiv();
    Element hTableWrapper = DOM.createDiv();
    Element hTableContainer = DOM.createDiv();
    Element table = DOM.createTable();
    Element headerTableBody = DOM.createTBody();
    Element tr = DOM.createTR();

    private Map<TableColumn, HeaderCell> cells = new HashMap<TableColumn, HeaderCell>();

    private final Element columnSelector = DOM.createDiv();

    private int focusedSlot = -1;

    public TableHeader() {
      DOM.setStyleAttribute(hTableWrapper, "overflow", "hidden");
      DOM.setElementProperty(hTableWrapper, "className", DEFAULT_STYLENAME + "-header");

      // TODO move styles to CSS
      DOM.setElementProperty(columnSelector, "className", DEFAULT_STYLENAME
          + "-column-selector");
      DOM.setStyleAttribute(columnSelector, "display", "none");

      DOM.appendChild(table, headerTableBody);
      DOM.appendChild(headerTableBody, tr);
      DOM.appendChild(hTableContainer, table);
      DOM.appendChild(hTableWrapper, hTableContainer);
      DOM.appendChild(div, hTableWrapper);
      DOM.appendChild(div, columnSelector);
      setElement(div);

      setStyleName(DEFAULT_STYLENAME + "-header-wrap");

      DOM.sinkEvents(columnSelector, Event.ONCLICK);
    }

    public void focusSlot(int index) {
      removeSlotFocus();
      if (index > 0) {
        DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(tr, index - 1)),
            "className", DEFAULT_STYLENAME + "-resizer " + DEFAULT_STYLENAME
                + "-focus-slot-right");
      } else {
        DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(tr, index)), "className",
            DEFAULT_STYLENAME + "-resizer " + DEFAULT_STYLENAME + "-focus-slot-left");
      }
      focusedSlot = index;
    }

    public HeaderCell getHeaderCell(int index) {
      final TableColumn col = Table.this.getColumnModel().getColumn(index);
      if (col != null) {
        return cells.get(col);
      } else {
        return null;
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
     */
    public Iterator<Widget> iterator() {
      final Iterator<TableColumn> iter = Table.this.getColumnModel().getColumns();
      return new Iterator<Widget>() {
        public boolean hasNext() {
          return iter.hasNext();
        }

        public Widget next() {
          return cells.get(iter.next());
        }

        public void remove() {
          throw new RuntimeException("remove() Not allowed");
        }
      };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mosaic.ui.client.layout.HasLayout#layout()
     */
    public void layout() {
      // TODO Auto-generated method stub

    }

    public void moveCell(int columnIndex, int newIndex) {
      final HeaderCell hCell = getHeaderCell(columnIndex);
      final Element cell = hCell.getElement();

      DOM.removeChild(tr, cell);
      DOM.insertChild(tr, cell, newIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.Panel#remove(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public boolean remove(Widget child) {
      if (child != null) {
        DOM.removeChild(DOM.getParent(child.getElement()), child.getElement());
        return true;
      }
      return false;
    }

    public void removeHeaderCell(TableColumn col) {
      if (remove(cells.get(col))) {
        cells.remove(col);
      }
    }

    private void removeSlotFocus() {
      if (focusedSlot < 0) {
        return;
      }
      if (focusedSlot == 0) {
        DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(tr, focusedSlot)),
            "className", DEFAULT_STYLENAME + "-resizer");
      } else if (focusedSlot > 0) {
        DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(tr, focusedSlot - 1)),
            "className", DEFAULT_STYLENAME + "-resizer");
      }
      focusedSlot = -1;
    }

    public void setHeaderCell(int index, HeaderCell cell) {
      final int visibleCells = tr.getChildNodes().getLength();
      if (index < visibleCells) {
        // insert to right slot
        DOM.insertChild(tr, cell.getElement(), index);
        adopt(cell);
      } else if (index == visibleCells) {
        // simply append
        DOM.appendChild(tr, cell.getElement());
        adopt(cell);
      } else {
        throw new RuntimeException("Header cells must be appended in order");
      }
    }

    public void updateFromModel() {
      Iterator<TableColumn> it = Table.this.getColumnModel().getColumns();
      for (int i = 0; it.hasNext(); i++) {
        final TableColumn col = it.next();
        final HeaderCell c = new HeaderCell(col);
        cells.put(col, c);
        setHeaderCell(i, c);
        if (col.getPreferredWidth() != 0) {
          c.setWidth(col.getPreferredWidth());
        } else {
          DeferredCommand.addCommand(new Command() {
            public void execute() {
              int[] box = DOM.getBoxSize(c.getElement());
              c.setWidth(box[0]);
            }
          });
        }
      }
    }

  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-Table";

  public boolean reorderingAllowed = true;

  private final BorderLayout layout = new BorderLayout();

  private final TableHeader tableHeader = new TableHeader();

  private final ScrollPanel tableBody = new ScrollPanel();

  protected TableColumnModel columnModel;

  protected TableModel dataModel;

  public Table(TableModel dataModel, TableColumnModel columnModel) {
    final LayoutPanel layoutPanel = new LayoutPanel(layout);
    layout.setMargin(0);
    layout.setSpacing(0);

    layoutPanel.add(tableHeader, new BorderLayoutData(BorderLayoutRegion.NORTH));

    tableBody.addStyleName(DEBUG_ID_PREFIX + "-body");
    layoutPanel.add(tableBody);

    if (columnModel == null) {
      columnModel = createDefaultColumnModel();
    }
    setColumnModel(columnModel);

    if (dataModel == null) {
      dataModel = createDefaultDataModel();
    }
    setModel(dataModel);

    initWidget(layoutPanel);

    setStyleName(DEFAULT_STYLENAME);
  }

  public void columnAdded(TableColumnModelEvent e) {
    // TODO Auto-generated method stub

  }

  public void columnMoved(TableColumnModelEvent e) {
    tableHeader.moveCell(e.getFromIndex(), e.getToIndex());

    // TODO handle table column move
  }

  public void columnRemoved(TableColumnModelEvent e) {
    // TODO Auto-generated method stub

  }

  protected TableColumnModel createDefaultColumnModel() {
    return new DefaultTableColumnModel();
  }

  private TableModel createDefaultDataModel() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Returns the <code>TableColumnModel</code> that contains all column
   * information of this table.
   * 
   * @return the object that provides the column state of the table
   * @see #setColumnModel
   */
  public TableColumnModel getColumnModel() {
    return columnModel;
  }

  public int getColWidth(int colIndex) {
    final HeaderCell cell = tableHeader.getHeaderCell(colIndex);
    return cell.getOffsetWidth(); // TODO check model width
  }

  /**
   * Returns the <code>TableModel</code> that provides the data displayed by
   * this <code>Table</code>.
   * 
   * @return the <code>TableModel</code> that provides the data displayed by
   *         this <code>Table</code>
   */
  public TableModel getModel() {
    return dataModel;
  }

  /**
   * Returns the number of rows that can be shown in the <code>Table</code>.
   * 
   * @return the number of rows shown in the <code>Table</code>.
   */
  public int getRowCount() {
    // TODO RowSorter
    return getModel().getRowCount();
  }

  /**
   * Returns <code>true</code> if the user is allowed to rearrange columns by
   * dragging their headers, <code>false</code> otherwise. The default is
   * <code>true</code>.
   * 
   * @return the <code>reorderingAllowed</code> property
   * @see #setReorderingAllowed
   */
  public boolean isReorderingAllowed() {
    return reorderingAllowed;
  }

  public void layout() {
    ((LayoutPanel) getWidget()).layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Widget#onLoad()
   */
  protected void onLoad() {
    ((LayoutPanel) getWidget()).layout();
  }

  public void reOrderColumn(TableColumn column, int newIndex) {
    final int columnIndex = columnModel.getColumnIndex(column);
    Table.this.getColumnModel().moveColumn(columnIndex, newIndex);
  }

  public void setColumnModel(TableColumnModel columnModel) {
    if (columnModel == null) {
      throw new IllegalArgumentException("ColumnModel is null");
    }
    if (this.columnModel != columnModel) {
      if (this.columnModel != null) {
        this.columnModel.removeColumnModelListener(this);
      }
    }
    this.columnModel = columnModel;
    this.columnModel.addColumnModelListener(this);
  }

  public void setColWidth(int colIndex, int newWidth) {
    final HeaderCell cell = tableHeader.getHeaderCell(colIndex);
    cell.setWidth(newWidth);
    // TODO tableBody.setColWidth(colIndex, newWidth);
  }

  private void setModel(TableModel dataModel) {
    // if (dataModel == null) {
    // throw new IllegalArgumentException("TableModel is null");
    // }
    // if (this.dataModel != dataModel) {
    // if (this.dataModel != null) {
    // this.dataModel.removeTableModelListener(this);
    // }
    // }
    // this.dataModel = dataModel;
    // dataModel.addTableModelListener(this);

    updateFromModel();
  }

  /**
   * Sets whether the user can drag column headers to reorder columns.
   * 
   * @param columnReordering <code>true</code> if the table view should allow
   *            reordering; otherwise <code>false</code>
   * @see #getReorderingAllowed
   */
  public void setReorderingAllowed(boolean columnReordering) {
    this.reorderingAllowed = columnReordering;
  }

  /**
   * Invoked when this table's <code>TableModel</code> generates a
   * <code>TableModelEvent</code>.
   * 
   * @see org.mosaic.ui.client.event.TableModelListener#tableChanged(org.mosaic.ui.client.event.TableModelEvent)
   */
  public void tableChanged(TableModelEvent e) {
    if (e == null || e.getFromIndex() == TableModelEvent.HEADER_ROW) {
      updateFromModel();
      return;
    }

    // TODO ...

    if (e.getType() == TableModelEventType.INSERT) {
      tableRowsInserted(e);
      return;
    }

    if (e.getType() == TableModelEventType.DELETE) {
      tableRowsDeleted(e);
      return;
    }

    int modelColumn = e.getColumn();
    int start = e.getFromIndex();
    int end = e.getToIndex();

    if (modelColumn == TableModelEvent.ALL_COLUMNS) {

    }
  }

  private void tableRowsDeleted(TableModelEvent e) {
    int start = e.getFromIndex();
    int end = e.getToIndex();
    if (start < 0) {
      start = 0;
    }
    if (end < 0) {
      end = getRowCount() - 1;
    }

    // ...
  }

  private void tableRowsInserted(TableModelEvent e) {
    int start = e.getFromIndex();
    int end = e.getToIndex();
    if (start < 0) {
      start = 0;
    }
    if (end < 0) {
      end = getRowCount() - 1;
    }

    // ...
  }

  /**
   * Invoked when the whole thing changed.
   */
  private void updateFromModel() {
    tableHeader.updateFromModel();

  }
}
