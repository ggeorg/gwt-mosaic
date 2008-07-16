/* 
 * Copyright 2008 IT Mill Ltd.
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
package org.mosaic.ui.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.mosaic.core.client.UserAgent;
import org.mosaic.ui.client.Table2.TableBody.TableRow;
import org.mosaic.ui.client.event.TableColumnModelEvent;
import org.mosaic.ui.client.event.TableColumnModelListener;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.table.DefaultTableColumnModel;
import org.mosaic.ui.client.table.TableColumn;
import org.mosaic.ui.client.table.TableColumnModel;
import org.mosaic.ui.client.table.TableModel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class Table2 extends Composite implements /* Table, */ScrollListener /*
                                                                             * ,
                                                                             * ContainerResizedListener
                                                                             */, TableColumnModelListener {

  public static final String DEFAULT_STYLENAME = "mosaic-table";
  /**
   * multiple of pagelenght which component will cache when requesting more rows
   */
  private static final double CACHE_RATE = 2;
  /**
   * fraction of pageLenght which can be scrolled without making new request
   */
  private static final double CACHE_REACT_RATE = 1.5;
  
  public static final char ALIGN_CENTER = 'c';
  public static final char ALIGN_LEFT = 'b';
  public static final char ALIGN_RIGHT = 'e';
  private int firstRowInViewPort = 0;
  private int pageLength = 15;

  private String[] columnOrder;

  // TODO private ApplicationConnection client;
  private String paintableId;

  private boolean immediate;

  public enum TableSelectMode {
    NONE, SIGNLE, MULTI
  }

  private TableSelectMode selectMode = TableSelectMode.NONE;

  private final HashSet selectedRowKeys = new HashSet();

  private boolean initializedAndAttached = false;

  private final TableHead tHead = new TableHead();

  private final ScrollPanel bodyContainer = new ScrollPanel();

  private int totalRows;

  private Set collapsedColumns;

  private final RowRequestHandler rowRequestHandler;
  private TableBody tBody;
  private String width;
  private String height;
  private int firstvisible = 0;
  private boolean sortAscending;
  private String sortColumn;
  private boolean columnReordering;

  /**
   * This map contains captions and icon urls for actions like: * "33_c" ->
   * "Edit" * "33_i" -> "http://dom.com/edit.png"
   */
  private final HashMap actionMap = new HashMap();
  private String[] visibleColOrder;
  private boolean initialContentReceived = false;
  private Element scrollPositionElement;
  private final FlowPanel panel;
  private boolean enabled;
  private boolean showColHeaders;

  /** flag to indicate that table body has changed */
  private boolean isNewBody = true;

  /**
   * Stores old height for IE, that sometimes fails to return correct height for
   * container element. Then this value is used as a fallback.
   */
  private int oldAvailPixels;

  public Table2() {
    this(null, null, null);
  }

  public Table2(TableModel dm) {
    this(dm, null, null);
  }

  public Table2(TableModel dm, TableColumnModel cm) {
    this(dm, cm, null);
  }

  public Table2(TableModel dm, TableColumnModel cm, Object sm) {
    super();

    if (cm == null) {
      cm = createDefaultColumnModel();
      // autocreateColumnsFromModel = true;
    }
    setColumnModel(cm);

    // if (sm == null) {
    // sm = createDefaultSelectionModel();
    // }
    // setSelectionModel(sm);

    // if (dm == null) {
    // dm = createDefaultDataModel();
    // }
    // setModel(dm);

    bodyContainer.addScrollListener(this);
    bodyContainer.setStyleName(DEFAULT_STYLENAME + "-body");

    panel = new FlowPanel();
    panel.setStyleName(DEFAULT_STYLENAME);
    panel.add(tHead);
    panel.add(bodyContainer);

    rowRequestHandler = new RowRequestHandler();

    initWidget(panel);
  }

  /** The <code>TableColumnModel</code> of the table. */
  protected TableColumnModel columnModel;

  private void setColumnModel(TableColumnModel columnModel) {
    if (columnModel == null) {
      throw new IllegalArgumentException("ColumnModel is null");
    }
    if (columnModel != this.columnModel) {
      if (this.columnModel != null) {
        this.columnModel.removeColumnModelListener(this);
      }
    }
    this.columnModel = columnModel;
    this.columnModel.removeColumnModelListener(this);
  }

  protected TableColumnModel createDefaultColumnModel() {
    return new DefaultTableColumnModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Widget#onLoad()
   */
  protected void onLoad() {
    super.onLoad();
    
    String[] headers = new String[columnModel.getColumnCount()];
    for (int i = 0; i < columnModel.getColumnCount(); i++) {
      headers[i] = columnModel.getColumn(i).getHeaderValue().toString();
    }
                                  
    updateHeader(headers);
  }

  public String getActionCaption(String actionKey) {
    return (String) actionMap.get(actionKey + "_c");
  }

  public String getActionIcon(String actionKey) {
    return (String) actionMap.get(actionKey + "_i");
  }

  private void updateHeader(String[] strings) {
    if (strings == null) {
      return;
    }

    int visibleCols = strings.length;
    int colIndex = 0;

    visibleColOrder = new String[visibleCols];

    int i;
    for (i = 0; i < strings.length; i++) {
      final String cid = strings[i];
      visibleColOrder[colIndex] = cid;
      tHead.enableColumn(cid, colIndex);
      colIndex++;
    }

    tHead.setVisible(showColHeaders);
  }

  /**
   * Gives correct column index for given column key ("cid" in UIDL).
   * 
   * @param colKey
   * @return column index of visible columns, -1 if column not visible
   */
  private int getColIndexByKey(String colKey) {
    // return 0 if asked for rowHeaders
    if ("0".equals(colKey)) {
      return 0;
    }
    for (int i = 0; i < visibleColOrder.length; i++) {
      if (visibleColOrder[i].equals(colKey)) {
        return i;
      }
    }
    return -1;
  }

  private boolean isCollapsedColumn(String colKey) {
    if (collapsedColumns == null) {
      return false;
    }
    if (collapsedColumns.contains(colKey)) {
      return true;
    }
    return false;
  }

  private String getColKeyByIndex(int index) {
    return tHead.getHeaderCell(index).getColKey();
  }

  private void setColWidth(int colIndex, int w) {
    final HeaderCell cell = tHead.getHeaderCell(colIndex);
    cell.setWidth(w);
    tBody.setColWidth(colIndex, w);
  }

  private int getColWidth(String colKey) {
    return tHead.getHeaderCell(colKey).getWidth();
  }

  private TableRow getRenderedRowByKey(String key) {
    final Iterator it = tBody.iterator();
    TableRow r = null;
    while (it.hasNext()) {
      r = (TableRow) it.next();
      if (r.getKey().equals(key)) {
        return r;
      }
    }
    return null;
  }

  private void reOrderColumn(String columnKey, int newIndex) {

    final int oldIndex = getColIndexByKey(columnKey);

    // Change header order
    tHead.moveCell(oldIndex, newIndex);

    // Change body order
    tBody.moveCol(oldIndex, newIndex);

    /*
     * Build new columnOrder and update it to server Note that columnOrder also
     * contains collapsed columns so we cannot directly build it from cells
     * vector Loop the old columnOrder and append in order to new array unless
     * on moved columnKey. On new index also put the moved key i == index on
     * columnOrder, j == index on newOrder
     */
    final String oldKeyOnNewIndex = visibleColOrder[newIndex];
    // add back hidden rows,
    for (int i = 0; i < columnOrder.length; i++) {
      if (columnOrder[i].equals(oldKeyOnNewIndex)) {
        break; // break loop at target
      }
      if (isCollapsedColumn(columnOrder[i])) {
        newIndex++;
      }
    }
    // finally we can build the new columnOrder for server
    final String[] newOrder = new String[columnOrder.length];
    for (int i = 0, j = 0; j < newOrder.length; i++) {
      if (j == newIndex) {
        newOrder[j] = columnKey;
        j++;
      }
      if (i == columnOrder.length) {
        break;
      }
      if (columnOrder[i].equals(columnKey)) {
        continue;
      }
      newOrder[j] = columnOrder[i];
      j++;
    }
    columnOrder = newOrder;
    // also update visibleColumnOrder
    int i = 0;
    for (int j = 0; j < newOrder.length; j++) {
      final String cid = newOrder[j];
      if (!isCollapsedColumn(cid)) {
        visibleColOrder[i++] = cid;
      }
    }
    // TODO client.updateVariable(paintableId, "columnorder", columnOrder,
    // false);
  }

  protected void onAttach() {
    super.onAttach();
    if (initialContentReceived) {
      sizeInit();
    }
  }

  protected void onDetach() {
    rowRequestHandler.cancel();
    super.onDetach();
    // ensure that scrollPosElement will be detached
    if (scrollPositionElement != null) {
      final Element parent = DOM.getParent(scrollPositionElement);
      if (parent != null) {
        DOM.removeChild(parent, scrollPositionElement);
      }
    }
  }

  /**
   * Run only once when component is attached and received its initial content.
   * This function : * Syncs headers and bodys "natural widths and saves the
   * values. * Sets proper width and height * Makes deferred request to get some
   * cache rows
   */
  private void sizeInit() {
    /*
     * We will use browsers table rendering algorithm to find proper column
     * widths. If content and header take less space than available, we will
     * divide extra space relatively to each column which has not width set.
     * 
     * Overflow pixels are added to last column.
     * 
     */

    Iterator headCells = tHead.iterator();
    int i = 0;
    int totalExplicitColumnsWidths = 0;
    int total = 0;

    final int[] widths = new int[tHead.visibleCells.size()];

    if (width == null) {
      // if this is a re-init, remove old manually fixed size
      bodyContainer.setWidth("");
      tHead.setWidth("");
      super.setWidth("");
    }

    tHead.enableBrowserIntelligence();
    // first loop: collect natural widths
    while (headCells.hasNext()) {
      final HeaderCell hCell = (HeaderCell) headCells.next();
      int w = hCell.getWidth();
      if (w > 0) {
        // server has defined column width explicitly
        totalExplicitColumnsWidths += w;
      } else {
        final int hw = hCell.getOffsetWidth();
        final int cw = tBody.getColWidth(i);
        w = (hw > cw ? hw : cw) + TableBody.CELL_EXTRA_WIDTH;
      }
      widths[i] = w;
      total += w;
      i++;
    }

    tHead.disableBrowserIntelligence();

    if (height == null) {
      bodyContainer.setHeight((tBody.getRowHeight() * pageLength) + "px");
    } else {
      mySetHeight(height);
      iLayout();
    }

    if (width == null) {
      int w = total;
      w += getScrollbarWidth();
      bodyContainer.setWidth(w + "px");
      tHead.setWidth(w + "px");
      super.setWidth(w + "px");
    } else {
      if (width.indexOf("px") > 0) {
        bodyContainer.setWidth(width);
        tHead.setWidth(width);
        super.setWidth(width);
      } else if (width.indexOf("%") > 0) {
        if (!width.equals("100%")) {
          super.setWidth(width);
        }
        // contained blocks are relatively to container element
        bodyContainer.setWidth("100%");
        tHead.setWidth("100%");

      }
    }

    int availW = tBody.getAvailableWidth();
    // Hey IE, are you really sure about this?
    availW = tBody.getAvailableWidth();

    if (availW > total) {
      // natural size is smaller than available space
      final int extraSpace = availW - total;
      final int totalWidthR = total - totalExplicitColumnsWidths;
      if (totalWidthR > 0) {
        // now we will share this sum relatively to those without
        // explicit width
        headCells = tHead.iterator();
        i = 0;
        HeaderCell hCell;
        while (headCells.hasNext()) {
          hCell = (HeaderCell) headCells.next();
          if (hCell.getWidth() == -1) {
            int w = widths[i];
            final int newSpace = extraSpace * w / totalWidthR;
            w += newSpace;
            widths[i] = w;
          }
          i++;
        }
      }
    } else {
      // bodys size will be more than available and scrollbar will appear
    }

    // last loop: set possibly modified values or reset if new tBody
    i = 0;
    headCells = tHead.iterator();
    while (headCells.hasNext()) {
      final HeaderCell hCell = (HeaderCell) headCells.next();
      if (isNewBody || hCell.getWidth() == -1) {
        final int w = widths[i];
        setColWidth(i, w);
      }
      i++;
    }

    isNewBody = false;

    if (firstvisible > 0) {
      // Deferred due some Firefox oddities. IE & Safari could survive
      // without
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          bodyContainer.setScrollPosition(firstvisible * tBody.getRowHeight());
          firstRowInViewPort = firstvisible;
        }
      });
    }

    if (enabled) {
      // Do we need cache rows
      if (tBody.getLastRendered() + 1 < firstRowInViewPort + pageLength
          + CACHE_REACT_RATE * pageLength) {
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            if (totalRows - 1 > tBody.getLastRendered()) {
              // fetch cache rows
              rowRequestHandler.setReqFirstRow(tBody.getLastRendered() + 1);
              rowRequestHandler.setReqRows((int) (pageLength * CACHE_RATE));
              rowRequestHandler.deferRowFetch(1);
            }
          }
        });
      }
    }
    initializedAndAttached = true;
  }

  public void iLayout() {
    if (height != null) {
      if (height.equals("100%")) {
        /*
         * We define height in pixels with 100% not to include borders which is
         * what users usually want. So recalculate pixels via setHeight.
         */
        mySetHeight(height);
      }

      int contentH = (DOM.getElementPropertyInt(getElement(), "clientHeight") - tHead.getOffsetHeight());
      if (contentH < 0) {
        contentH = 0;
      }
      bodyContainer.setHeight(contentH + "px");
    }
  }

  private int getScrollbarWidth() {
    return bodyContainer.getOffsetWidth()
        - DOM.getElementPropertyInt(bodyContainer.getElement(), "clientWidth");
  }

  /**
   * This method has logic which rows needs to be requested from server when
   * user scrolls
   */
  public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
    if (!initializedAndAttached) {
      return;
    }
    if (!enabled) {
      bodyContainer.setScrollPosition(firstRowInViewPort * tBody.getRowHeight());
      return;
    }

    rowRequestHandler.cancel();

    // fix headers horizontal scrolling
    tHead.setHorizontalScrollPosition(scrollLeft);

    firstRowInViewPort = (int) Math.ceil(scrollTop / (double) tBody.getRowHeight());
    // ApplicationConnection.getConsole().log(
    // "At scrolltop: " + scrollTop + " At row " + firstRowInViewPort);

    int postLimit = (int) (firstRowInViewPort + pageLength + pageLength
        * CACHE_REACT_RATE);
    if (postLimit > totalRows - 1) {
      postLimit = totalRows - 1;
    }
    int preLimit = (int) (firstRowInViewPort - pageLength * CACHE_REACT_RATE);
    if (preLimit < 0) {
      preLimit = 0;
    }
    final int lastRendered = tBody.getLastRendered();
    final int firstRendered = tBody.getFirstRendered();

    if (postLimit <= lastRendered && preLimit >= firstRendered) {
      // TODO client.updateVariable(paintableId, "firstvisible",
      // firstRowInViewPort, false);
      return; // scrolled withing "non-react area"
    }

    if (firstRowInViewPort - pageLength * CACHE_RATE > lastRendered
        || firstRowInViewPort + pageLength + pageLength * CACHE_RATE < firstRendered) {
      // need a totally new set
      // ApplicationConnection.getConsole().log(
      // "Table: need a totally new set");
      rowRequestHandler.setReqFirstRow((int) (firstRowInViewPort - pageLength
          * CACHE_RATE));
      int last = firstRowInViewPort + (int) CACHE_RATE * pageLength + pageLength;
      if (last > totalRows) {
        last = totalRows - 1;
      }
      rowRequestHandler.setReqRows(last - rowRequestHandler.getReqFirstRow() + 1);
      rowRequestHandler.deferRowFetch();
      return;
    }
    if (preLimit < firstRendered) {
      // need some rows to the beginning of the rendered area
      // ApplicationConnection
      // .getConsole()
      // .log(
      // "Table: need some rows to the beginning of the rendered area");
      rowRequestHandler.setReqFirstRow((int) (firstRowInViewPort - pageLength
          * CACHE_RATE));
      rowRequestHandler.setReqRows(firstRendered - rowRequestHandler.getReqFirstRow());
      rowRequestHandler.deferRowFetch();

      return;
    }
    if (postLimit > lastRendered) {
      // need some rows to the end of the rendered area
      // ApplicationConnection.getConsole().log(
      // "need some rows to the end of the rendered area");
      rowRequestHandler.setReqFirstRow(lastRendered + 1);
      rowRequestHandler.setReqRows((int) ((firstRowInViewPort + pageLength + pageLength
          * CACHE_RATE) - lastRendered));
      rowRequestHandler.deferRowFetch();
    }

  }

  private void announceScrollPosition() {
    if (scrollPositionElement == null) {
      scrollPositionElement = DOM.createDiv();
      DOM.setElementProperty(scrollPositionElement, "className", "i-table-scrollposition");
      DOM.appendChild(getElement(), scrollPositionElement);
    }

    DOM.setStyleAttribute(scrollPositionElement, "position", "absolute");
    DOM.setStyleAttribute(scrollPositionElement, "marginLeft",
        (DOM.getElementPropertyInt(getElement(), "offsetWidth") / 2 - 80) + "px");
    DOM.setStyleAttribute(scrollPositionElement, "marginTop",
        -(DOM.getElementPropertyInt(getElement(), "offsetHeight") - 2) + "px");

    int last = (firstRowInViewPort + pageLength);
    if (last > totalRows) {
      last = totalRows;
    }
    DOM.setInnerHTML(scrollPositionElement, "<span>" + firstRowInViewPort + " &ndash; "
        + last + "..." + "</span>");
    DOM.setStyleAttribute(scrollPositionElement, "display", "block");
  }

  private void hideScrollPositionAnnotation() {
    if (scrollPositionElement != null) {
      DOM.setStyleAttribute(scrollPositionElement, "display", "none");
    }
  }

  private class RowRequestHandler extends Timer {

    private int reqFirstRow = 0;
    private int reqRows = 0;

    public void deferRowFetch() {
      deferRowFetch(250);
    }

    public void deferRowFetch(int msec) {
      if (reqRows > 0 && reqFirstRow < totalRows) {
        schedule(msec);

        // tell scroll position to user if currently "visible" rows are
        // not rendered
        if ((firstRowInViewPort + pageLength > tBody.getLastRendered())
            || (firstRowInViewPort < tBody.getFirstRendered())) {
          announceScrollPosition();
        } else {
          hideScrollPositionAnnotation();
        }
      }
    }

    public void setReqFirstRow(int reqFirstRow) {
      if (reqFirstRow < 0) {
        reqFirstRow = 0;
      } else if (reqFirstRow >= totalRows) {
        reqFirstRow = totalRows - 1;
      }
      this.reqFirstRow = reqFirstRow;
    }

    public void setReqRows(int reqRows) {
      this.reqRows = reqRows;
    }

    public void run() {
      // ApplicationConnection.getConsole().log(
      // "Getting " + reqRows + " rows from " + reqFirstRow);

      int firstToBeRendered = tBody.firstRendered;
      if (reqFirstRow < firstToBeRendered) {
        firstToBeRendered = reqFirstRow;
      } else if (firstRowInViewPort - (int) (CACHE_RATE * pageLength) > firstToBeRendered) {
        firstToBeRendered = firstRowInViewPort - (int) (CACHE_RATE * pageLength);
        if (firstToBeRendered < 0) {
          firstToBeRendered = 0;
        }
      }

      int lastToBeRendered = tBody.lastRendered;

      if (reqFirstRow + reqRows - 1 > lastToBeRendered) {
        lastToBeRendered = reqFirstRow + reqRows - 1;
      } else if (firstRowInViewPort + pageLength + pageLength * CACHE_RATE < lastToBeRendered) {
        lastToBeRendered = (firstRowInViewPort + pageLength + (int) (pageLength * CACHE_RATE));
        if (lastToBeRendered >= totalRows) {
          lastToBeRendered = totalRows - 1;
        }
      }

      // TODO client.updateVariable(paintableId, "firstToBeRendered",
      // firstToBeRendered, false);
      //
      // client.updateVariable(paintableId, "lastToBeRendered",
      // lastToBeRendered, false);
      //
      // client.updateVariable(paintableId, "firstvisible", firstRowInViewPort,
      // false);
      // client.updateVariable(paintableId, "reqfirstrow", reqFirstRow, false);
      // client.updateVariable(paintableId, "reqrows", reqRows, true);
    }

    public int getReqFirstRow() {
      return reqFirstRow;
    }

    public int getReqRows() {
      return reqRows;
    }

    /**
     * Sends request to refresh content at this position.
     */
    public void refreshContent() {
      int first = (int) (firstRowInViewPort - pageLength * CACHE_RATE);
      int reqRows = (int) (2 * pageLength * CACHE_RATE + pageLength);
      if (first < 0) {
        reqRows = reqRows + first;
        first = 0;
      }
      setReqFirstRow(first);
      setReqRows(reqRows);
      run();
    }
  }

  public class HeaderCell extends Widget {

    private static final int DRAG_WIDGET_WIDTH = 4;

    private static final int MINIMUM_COL_WIDTH = 20;

    Element td = DOM.createTD();

    Element captionContainer = DOM.createDiv();

    Element colResizeWidget = DOM.createDiv();

    Element floatingCopyOfHeaderCell;

    private boolean sortable = false;
    private final String cid;
    private boolean dragging;

    private int dragStartX;
    private int colIndex;
    private int originalWidth;

    private boolean isResizing;

    private int headerX;

    private boolean moved;

    private int closestSlot;

    private int width = -1;

    private char align = ALIGN_LEFT;

    public void setSortable(boolean b) {
      sortable = b;
    }

    public HeaderCell(String colId, String headerText) {
      cid = colId;

      DOM.setElementProperty(colResizeWidget, "className", DEFAULT_STYLENAME + "-resizer");
      DOM.setStyleAttribute(colResizeWidget, "width", DRAG_WIDGET_WIDTH + "px");
      DOM.sinkEvents(colResizeWidget, Event.MOUSEEVENTS);

      setText(headerText);

      DOM.appendChild(td, colResizeWidget);

      DOM.setElementProperty(captionContainer, "className", DEFAULT_STYLENAME
          + "-caption-container");

      // ensure no clipping initially (problem on column additions)
      DOM.setStyleAttribute(captionContainer, "overflow", "visible");

      DOM.sinkEvents(captionContainer, Event.MOUSEEVENTS);

      DOM.appendChild(td, captionContainer);

      DOM.sinkEvents(td, Event.MOUSEEVENTS);

      setElement(td);
    }

    public void setWidth(int w) {
      if (width == -1) {
        // go to default mode, clip content if necessary
        DOM.setStyleAttribute(captionContainer, "overflow", "");
      }
      width = w;
      DOM.setStyleAttribute(captionContainer, "width", (w - DRAG_WIDGET_WIDTH - 4) + "px");
      setWidth(w + "px");
    }

    public int getWidth() {
      return width;
    }

    public void setText(String headerText) {
      DOM.setInnerHTML(captionContainer, headerText);
    }

    public String getColKey() {
      return cid;
    }

    private void setSorted(boolean sorted) {
      if (sorted) {
        if (sortAscending) {
          this.setStyleName(DEFAULT_STYLENAME + "-header-cell-asc");
        } else {
          this.setStyleName(DEFAULT_STYLENAME + "-header-cell-desc");
        }
      } else {
        this.setStyleName(DEFAULT_STYLENAME + "-header-cell");
      }
    }

    /**
     * Handle column reordering.
     */
    public void onBrowserEvent(Event event) {
      if (enabled) {
        if (isResizing || DOM.compare(DOM.eventGetTarget(event), colResizeWidget)) {
          onResizeEvent(event);
        } else {
          handleCaptionEvent(event);
        }
      }
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

    private void updateFloatingCopysPosition(int x, int y) {
      x -= DOM.getElementPropertyInt(floatingCopyOfHeaderCell, "offsetWidth") / 2;
      DOM.setStyleAttribute(floatingCopyOfHeaderCell, "left", x + "px");
      if (y > 0) {
        DOM.setStyleAttribute(floatingCopyOfHeaderCell, "top", (y + 7) + "px");
      }
    }

    private void hideFloatingCopy() {
      DOM.removeChild(RootPanel.get().getElement(), floatingCopyOfHeaderCell);
      floatingCopyOfHeaderCell = null;
    }

    protected void handleCaptionEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
          // TODO ApplicationConnection.getConsole().log("HeaderCaption: mouse
          // down");
          if (columnReordering) {
            dragging = true;
            moved = false;
            colIndex = getColIndexByKey(cid);
            DOM.setCapture(getElement());
            headerX = tHead.getAbsoluteLeft();
            // TODO ApplicationConnection.getConsole().log(
            // "HeaderCaption: Caption set to capture mouse events");
            DOM.eventPreventDefault(event); // prevent selecting text
          }
          break;
        case Event.ONMOUSEUP:
          // TODO ApplicationConnection.getConsole().log("HeaderCaption:
          // mouseUP");
          if (columnReordering) {
            dragging = false;
            DOM.releaseCapture(getElement());
            // TODO ApplicationConnection.getConsole().log(
            // "HeaderCaption: Stopped column reordering");
            if (moved) {
              hideFloatingCopy();
              tHead.removeSlotFocus();
              if (closestSlot != colIndex && closestSlot != (colIndex + 1)) {
                if (closestSlot > colIndex) {
                  reOrderColumn(cid, closestSlot - 1);
                } else {
                  reOrderColumn(cid, closestSlot);
                }
              }
            }
          }

          if (!moved) {
            // mouse event was a click to header -> sort column
            if (sortable) {
              if (sortColumn.equals(cid)) {
                // just toggle order
                // TODO client.updateVariable(paintableId, "sortascending",
                // !sortAscending, false);
              } else {
                // set table scrolled by this column
                // TODO client.updateVariable(paintableId, "sortcolumn", cid,
                // false);
              }
              // get also cache columns at the same request
              bodyContainer.setScrollPosition(0);
              firstvisible = 0;
              rowRequestHandler.setReqFirstRow(0);
              rowRequestHandler.setReqRows((int) (2 * pageLength * CACHE_RATE + pageLength));
              rowRequestHandler.deferRowFetch();
            }
            break;
          }
          break;
        case Event.ONMOUSEMOVE:
          if (dragging) {
            // TODO ApplicationConnection.getConsole().log(
            // "HeaderCaption: Dragging column, optimal index...");
            if (!moved) {
              createFloatingCopy();
              moved = true;
            }
            final int x = DOM.eventGetClientX(event)
                + DOM.getElementPropertyInt(tHead.hTableWrapper, "scrollLeft");
            int slotX = headerX;
            closestSlot = colIndex;
            int closestDistance = -1;
            int start = 0;
            final int visibleCellCount = tHead.getVisibleCellCount();
            for (int i = start; i <= visibleCellCount; i++) {
              if (i > 0) {
                final String colKey = getColKeyByIndex(i - 1);
                slotX += getColWidth(colKey);
              }
              final int dist = Math.abs(x - slotX);
              if (closestDistance == -1 || dist < closestDistance) {
                closestDistance = dist;
                closestSlot = i;
              }
            }
            tHead.focusSlot(closestSlot);

            updateFloatingCopysPosition(DOM.eventGetClientX(event), -1);
            // TODO ApplicationConnection.getConsole().log("" + closestSlot);
          }
          break;
        default:
          break;
      }
    }

    private void onResizeEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
          isResizing = true;
          DOM.setCapture(getElement());
          dragStartX = DOM.eventGetClientX(event);
          colIndex = getColIndexByKey(cid);
          originalWidth = getWidth();
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

            int newWidth = originalWidth + deltaX;
            if (newWidth < MINIMUM_COL_WIDTH) {
              newWidth = MINIMUM_COL_WIDTH;
            }
            setColWidth(colIndex, newWidth);
          }
          break;
        default:
          break;
      }
    }

    public String getCaption() {
      return DOM.getInnerText(captionContainer);
    }

    public boolean isEnabled() {
      return getParent() != null;
    }

    public void setAlign(char c) {
      if (align != c) {
        switch (c) {
          case ALIGN_CENTER:
            DOM.setStyleAttribute(captionContainer, "textAlign", "center");
            break;
          case ALIGN_RIGHT:
            DOM.setStyleAttribute(captionContainer, "textAlign", "right");
            break;
          default:
            DOM.setStyleAttribute(captionContainer, "textAlign", "");
            break;
        }
      }
      align = c;
    }

    public char getAlign() {
      return align;
    }

  }

  public class TableHead extends Panel /* implements ActionOwner */{

    private static final int WRAPPER_WIDTH = 9000;

    Vector visibleCells = new Vector();

    HashMap availableCells = new HashMap();

    Element div = DOM.createDiv();
    Element hTableWrapper = DOM.createDiv();
    Element hTableContainer = DOM.createDiv();
    Element table = DOM.createTable();
    Element headerTableBody = DOM.createTBody();
    Element tr = DOM.createTR();

    private final Element columnSelector = DOM.createDiv();

    private int focusedSlot = -1;

    public TableHead() {
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
    
    protected void onLoad() {
      super.onLoad();
      
      Iterator<TableColumn> it = columnModel.getColumns();
      HashSet<String> updated = new HashSet<String>();
      while(it.hasNext()) {
        final TableColumn col = it.next();
        final String cid = col.getHeaderValue().toString();
        updated.add(cid);
        HeaderCell c = getHeaderCell(cid);
        if (c == null) {
          c = new HeaderCell(cid, col.getHeaderValue().toString());
          availableCells.put(cid, c);
          if (initializedAndAttached) {
            // we will need a column width recalculation
            initializedAndAttached = false;
            initialContentReceived = false;
            isNewBody = true;
          }
        } else {
          c.setText(col.getHeaderValue().toString());
        }
        
        // TODO various
      }
      
      // check for orphaned header cells
      Iterator it2 = availableCells.keySet().iterator();
      while(it2.hasNext()) {
        String cid = (String) it2.next();
        if (!updated.contains(cid)) {
          removeCell(cid);
          it.remove();
        }
      }
    }

    public void enableColumn(String cid, int index) {
      final HeaderCell c = getHeaderCell(cid);
      if (!c.isEnabled()) {
        setHeaderCell(index, c);
        if (c.getWidth() == -1) {
          if (initializedAndAttached) {
            // column is not drawn before,
            // we will need a column width recalculation
            initializedAndAttached = false;
            initialContentReceived = false;
            isNewBody = true;
          }
        }
      }
    }

    public int getVisibleCellCount() {
      return visibleCells.size();
    }

    public void setHorizontalScrollPosition(int scrollLeft) {
      DOM.setElementPropertyInt(hTableWrapper, "scrollLeft", scrollLeft);
    }

    public void setColumnCollapsingAllowed(boolean cc) {
      if (cc) {
        DOM.setStyleAttribute(columnSelector, "display", "block");
      } else {
        DOM.setStyleAttribute(columnSelector, "display", "none");
      }
    }

    public void disableBrowserIntelligence() {
      DOM.setStyleAttribute(hTableContainer, "width", WRAPPER_WIDTH + "px");
    }

    public void enableBrowserIntelligence() {
      DOM.setStyleAttribute(hTableContainer, "width", "");
    }

    public void setHeaderCell(int index, HeaderCell cell) {
      if (index < visibleCells.size()) {
        // insert to right slot
        DOM.insertChild(tr, cell.getElement(), index);
        adopt(cell);
        visibleCells.insertElementAt(cell, index);

      } else if (index == visibleCells.size()) {
        // simply append
        DOM.appendChild(tr, cell.getElement());
        adopt(cell);
        visibleCells.add(cell);
      } else {
        throw new RuntimeException("Header cells must be appended in order");
      }
    }

    public HeaderCell getHeaderCell(int index) {
      if (index < visibleCells.size()) {
        return (HeaderCell) visibleCells.get(index);
      } else {
        return null;
      }
    }

    /**
     * Get's HeaderCell by it's column Key.
     * 
     * Note that this returns HeaderCell even if it is currently collapsed.
     * 
     * @param cid Column key of accessed HeaderCell
     * @return HeaderCell
     */
    public HeaderCell getHeaderCell(String cid) {
      return (HeaderCell) availableCells.get(cid);
    }

    public void moveCell(int oldIndex, int newIndex) {
      final HeaderCell hCell = getHeaderCell(oldIndex);
      final Element cell = hCell.getElement();

      visibleCells.remove(oldIndex);
      DOM.removeChild(tr, cell);

      DOM.insertChild(tr, cell, newIndex);
      visibleCells.insertElementAt(hCell, newIndex);
    }

    public Iterator iterator() {
      return visibleCells.iterator();
    }

    public boolean remove(Widget w) {
      if (visibleCells.contains(w)) {
        visibleCells.remove(w);
        orphan(w);
        DOM.removeChild(DOM.getParent(w.getElement()), w.getElement());
        return true;
      }
      return false;
    }

    public void removeCell(String colKey) {
      final HeaderCell c = getHeaderCell(colKey);
      remove(c);
    }

    private void focusSlot(int index) {
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

    public void onBrowserEvent(Event event) {
      if (enabled) {
        if (DOM.compare(DOM.eventGetTarget(event), columnSelector)) {
          final int left = DOM.getAbsoluteLeft(columnSelector);
          final int top = DOM.getAbsoluteTop(columnSelector)
              + DOM.getElementPropertyInt(columnSelector, "offsetHeight");
          // TODO client.getContextMenu().showAt(this, left, top);
        }
      }
    }

    public String getPaintableId() {
      return paintableId;
    }

    /**
     * Returns column alignments for visible columns
     */
    public char[] getColumnAlignments() {
      final Iterator it = visibleCells.iterator();
      final char[] aligns = new char[visibleCells.size()];
      int colIndex = 0;
      while (it.hasNext()) {
        aligns[colIndex++] = ((HeaderCell) it.next()).getAlign();
      }
      return aligns;
    }

  }

  /**
   * This Panel can only contain TableRow type of widgets. This "simulates" very
   * large table, keeping spacers which take room of unrendered rows.
   */
  public class TableBody extends Panel {

    public static final int CELL_EXTRA_WIDTH = 20;

    public static final int DEFAULT_ROW_HEIGHT = 24;

    /**
     * Amount of padding inside one table cell (this is reduced from the
     * "cellContent" element's width). You may override this in your own
     * widgetset.
     */
    public static final int CELL_CONTENT_PADDING = 8;

    private int rowHeight = -1;

    private final List renderedRows = new Vector();

    private boolean initDone = false;

    Element preSpacer = DOM.createDiv();
    Element postSpacer = DOM.createDiv();

    Element container = DOM.createDiv();

    Element tBody = DOM.createTBody();
    Element table = DOM.createTable();

    private int firstRendered;

    private int lastRendered;

    private char[] aligns;

    TableBody() {
      constructDOM();
      setElement(container);
    }

    private void constructDOM() {
      DOM.setElementProperty(table, "className", DEFAULT_STYLENAME + "-table");
      DOM.setElementProperty(preSpacer, "className", DEFAULT_STYLENAME + "-row-spacer");
      DOM.setElementProperty(postSpacer, "className", DEFAULT_STYLENAME + "-row-spacer");

      DOM.appendChild(table, tBody);
      DOM.appendChild(container, preSpacer);
      DOM.appendChild(container, table);
      DOM.appendChild(container, postSpacer);

    }

    public int getAvailableWidth() {
      return DOM.getElementPropertyInt(preSpacer, "offsetWidth");
    }

    private void addRowBeforeFirstRendered(TableRow row) {
      TableRow first = null;
      if (renderedRows.size() > 0) {
        first = (TableRow) renderedRows.get(0);
      }
      if (first != null && first.getStyleName().indexOf("-odd") == -1) {
        row.setStyleName(DEFAULT_STYLENAME + "-row-odd");
      }
      if (row.isSelected()) {
        row.addStyleName("i-selected");
      }
      DOM.insertChild(tBody, row.getElement(), 0);
      adopt(row);
      renderedRows.add(0, row);
    }

    private void addRow(TableRow row) {
      TableRow last = null;
      if (renderedRows.size() > 0) {
        last = (TableRow) renderedRows.get(renderedRows.size() - 1);
      }
      if (last != null && last.getStyleName().indexOf("-odd") == -1) {
        row.setStyleName(DEFAULT_STYLENAME + "-row-odd");
      }
      if (row.isSelected()) {
        row.addStyleName("i-selected");
      }
      DOM.appendChild(tBody, row.getElement());
      adopt(row);
      renderedRows.add(row);
    }

    public Iterator iterator() {
      return renderedRows.iterator();
    }

    /**
     * @return false if couldn't remove row
     */
    public boolean unlinkRow(boolean fromBeginning) {
      if (lastRendered - firstRendered < 0) {
        return false;
      }
      int index;
      if (fromBeginning) {
        index = 0;
        firstRendered++;
      } else {
        index = renderedRows.size() - 1;
        lastRendered--;
      }
      final TableRow toBeRemoved = (TableRow) renderedRows.get(index);
      // TODO client.unregisterChildPaintables(toBeRemoved);
      DOM.removeChild(tBody, toBeRemoved.getElement());
      orphan(toBeRemoved);
      renderedRows.remove(index);
      fixSpacers();
      return true;
    }

    public boolean remove(Widget w) {
      throw new UnsupportedOperationException();
    }

    protected void onAttach() {
      super.onAttach();
      setContainerHeight();
    }

    /**
     * Fix container blocks height according to totalRows to avoid "bouncing"
     * when scrolling
     */
    private void setContainerHeight() {
      fixSpacers();
      DOM.setStyleAttribute(container, "height", totalRows * getRowHeight() + "px");
    }

    private void fixSpacers() {
      int prepx = getRowHeight() * firstRendered;
      if (prepx < 0) {
        prepx = 0;
      }
      DOM.setStyleAttribute(preSpacer, "height", prepx + "px");
      int postpx = getRowHeight() * (totalRows - 1 - lastRendered);
      if (postpx < 0) {
        postpx = 0;
      }
      DOM.setStyleAttribute(postSpacer, "height", postpx + "px");
    }

    public int getRowHeight() {
      if (initDone) {
        return rowHeight;
      } else {
        if (DOM.getChildCount(tBody) > 0) {
          rowHeight = DOM.getElementPropertyInt(tBody, "offsetHeight")
              / DOM.getChildCount(tBody);
        } else {
          return DEFAULT_ROW_HEIGHT;
        }
        initDone = true;
        return rowHeight;
      }
    }

    public int getColWidth(int i) {
      if (initDone) {
        final Element e = DOM.getChild(DOM.getChild(tBody, 0), i);
        return DOM.getElementPropertyInt(e, "offsetWidth");
      } else {
        return 0;
      }
    }

    public void setColWidth(int colIndex, int w) {
      final int rows = DOM.getChildCount(tBody);
      for (int i = 0; i < rows; i++) {
        final Element cell = DOM.getChild(DOM.getChild(tBody, i), colIndex);
        DOM.setStyleAttribute(DOM.getFirstChild(cell), "width",
            (w - CELL_CONTENT_PADDING) + "px");
        DOM.setStyleAttribute(cell, "width", w + "px");
      }
    }

    public int getLastRendered() {
      return lastRendered;
    }

    public int getFirstRendered() {
      return firstRendered;
    }

    public void moveCol(int oldIndex, int newIndex) {

      // loop all rows and move given index to its new place
      final Iterator rows = iterator();
      while (rows.hasNext()) {
        final TableRow row = (TableRow) rows.next();

        final Element td = DOM.getChild(row.getElement(), oldIndex);
        DOM.removeChild(row.getElement(), td);

        DOM.insertChild(row.getElement(), td, newIndex);

      }

    }

    public class TableRow extends Panel /* implements ActionOwner */{

      Vector childWidgets = new Vector();
      private boolean selected = false;
      private final int rowKey;

      private String[] actionKeys = null;

      private TableRow(int rowKey) {
        this.rowKey = rowKey;
        setElement(DOM.createElement("tr"));
        DOM.sinkEvents(getElement(), Event.ONCLICK);
        // TODO attachContextMenuEvent(getElement());
        setStyleName(DEFAULT_STYLENAME + "-row");
      }

      protected void onDetach() {
        // TODO Util.removeContextMenuEvent(getElement());
        super.onDetach();
      }

      public String getKey() {
        return String.valueOf(rowKey);
      }

      public void addCell(String text, char align) {
        // String only content is optimized by not using Label widget
        final Element td = DOM.createTD();
        final Element container = DOM.createDiv();
        DOM.setElementProperty(container, "className", DEFAULT_STYLENAME
            + "-cell-content");
        DOM.setInnerHTML(container, text);
        if (align != ALIGN_LEFT) {
          switch (align) {
            case ALIGN_CENTER:
              DOM.setStyleAttribute(container, "textAlign", "center");
              break;
            case ALIGN_RIGHT:
            default:
              DOM.setStyleAttribute(container, "textAlign", "right");
              break;
          }
        }
        DOM.appendChild(td, container);
        DOM.appendChild(getElement(), td);
      }

      public void addCell(Widget w, char align) {
        final Element td = DOM.createTD();
        final Element container = DOM.createDiv();
        DOM.setElementProperty(container, "className", DEFAULT_STYLENAME
            + "-cell-content");
        // TODO most components work with this, but not all (e.g.
        // Select)
        // Old comment: make widget cells respect align.
        // text-align:center for IE, margin: auto for others
        if (align != ALIGN_LEFT) {
          switch (align) {
            case ALIGN_CENTER:
              DOM.setStyleAttribute(container, "textAlign", "center");
              break;
            case ALIGN_RIGHT:
            default:
              DOM.setStyleAttribute(container, "textAlign", "right");
              break;
          }
        }
        DOM.appendChild(td, container);
        DOM.appendChild(getElement(), td);
        DOM.appendChild(container, w.getElement());
        adopt(w);
        childWidgets.add(w);
      }

      public Iterator iterator() {
        return childWidgets.iterator();
      }

      public boolean remove(Widget w) {
        // TODO Auto-generated method stub
        return false;
      }

      /*
       * React on click that occur on content cells only
       */
      public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
          case Event.ONCLICK:
            final Element tdOrTr = DOM.getParent(DOM.eventGetTarget(event));
            if (DOM.compare(getElement(), tdOrTr)
                || DOM.compare(getElement(), DOM.getParent(tdOrTr))) {
              if (selectMode != TableSelectMode.NONE) {
                toggleSelection();
                // client.updateVariable(paintableId, "selected",
                // selectedRowKeys.toArray(),
                // immediate);
              }
            }
            break;

          default:
            break;
        }
        super.onBrowserEvent(event);
      }

      public void showContextMenu(Event event) {
        // ApplicationConnection.getConsole().log("Context menu");
        if (enabled && actionKeys != null) {
          int left = DOM.eventGetClientX(event);
          int top = DOM.eventGetClientY(event);
          top += Window.getScrollTop();
          left += Window.getScrollLeft();
          // TODO client.getContextMenu().showAt(this, left, top);
        }
      }

      public boolean isSelected() {
        return selected;
      }

      private void toggleSelection() {
        selected = !selected;
        if (selected) {
          if (selectMode == TableSelectMode.SIGNLE) {
            deselectAll();
          }
          selectedRowKeys.add(String.valueOf(rowKey));
          addStyleName("i-selected");
        } else {
          selectedRowKeys.remove(String.valueOf(rowKey));
          removeStyleName("i-selected");
        }
      }

      public String getPaintableId() {
        return paintableId;
      }
    }
  }

  public void deselectAll() {
    final Object[] keys = selectedRowKeys.toArray();
    for (int i = 0; i < keys.length; i++) {
      final TableRow row = getRenderedRowByKey((String) keys[i]);
      if (row != null && row.isSelected()) {
        row.toggleSelection();
      }
    }
    // still ensure all selects are removed from (not necessary rendered)
    selectedRowKeys.clear();

  }

  public void add(Widget w) {
    throw new UnsupportedOperationException(
        "ITable can contain only rows created by itself.");
  }

  public void clear() {
    panel.clear();
  }

  public Iterator iterator() {
    return panel.iterator();
  }

  public boolean remove(Widget w) {
    return panel.remove(w);
  }

  public void mySetHeight(String height) {
    // workaround very common 100% height problem - extract borders
    if (height.equals("100%")) {
      final int borders = getBorderSpace();
      final Element parentElem = DOM.getParent(getElement());

      // put table away from flow for a moment
      DOM.setStyleAttribute(getElement(), "position", "absolute");
      // get containers natural space for table
      int availPixels = DOM.getElementPropertyInt(parentElem, "offsetHeight");
      if (UserAgent.isIE6()) {
        if (availPixels == 0) {
          // In complex layouts IE sometimes rather randomly returns 0
          // although container really has height. Use old value if
          // one exits.
          if (oldAvailPixels > 0) {
            availPixels = oldAvailPixels;
          }
        } else {
          oldAvailPixels = availPixels;
        }
      }
      // put table back to flow
      DOM.setStyleAttribute(getElement(), "position", "static");
      // set 100% height with borders
      int pixelSize = (availPixels - borders);
      if (pixelSize < 0) {
        pixelSize = 0;
      }
      super.setHeight(pixelSize + "px");
    } else {
      // normally height don't include borders
      super.setHeight(height);
    }
  }

  private int getBorderSpace() {
    final Element el = getElement();
    return DOM.getElementPropertyInt(el, "offsetHeight")
        - DOM.getElementPropertyInt(el, "clientHeight");
  }

  public void setWidth(String width) {
    // NOP size handled internally
  }

  public void setHeight(String height) {
    // NOP size handled internally
  }

  /*
   * Overridden due Table might not survive of visibility change (scroll pos
   * lost). Example ITabPanel just set contained components invisible and back
   * when changing tabs.
   */
  public void setVisible(boolean visible) {
    if (isVisible() != visible) {
      super.setVisible(visible);
      if (initializedAndAttached) {
        if (visible) {
          DeferredCommand.addCommand(new Command() {
            public void execute() {
              bodyContainer.setScrollPosition(firstRowInViewPort * tBody.getRowHeight());
            }
          });
        }
      }
    }
  }

  /**
   * Invoked when a column is repositioned.
   * 
   * @see org.mosaic.ui.client.event.TableColumnModelListener#columnMoved(org.mosaic.ui.client.event.TableColumnModelEvent)
   */
  public void columnMoved(TableColumnModelEvent e) {
    // TODO Auto-generated method stub

  }

  /**
   * Invoked when a column is added to the table column model.
   * 
   * @see org.mosaic.ui.client.event.TableColumnModelListener#columnAdded(org.mosaic.ui.client.event.TableColumnModelEvent)
   */
  public void columnAdded(TableColumnModelEvent e) {
    // TODO Auto-generated method stub

  }

  /**
   * Invoked when a column is removed from the table column model.
   * 
   * @see org.mosaic.ui.client.event.TableColumnModelListener#columnRemoved(org.mosaic.ui.client.event.TableColumnModelEvent)
   */
  public void columnRemoved(TableColumnModelEvent e) {
    // TODO Auto-generated method stub

  }

}
