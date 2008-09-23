/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.ui.client.treetable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.DecoratedFastTreeItem;
import com.google.gwt.widgetideas.client.overrides.DOMHelper;
import com.google.gwt.widgetideas.table.client.overrides.OverrideDOM;

/**
 * An item that can be contained within a
 * {@link com.google.gwt.widgetideas.client.FastTree}.
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.TreeExample}
 */

public class FastTreeTableItem extends Widget implements HasHTML,
    HasFastTreeTableItems {
  private static final String STYLENAME_SELECTED = "selected";

  // TODO(ECC) change states to enums and move style names to FastTreeTable
  // where
  // they below.
  private static final int TREE_NODE_LEAF = 1;
  private static final int TREE_NODE_INTERIOR_NEVER_OPENED = 2;
  private static final int TREE_NODE_INTERIOR_OPEN = 3;
  private static final int TREE_NODE_INTERIOR_CLOSED = 4;
  private static final String STYLENAME_CHILDREN = "gwt-FastTreeTableItem-children";
  private static final String STYLENAME_LEAF_DEFAULT = "gwt-FastTreeTableItem gwt-FastTreeTableItem-leaf";
  private static final String STYLENAME_OPEN = "open";
  private static final String STYLENAME_CLOSED = "closed";
  private static final String STYLENAME_LEAF = "leaf";

  private static final String STYLENAME_CONTENT = "treeItemContent";
  /**
   * The base tree item element that will be cloned.
   */
  private static Element TREE_LEAF;

  /**
   * Static constructor to set up clonable elements.
   */
  static {
    if (GWT.isClient()) {
      // Create the base element that will be cloned
      TREE_LEAF = DOM.createDiv();
      // leaf contents.
      setStyleName(TREE_LEAF, STYLENAME_LEAF_DEFAULT);
      Element content = DOM.createDiv();
      setStyleName(content, STYLENAME_CONTENT);
      DOM.appendChild(TREE_LEAF, content);
    }
  }

  private int state = TREE_NODE_LEAF;
  private ArrayList<FastTreeTableItem> children;
  private Element contentElem;
  // FixedWidthGrid childTable;
  private FastTreeTableItem parent;
  private FastTreeTable treeTable;
  private Widget widget;

  private Object userObject;

  /**
   * Sets the user-defined object associated with this item.
   * 
   * @param userObject the item's user-defined object
   */
  public void setUserObject(Object userObject) {
    this.userObject = userObject;
  }

  /**
   * Gets the user-defined object associated with this item.
   * 
   * @return the item's user-defined object
   */
  public Object getUserObject() {
    return userObject;
  }

  /**
   * Creates an empty tree item.
   */
  public FastTreeTableItem() {
    Element elem = createLeafElement();
    setElement(elem);
  }

  /**
   * Constructs a tree item with the given HTML.
   * 
   * @param html the item's HTML
   */
  public FastTreeTableItem(String html) {
    this();
    DOM.setInnerHTML(getElementToAttach(), html);
  }

  /**
   * Constructs a tree item with the given <code>Widget</code>.
   * 
   * @param widget the item's widget
   */
  public FastTreeTableItem(Widget widget) {
    this();
    addWidget(widget);
  }

  /**
   * This constructor is only for use by {@link DecoratedFastTreeItem}.
   * 
   * @param element element
   */
  FastTreeTableItem(Element element) {
    setElement(element);
  }

  public void addItem(FastTreeTableItem item) {
    // Detach item from existing parent.
    if ((item.getParentItem() != null) || (item.getTreeTable() != null)) {
      item.remove();
    }
    if (isLeafNode()) {
      becomeInteriorNode();
    }
    if (children == null) {
      // Never had children.
      children = new ArrayList<FastTreeTableItem>();
    }
    // Logical attach.
    item.setParentItem(this);
    children.add(item);
    item.setDepth(getDepth() + 1);

    // Physical attach.
    if (state != TREE_NODE_INTERIOR_NEVER_OPENED) {
      // DOM.appendChild(childElems, item.getElement());
      insertItem(item, getChildCount());
    }

    // Adopt.
    if (treeTable != null) {
      item.setTreeTable(treeTable);
    }
  }

  /**
   * Updates table rows to include children.
   * 
   * @param item the item to insert
   * @param r the row to insert the item to
   */
  private void insertItem(FastTreeTableItem item, int r) {
    // childTable.insertRow(r);
    // childTable.setWidget(r, treeTable.getTreeColumn(), item);
    final Element tr = getElement().getParentElement().getParentElement().cast();
    r += OverrideDOM.getRowIndex(tr);
    treeTable.insertRow(r);
    treeTable.setWidget(r, treeTable.getTreeColumn(), item);
    int d = item.getDepth();
    if (d != 0) {
      DOM.setStyleAttribute(item.getElement(), "marginLeft", (d * 10) + "px");
    }
    treeTable.render(item, r);
  }

  public FastTreeTableItem addItem(String itemText) {
    FastTreeTableItem ret = new FastTreeTableItem(itemText);
    addItem(ret);
    return ret;
  }

  public FastTreeTableItem addItem(Widget widget) {
    return addItem(widget, null);
  }
  
  public FastTreeTableItem addItem(Widget widget, Object userObject) {
    FastTreeTableItem ret = new FastTreeTableItem(widget);
    ret.setUserObject(userObject);
    addItem(ret);
    return ret;
  }

  /**
   * Become an interior node.
   */
  public void becomeInteriorNode() {
    if (!isInteriorNode()) {
      state = TREE_NODE_INTERIOR_NEVER_OPENED;

      Element control = DOM.createDiv();
      setStyleName(control, STYLENAME_CLOSED);
      DOM.appendChild(control, contentElem);
      convertElementToInteriorNode(control);
    }
  }

  public FastTreeTableItem getChild(int index) {
    if ((index < 0) || (index >= getChildCount())) {
      throw new IndexOutOfBoundsException("No child at index " + index);
    }
    return children.get(index);
  }

  public int getChildCount() {
    if (children == null) {
      return 0;
    }
    return children.size();
  }

  public int getChildIndex(FastTreeTableItem child) {
    if (children == null) {
      return -1;
    }
    return children.indexOf(child);
  }

  /**
   * Returns the width of the control open/close image. Must be overridden if
   * the TreeItem is using a control image that is <i>not</i> 16 pixels wide.
   * 
   * @return the width of the control image
   */
  public int getControlImageWidth() {
    return 16;
  }

  public String getHTML() {
    return DOM.getInnerHTML(getElementToAttach());
  }

  /**
   * Gets this item's parent.
   * 
   * @return the parent item
   */
  public FastTreeTableItem getParentItem() {
    return parent;
  }

  public String getText() {
    return DOM.getInnerText(getElementToAttach());
  }

  /**
   * Gets the tree that contains this item.
   * 
   * @return the containing tree
   */
  public final FastTreeTable getTreeTable() {
    return treeTable;
  }

  /**
   * Gets the <code>Widget</code> associated with this tree item.
   */
  public Widget getWidget() {
    return widget;
  }

  /**
   * Has this {@link FastTreeTableItem} ever been opened?
   * 
   * @return whether the {@link FastTreeTableItem} has ever been opened.
   */
  public boolean hasBeenOpened() {
    return state == TREE_NODE_INTERIOR_OPEN;
  }

  /**
   * Does this {@link FastTreeTableItem} represent an interior node?
   */
  public boolean isInteriorNode() {
    return state >= TREE_NODE_INTERIOR_NEVER_OPENED;
  }

  /**
   * Is this {@link FastTreeTableItem} a leaf node?
   */
  public boolean isLeafNode() {
    return state <= TREE_NODE_LEAF;
  }

  /**
   * Is the {@link FastTreeTableItem} open? Returns false if the
   * {@link FastTreeTableItem} is closed or a leaf node.
   */
  public boolean isOpen() {
    return state == TREE_NODE_INTERIOR_OPEN;
  }

  /**
   * Determines whether this item is currently selected.
   * 
   * @return <code>true</code> if it is selected
   */
  public boolean isSelected() {
    if (treeTable == null) {
      return false;
    } else {
      return treeTable.getSelectedItem() == this;
    }
  }

  /**
   * Returns whether the tree is currently showing this
   * {@link FastTreeTableItem}.
   */
  public boolean isShowing() {
    if (treeTable == null || isVisible() == false) {
      return false;
    } else if (parent == null) {
      return true;
    } else if (!parent.isOpen()) {
      return false;
    } else {
      return parent.isShowing();
    }
  }

  /**
   * Removes this item from its tree.
   */
  public void remove() {
    if (parent != null) {
      // If this item has a parent, remove self from it.
      parent.removeItem(this);
    } else if (treeTable != null) {
      // If the item has no parent, but is in the Tree, it must be a top-level
      // element.
      treeTable.removeItem(this);
    }
  }

  /**
   * Removes an item from the tree. Note, if you want to switch the node back to
   * be a leaf node, must call becomeLeaf()
   */
  public void removeItem(FastTreeTableItem item) {
    // Validate.
    if (children == null || !children.contains(item)) {
      return;
    }

    // Orphan.
    item.clearTree();

    // Physical detach.
    if (state != TREE_NODE_INTERIOR_NEVER_OPENED) {
      // TODO DOM.removeChild(childElems, item.getElement());
      // childTable.remove(item);
      // if (item.childTable != null) {
      // childTable.remove(item.childTable);
      // }
      treeTable.remove(item);
      if (item.state != TREE_NODE_INTERIOR_NEVER_OPENED) {
        for (int i = 0, n = item.getChildCount(); i < n; i++) {
          item.removeItem(item.getChild(i));
        }
      }
    }

    // Logical detach.
    item.setParentItem(null);
    children.remove(item);
  }

  /**
   * Removes all of this item's children.
   */
  public void removeItems() {
    while (getChildCount() > 0) {
      removeItem(getChild(0));
    }
  }

  public void setHTML(String html) {
    clearWidget();
    DOM.setInnerHTML(getElementToAttach(), html);
  }

  /**
   * Sets whether this item's children are displayed.
   * 
   * @param open whether the item is open
   */
  public final void setState(boolean open) {
    setState(open, true);
  }

  /**
   * Sets whether this item's children are displayed.
   * 
   * @param open whether the item is open
   * @param fireEvents <code>true</code> to allow open/close events to be
   *          fired
   */
  public void setState(boolean open, boolean fireEvents) {
    if (open == isOpen()) {
      return;
    }
    // Cannot open leaf nodes.
    if (isLeafNode()) {
      return;
    }
    if (open) {
      beforeOpen();
      if (state == TREE_NODE_INTERIOR_NEVER_OPENED) {
        ensureChildren();
        // childElems = DOM.createDiv();
        // childTable = new FixedWidthGrid();
        // childTable.resize(0, treeTable.getColumnCount());
        // childTable.setBorderWidth(0);
        // childTable.setCellPadding(treeTable.getCellPadding());
        // childTable.setCellSpacing(treeTable.getCellSpacing());
        // UIObject.setStyleName(childElems, STYLENAME_CHILDREN);
        // childTable.setStyleName(STYLENAME_CHILDREN);
        // convertElementToHaveChildren(childTable);

        if (children != null) {
          for (int i = 0, n = children.size(); i < n; i++) {
            final FastTreeTableItem item = children.get(i);
            // DOM.appendChild(childElems, item.getElement());
            // insertItem(item, childTable.getRowCount());
            insertItem(item, i);
          }
        }
      }

      state = TREE_NODE_INTERIOR_OPEN;
    } else {
      beforeClose();
      state = TREE_NODE_INTERIOR_CLOSED;
    }
    updateState();
    if (open) {
      afterOpen();
    } else {
      afterClose();
    }
  }

  public void setText(String text) {
    clearWidget();
    DOM.setInnerText(getElementToAttach(), text);
  }

  public void setWidget(Widget widget) {
    // Physical detach old from self.
    // Clear out any existing content before adding a widget.

    DOM.setInnerHTML(getElementToAttach(), "");
    clearWidget();
    addWidget(widget);
  }

  /**
   * Called after the tree item is closed.
   */
  protected void afterClose() {
  }

  /**
   * Called after the tree item is opened.
   */
  protected void afterOpen() {
  }

  /**
   * Called before the tree item is closed.
   */
  protected void beforeClose() {
  }

  /**
   * Called before the tree item is opened.
   */
  protected void beforeOpen() {
  }

  /**
   * Called when tree item is being unselected. Returning <code>false</code>
   * cancels the unselection.
   * 
   */
  protected boolean beforeSelectionLost() {
    return true;
  }

  /**
   * Fired when a tree item receives a request to open for the first time.
   * Should be overridden in child classes.
   */
  protected void ensureChildren() {
  }

  /**
   * Returns the widget, if any, that should be focused on if this TreeItem is
   * selected.
   * 
   * @return widget to be focused.
   */
  protected HasFocus getFocusableWidget() {
    Widget w = getWidget();
    if (w instanceof HasFocus) {
      return (HasFocus) w;
    } else {
      return null;
    }
  }

  /**
   * Called when a tree item is selected.
   * 
   */
  protected void onSelected() {
  }

  void clearTree() {
    if (treeTable != null) {
      if (widget != null) {
        treeTable.treeOrphan(widget);
      }
      if (treeTable.getSelectedItem() == this) {
        treeTable.setSelectedItem(null);
      }
      treeTable = null;
      for (int i = 0, n = getChildCount(); i < n; ++i) {
        children.get(i).clearTree();
      }
    }
  }

  // void convertElementToHaveChildren(FixedWidthGrid t) {
  // final Element tr =
  // getElement().getParentElement().getParentElement().cast();
  // final int r = OverrideDOM.getRowIndex(tr);
  // FastTreeTableItem parent = getParentItem();
  // if (parent != null) {
  // parent.childTable.insertRow(r);
  // final Element td =
  // tr.getNextSiblingElement().getFirstChildElement().cast();
  // DOM.setElementPropertyInt(td, "colSpan", treeTable.getColumnCount());
  // parent.childTable.setWidget(r, 0, t);
  // } else {
  // treeTable.insertRow(r);
  // final Element td =
  // tr.getNextSiblingElement().getFirstChildElement().cast();
  // DOM.setElementPropertyInt(td, "colSpan", treeTable.getColumnCount());
  // treeTable.setWidget(r, 0, t);
  // }
  // for (int i = 0; i < treeTable.getColumnCount(); i++) {
  // t.setColumnWidth(i, treeTable.getColumnWidth(i));
  // }
  // }

  void convertElementToInteriorNode(Element control) {
    setStyleName(getElement(), "gwt-FastTreeTableItem-leaf", false);
    DOM.appendChild(getElement(), control);
  }

  Element createLeafElement() {
    Element elem = DOMHelper.clone(TREE_LEAF, true);
    contentElem = DOMHelper.rawFirstChild(elem);
    return elem;
  }

  void dumpTreeTableItems(List<FastTreeTableItem> accum) {
    if (isInteriorNode() && getChildCount() > 0) {
      for (int i = 0; i < children.size(); i++) {
        FastTreeTableItem item = children.get(i);
        accum.add(item);
        item.dumpTreeTableItems(accum);
      }
    }
  }

  ArrayList<FastTreeTableItem> getChildren() {
    return children;
  }

  Element getContentElem() {
    return contentElem;
  }

  Element getControlElement() {
    return DOM.getParent(contentElem);
  }

  Element getElementToAttach() {
    return contentElem;
  }

  void setParentItem(FastTreeTableItem parent) {
    this.parent = parent;
  }

  /**
   * Selects or deselects this item.
   * 
   * @param selected <code>true</code> to select the item, <code>false</code>
   *          to deselect it
   */
  void setSelection(boolean selected, boolean fireEvents) {
    setStyleName(getControlElement(), STYLENAME_SELECTED, selected);
    if (selected && fireEvents) {
      onSelected();
    }
  }

  void setTreeTable(FastTreeTable newTreeTable) {
    if (treeTable == newTreeTable) {
      return;
    }

    // Early out.
    if (treeTable != null) {
      throw new IllegalStateException(
          "Each Tree Item must be removed from its current tree before being added to another.");
    }
    treeTable = newTreeTable;

    if (widget != null) {
      // Add my widget to the new tree.
      treeTable.adopt(widget, this);
    }

    for (int i = 0, n = getChildCount(); i < n; ++i) {
      children.get(i).setTreeTable(newTreeTable);
    }
  }

  void updateState() {
    // No work to be done.
    if (isLeafNode()) {
      return;
    }
    // Element tr =
    // childTable.getElement().getParentElement().getParentElement().cast();
    if (isOpen()) {
      showOpenImage();
      // UIObject.setVisible(tr, true);
      for (int i = 0, n = getChildCount(); i < n; i++) {
        final FastTreeTableItem item = getChild(i);
        final Element tr = item.getElement().getParentElement().getParentElement().cast();
        UIObject.setVisible(tr, true);
        updateVisibility(item, true);
      }
    } else {
      showClosedImage();
      // UIObject.setVisible(tr, false);
      if (state != TREE_NODE_INTERIOR_NEVER_OPENED) {
        for (int i = 0, n = getChildCount(); i < n; i++) {
          final FastTreeTableItem item = getChild(i);
          final Element tr = item.getElement().getParentElement().getParentElement().cast();
          UIObject.setVisible(tr, false);
          updateVisibility(item, false);
        }
      }
    }
  }

  void updateVisibility(final FastTreeTableItem item, final boolean visible) {
    if (item.state != TREE_NODE_INTERIOR_NEVER_OPENED) {
      for (int i = 0, n = item.getChildCount(); i < n; i++) {
        final FastTreeTableItem child = item.getChild(i);
        final Element tr = child.getElement().getParentElement().getParentElement().cast();
        UIObject.setVisible(tr, visible);
        child.updateVisibility(child, visible);
      }
    }
  }

  /**
   * Adds a widget to an already empty {@link FastTreeTableItem}.
   */
  private void addWidget(Widget newWidget) {
    // Detach new child from old parent.
    if (newWidget != null) {
      newWidget.removeFromParent();
    }

    // Logical detach old/attach new.
    widget = newWidget;

    if (newWidget != null) {
      DOM.appendChild(getElementToAttach(), widget.getElement());
      bidiSupport();
      // Attach child to tree.
      if (treeTable != null) {
        treeTable.adopt(widget, this);
      }
    }
  }

  private void bidiSupport() {
  }

  private void clearWidget() {
    // Detach old child from tree.
    if (widget != null && treeTable != null) {
      treeTable.treeOrphan(widget);
      widget = null;
    }
  }

  private void showClosedImage() {
    setStyleName(getControlElement(), STYLENAME_OPEN, false);
    setStyleName(getControlElement(), STYLENAME_CLOSED, true);
  }

  private void showOpenImage() {
    setStyleName(getControlElement(), STYLENAME_CLOSED, false);
    setStyleName(getControlElement(), STYLENAME_OPEN, true);
  }

  private int depth = -1;

  private int getDepth() {
    return depth;
  }

  private void setDepth(final int depth) {
    this.depth = depth;
  }

}
