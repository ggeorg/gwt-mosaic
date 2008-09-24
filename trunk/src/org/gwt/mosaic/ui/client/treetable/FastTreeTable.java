/*
 * Copyright 2006-2008 Google Inc. Copyright 2008 Georgios J. Georgopoulos.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.libideas.client.StyleInjector;
import com.google.gwt.libideas.resources.client.DataResource;
import com.google.gwt.libideas.resources.client.ImmutableResourceBundle;
import com.google.gwt.libideas.resources.client.TextResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.google.gwt.widgetideas.client.FastTreeItem;
import com.google.gwt.widgetideas.client.overrides.DOMHelper;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;

/**
 * A standard hierarchical tree widget. The tree contains a hierarchy of
 * {@link FastTreeItem}s.
 * <p>
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-FastTree { the tree itself }</li>
 * <li>.gwt-FastTree .gwt-FastTreeItem { a tree item }</li>
 * <li>.gwt-FastTree .selection-bar {the selection bar used to highlight the
 * selected tree item}</li>
 * </ul>
 */
public class FastTreeTable extends FixedWidthGrid implements HasFocus,
    HasFastTreeTableItems {

  @Override
  public void setColumnWidth(int column, int width) {
    super.setColumnWidth(column, width);
  }

  @Override
  protected void hoverCell(Element cellElem) {
    super.hoverCell(cellElem);
  }
  
  /**
   * Resources used.
   */
  public interface DefaultResources extends ImmutableResourceBundle {

    /**
     * The css file.
     * 
     * @gwt.resource FastTreeTable.css
     */
    TextResource css();

    /**
     * The rtl css file.
     * 
     * @gwt.resource FastTreeTableRTL.css
     */
    TextResource cssRTL();

    /**
     * The gif used to highlight selection.
     * 
     * @gwt.resource selectionBar.gif
     */
    DataResource selectionBar();

    /**
     * "+" gif.
     * 
     * @gwt.resource treeClosed.gif
     */

    DataResource treeClosed();

    /**
     * "-" gif.
     * 
     * @gwt.resource treeOpen.gif
     */
    DataResource treeOpen();
  }

  private static final String STYLENAME_DEFAULT = "gwt-FastTreeTable";

  private static final String STYLENAME_SELECTION = "selection-bar";

  private static FocusImpl impl = FocusImpl.getFocusImplForPanel();

  /**
   * Add the default style sheet and images.
   */
  public static void addDefaultCSS() {
    DefaultResources instance = GWT.create(DefaultResources.class);
    if (LocaleInfo.getCurrentLocale().isRTL()) {
      StyleInjector.injectStylesheet(instance.cssRTL().getText(), instance);
    } else {
      StyleInjector.injectStylesheet(instance.css().getText(), instance);
    }
  }

  private static boolean hasModifiers(Event event) {
    boolean alt = event.getAltKey();
    boolean ctrl = event.getCtrlKey();
    boolean meta = event.getMetaKey();
    boolean shift = event.getShiftKey();

    return alt || ctrl || meta || shift;
  }

  private boolean lostMouseDown = true;
  /**
   * Map of TreeItem.widget -> TreeItem.
   */
  private final Map<Widget, FastTreeTableItem> childWidgets = new HashMap<Widget, FastTreeTableItem>();
  private FastTreeTableItem curSelection;
  private final Element focusable;
  private FocusListenerCollection focusListeners;
  private KeyboardListenerCollection keyboardListeners;
  private MouseListenerCollection mouseListeners;
  private final FastTreeTableItem root;
  private Event keyDown;

  private Event lastKeyDown;

  /**
   * Constructs a tree.
   */
  public FastTreeTable() {
    super();

    setBorderWidth(0);
    setCellPadding(0);
    setCellSpacing(0);

    focusable = createFocusElement();
    setStyleName(focusable, STYLENAME_SELECTION);

    sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK | Event.KEYEVENTS
        | Event.MOUSEEVENTS);

    // The 'root' item is invisible and serves only as a container
    // for all top-level items.
    root = new FastTreeTableItem() {
      @Override
      public void addItem(FastTreeTableItem item) {
        super.addItem(item);

        // DOM.appendChild(FastTreeTable.this.getElement(), item.getElement());
        insertItem(item, getRowCount());

        // Explicitly set top-level items' parents to null.
        item.setParentItem(null);

        // Use no margin on top-most items.
        DOM.setIntStyleAttribute(item.getElement(), "margin", 0);
      }

      @Override
      public void removeItem(FastTreeTableItem item) {
        if (!getChildren().contains(item)) {
          return;
        }

        // Update Item state.
        item.clearTree();
        item.setParentItem(null);
        getChildren().remove(item);

        DOM.removeChild(FastTreeTable.this.getElement(), item.getElement());
      }
    };
    root.setTreeTable(this);

    setStyleName(STYLENAME_DEFAULT);
    moveSelectionBar(curSelection);
  }

  private int treeColumn = 0;

  public int getTreeColumn() {
    return treeColumn;
  }

  public void setTreeColumn(int treeColumn) {
    this.treeColumn = treeColumn;
  }

  /**
   * Updates table rows to include children.
   * 
   * @param item the item to insert
   * @param r the row to insert the item to
   */
  private void insertItem(FastTreeTableItem item, int r) {
    insertRow(r);
    setWidget(r, getTreeColumn(), item);
    render(item, r);
  }

  private TreeTableLabelProvider labelProvider;

  class DefaultTreeTableLabelProvider implements TreeTableLabelProvider {
    public Object getItemLabel(FastTreeTableItem item, int col) {
      final Object obj = item.getUserObject();
      if (obj instanceof Widget) {
        return obj;
      } else if (obj instanceof Object[]) {
        Object[] objs = (Object[]) obj;
        if (objs.length > col) {
          Object o = objs[col];
          if (o instanceof Widget) {
            return o;
          } else if (o != null) {
            return o.toString();
          } else {
            return null;
          }
        }
      } else if (obj != null) {
        return obj.toString();
      }
      return null;
    }
  }

  public TreeTableLabelProvider getTreeTableLabelProvider() {
    if (labelProvider == null) {
      labelProvider = new DefaultTreeTableLabelProvider();
    }
    return labelProvider;
  }

  public void setTreeTableLabelProvider(TreeTableLabelProvider renderer) {
    this.labelProvider = renderer;
  }

  public void render(FastTreeTableItem item, int row) {
    for (int i = 0, n = getColumnCount(); i < n; i++) {
      if (i == getTreeColumn()) {
        continue;
      }
      final Object obj = getTreeTableLabelProvider().getItemLabel(item, i);
      if (obj instanceof Widget) {
        setWidget(row, i, (Widget) obj);
      } else if (obj != null) {
        setHTML(row, i, obj.toString());
      }
    }
  }

  /**
   * Adds the widget as a root tree item.
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   * @param widget widget to add.
   */
  @Override
  public void add(Widget widget) {
    addItem(widget);
  }

  public void addFocusListener(FocusListener listener) {
    if (focusListeners == null) {
      focusListeners = new FocusListenerCollection();
    }
    focusListeners.add(listener);
  }

  /**
   * Adds an item to the root level of this tree.
   * 
   * @param item the item to be added
   */
  public void addItem(FastTreeTableItem item) {
    root.addItem(item);
  }

  /**
   * Adds a simple tree item containing the specified text.
   * 
   * @param itemText the text of the item to be added
   * @return the item that was added
   */
  public FastTreeTableItem addItem(String itemText) {
    FastTreeTableItem ret = new FastTreeTableItem(itemText);
    addItem(ret);

    return ret;
  }

  /**
   * Adds a new tree item containing the specified widget.
   * 
   * @param widget the widget to be added
   */
  public FastTreeTableItem addItem(Widget widget) {
    return root.addItem(widget);
  }

  public void addKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners == null) {
      keyboardListeners = new KeyboardListenerCollection();
    }
    keyboardListeners.add(listener);
  }

  public void addMouseListener(MouseListener listener) {
    if (mouseListeners == null) {
      mouseListeners = new MouseListenerCollection();
    }
    mouseListeners.add(listener);
  }

  /**
   * Clears all tree items from the current tree.
   */
  @Override
  public void clear() {
    int size = root.getChildCount();
    for (int i = size - 1; i >= 0; i--) {
      root.getChild(i).remove();
    }
  }

  /**
   * Ensures that the currently-selected item is visible, opening its parents
   * and scrolling the tree as necessary.
   */
  public void ensureSelectedItemVisible() {
    if (curSelection == null) {
      return;
    }

    FastTreeTableItem parent = curSelection.getParentItem();
    while (parent != null) {
      parent.setState(true);
      parent = parent.getParentItem();
    }
    moveFocus(curSelection);
  }

  public FastTreeTableItem getChild(int index) {
    return root.getChild(index);
  }

  public int getChildCount() {
    return root.getChildCount();
  }

  public int getChildIndex(FastTreeTableItem child) {
    return root.getChildIndex(child);
  }

  /**
   * Gets the top-level tree item at the specified index.
   * 
   * @param index the index to be retrieved
   * @return the item at that index
   */
  public FastTreeTableItem getItem(int index) {
    return root.getChild(index);
  }

  /**
   * Gets the number of items contained at the root of this tree.
   * 
   * @return this tree's item count
   */
  public int getItemCount() {
    return root.getChildCount();
  }

  /**
   * Gets the currently selected item.
   * 
   * @return the selected item
   */
  public FastTreeTableItem getSelectedItem() {
    return curSelection;
  }

  public int getTabIndex() {
    return impl.getTabIndex(focusable);
  }

  public Iterator<Widget> iterator() {
    final Widget[] widgets = new Widget[childWidgets.size()];
    childWidgets.keySet().toArray(widgets);
    return WidgetIterators.createWidgetIterator(this, widgets);
  }

  @Override
  @SuppressWarnings("fallthrough")
  public void onBrowserEvent(Event event) {
    int eventType = DOM.eventGetType(event);

    switch (eventType) {
      case Event.ONCLICK: {
        Element e = DOM.eventGetTarget(event);
        if (shouldTreeDelegateFocusToElement(e)) {
          // The click event should have given focus to this element already.
          // Avoid moving focus back up to the tree (so that focusable widgets
          // attached to TreeItems can receive keyboard events).
        } else {
          if (!hasModifiers(event)) {
            clickedOnFocus(DOM.eventGetTarget(event));
          }
        }
        break;
      }

      case Event.ONMOUSEMOVE: {
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, event);
        }
        break;
      }

      case Event.ONMOUSEUP: {
        boolean left = event.getButton() == Event.BUTTON_LEFT;

        if (lostMouseDown) {
          // artificial mouse down due to IE bug where mouse downs are lost.

          if (left && !hasModifiers(event)) {
            elementClicked(root, event);
          }
        }
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, event);
        }
        lostMouseDown = true;
        break;
      }
      case Event.ONMOUSEDOWN: {
        boolean left = event.getButton() == Event.BUTTON_LEFT;

        lostMouseDown = false;
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, event);
        }
        if (left && !hasModifiers(event)) {
          elementClicked(root, event);
        }
        break;
      }
      case Event.ONMOUSEOVER: {
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, event);
        }
        break;
      }

      case Event.ONMOUSEOUT: {
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, event);
        }
        break;
      }

      case Event.ONFOCUS:
        // If we already have focus, ignore the focus event.
        if (focusListeners != null) {
          focusListeners.fireFocusEvent(this, event);
        }
        break;

      case Event.ONBLUR: {
        if (focusListeners != null) {
          focusListeners.fireFocusEvent(this, event);
        }

        break;
      }

      case Event.ONKEYDOWN:
        keyDown = event;
        // Intentional fallthrough.
      case Event.ONKEYUP:
        if (eventType == Event.ONKEYUP) {
          // If we got here because of a key tab, then we need to make sure the
          // current tree item is selected.
          if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
            ArrayList<Element> chain = new ArrayList<Element>();
            collectElementChain(chain, getElement(), DOM.eventGetTarget(event));
            FastTreeTableItem item = findItemByChain(chain, 0, root);
            if (item != getSelectedItem()) {
              setSelectedItem(item, true);
            }
          }
        }

        // Intentional fall through.
      case Event.ONKEYPRESS: {
        if (keyboardListeners != null) {
          keyboardListeners.fireKeyboardEvent(this, event);
        }

        if (hasModifiers(event)) {
          break;
        }

        // Trying to avoid duplicate key downs and fire navigation despite
        // missing key downs.
        if (eventType != Event.ONKEYUP) {
          if (lastKeyDown == null || (!lastKeyDown.equals(keyDown))) {
            keyboardNavigation(event);
          }
          if (eventType == Event.ONKEYPRESS) {
            lastKeyDown = null;
          } else {
            lastKeyDown = keyDown;
          }
        }
        if (DOMHelper.isArrowKey(DOM.eventGetKeyCode(event))) {
          DOM.eventCancelBubble(event, true);
          DOM.eventPreventDefault(event);
        }
        break;
      }
    }

    // We must call SynthesizedWidget's implementation for all other events.
    super.onBrowserEvent(event);
  }

  @Override
  public boolean remove(Widget w) {
    // Validate.
    FastTreeTableItem item = childWidgets.get(w);
    if (item == null) {
      return false;
    }

    // Delegate to TreeItem.setWidget, which performs correct removal.
    item.setWidget(null);
    return true;
  }

  public void removeFocusListener(FocusListener listener) {
    if (focusListeners != null) {
      focusListeners.remove(listener);
    }
  }

  /**
   * Removes an item from the root level of this tree.
   * 
   * @param item the item to be removed
   */
  public void removeItem(FastTreeTableItem item) {
    root.removeItem(item);
  }

  /**
   * Removes all items from the root level of this tree.
   */
  public void removeItems() {
    while (getItemCount() > 0) {
      removeItem(getItem(0));
    }
  }

  public void removeKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners != null) {
      keyboardListeners.remove(listener);
    }
  }

  public void setAccessKey(char key) {
    impl.setAccessKey(focusable, key);
  }

  public void setFocus(boolean focus) {
    if (focus) {
      impl.focus(focusable);
    } else {
      impl.blur(focusable);
    }
  }

  /**
   * Selects a specified item.
   * 
   * @param item the item to be selected, or <code>null</code> to deselect all
   *          items
   */
  public void setSelectedItem(FastTreeTableItem item) {
    setSelectedItem(item, true);
  }

  /**
   * Selects a specified item.
   * 
   * @param item the item to be selected, or <code>null</code> to deselect all
   *          items
   * @param fireEvents <code>true</code> to allow selection events to be fired
   */
  public void setSelectedItem(FastTreeTableItem item, boolean fireEvents) {
    if (item == null) {
      if (curSelection == null) {
        return;
      }
      curSelection.setSelection(false, fireEvents);
      curSelection = null;
      return;
    }

    onSelection(item, fireEvents, true);
  }

  public void setTabIndex(int index) {
    impl.setTabIndex(focusable, index);
  }

  /**
   * Iterator of tree items.
   */
  public Iterator<FastTreeTableItem> treeItemIterator() {
    List<FastTreeTableItem> accum = new ArrayList<FastTreeTableItem>();
    root.dumpTreeTableItems(accum);
    return accum.iterator();
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    DOM.setEventListener(focusable, this);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    DOM.setEventListener(focusable, null);
  }

  protected FastTreeTableItem getRoot() {
    return root;
  }

  protected void keyboardNavigation(Event event) {
    // If nothing's selected, select the first item.
    if (curSelection == null) {
      if (root.getChildCount() > 0) {
        onSelection(root.getChild(0), true, true);
      }
      super.onBrowserEvent(event);
    } else {

      // Handle keyboard events if keyboard navigation is enabled

      switch (DOMHelper.standardizeKeycode(DOM.eventGetKeyCode(event))) {
        case KeyboardListener.KEY_UP: {
          moveSelectionUp(curSelection);
          break;
        }
        case KeyboardListener.KEY_DOWN: {
          moveSelectionDown(curSelection, true);
          break;
        }
        case KeyboardListener.KEY_LEFT: {
          if (curSelection.isOpen()) {
            curSelection.setState(false);
          } else {
            FastTreeTableItem parent = curSelection.getParentItem();
            if (parent != null) {
              setSelectedItem(parent);
            }
          }
          break;
        }
        case KeyboardListener.KEY_RIGHT: {
          if (!curSelection.isOpen()) {
            curSelection.setState(true);
          }
          // Do nothing if the element is already open.
          break;
        }
      }
    }
  }

  /**
   * Moves the selection bar around the given {@link FastTreeItem}.
   * 
   * @param item the item to move selection bar to
   */
  protected void moveSelectionBar(FastTreeTableItem item) {
    if (item == null || item.isShowing() == false) {
      UIObject.setVisible(focusable, false);
      return;
    }
    // focusable is being used for highlight as well.
    // Get the location and size of the given item's content element relative
    // to the tree.
    Element selectedElem = item.getContentElem();
    moveElementOverTarget(focusable, selectedElem);
    UIObject.setVisible(focusable, true);
  }

  @Override
  protected void onLoad() {
    if (getSelectedItem() != null) {
      moveSelectionBar(getSelectedItem());
    }
  }

  protected void onSelection(FastTreeTableItem item, boolean fireEvents,
      boolean moveFocus) {
    // 'root' isn't a real item, so don't let it be selected
    // (some cases in the keyboard handler will try to do this)
    if (item == root) {
      return;
    }

    if (curSelection == item) {
      return;
    }
    if (curSelection != null) {
      if (curSelection.beforeSelectionLost() == false) {
        return;
      }
      curSelection.setSelection(false, fireEvents);
    }

    curSelection = item;

    if (curSelection != null) {
      if (moveFocus) {
        moveFocus(curSelection);
      } else {
        // Move highlight even if we do no not need to move focus.
        moveSelectionBar(curSelection);
      }

      // Select the item and fire the selection event.
      curSelection.setSelection(true, fireEvents);
    }
  }

  /**
   * This method is called immediately before a widget will be detached from the
   * browser's document.
   */
  @Override
  protected void onUnload() {
  }

  /**
   * This is called when a valid selectable element is clicked in the tree.
   * Subclasses can override this method to decide whether or not FastTree
   * should keep processing the element clicked. For example, a subclass may
   * decide to return false for this method if selecting a new item in the tree
   * is subject to asynchronous approval from other components of the
   * application.
   * 
   * @returns true if element should be processed normally, false otherwise.
   *          Default returns true.
   */
  protected boolean processElementClicked(FastTreeTableItem item) {
    return true;
  }

  void adopt(Widget widget, FastTreeTableItem treeItem) {
    assert (!childWidgets.containsKey(widget));
    childWidgets.put(widget, treeItem);
    super.adopt(widget);
  }

  /*
   * This method exists solely to support unit tests.
   */
  Map<Widget, FastTreeTableItem> getChildWidgets() {
    return childWidgets;
  }

  void treeOrphan(Widget widget) {
    super.orphan(widget);

    // Logical detach.
    childWidgets.remove(widget);
  }

  private void clickedOnFocus(Element e) {
    // An element was clicked on that is not focusable, so we use the hidden
    // focusable to not shift focus.
    moveElementOverTarget(focusable, e);
    impl.focus(focusable);
  }

  /**
   * Collects parents going up the element tree, terminated at the tree root.
   */
  private void collectElementChain(ArrayList<Element> chain, Element hRoot,
      Element hElem) {
    if ((hElem == null) || hElem.equals(hRoot)) {
      return;
    }

    collectElementChain(chain, hRoot, DOM.getParent(hElem));
    chain.add(hElem);
  }

  private Element createFocusElement() {
    Element e = impl.createFocusable();
    DOM.setStyleAttribute(e, "position", "absolute");
    DOM.appendChild(getElement(), e);
    DOM.sinkEvents(e, Event.FOCUSEVENTS | Event.ONMOUSEDOWN);
    // Needed for IE only
    DOM.setElementAttribute(e, "focus", "false");
    return e;
  }

  /**
   * Disables the selection text on IE.
   */
  private native void disableSelection(Element element)
  /*-{
    element.onselectstart = function() {
      return false;
    };  
  }-*/;

  private boolean elementClicked(FastTreeTableItem root, Event event) {
    Element target = DOM.eventGetTarget(event).cast();
    ArrayList<Element> chain = new ArrayList<Element>();
    collectElementChain(chain, getElement(), target);
    FastTreeTableItem item = findItemByChain(chain, 0, root);
    if (item != null) {
      if (item.isInteriorNode() && item.getControlElement().equals(target)) {
        item.setState(!item.isOpen(), true);
        moveSelectionBar(curSelection);
        disableSelection(target);
        return false;
      }
      if (processElementClicked(item)) {
        onSelection(item, true, !shouldTreeDelegateFocusToElement(target));
        return true;
      }
    }
    return false;
  }

  private FastTreeTableItem findDeepestOpenChild(FastTreeTableItem item) {
    if (!item.isOpen()) {
      return item;
    }
    return findDeepestOpenChild(item.getChild(item.getChildCount() - 1));
  }

  private FastTreeTableItem findItemByChain(final ArrayList<Element> chain,
      int idx, FastTreeTableItem root) {
    if (idx == chain.size()) {
      return root;
    }

    for (int i = 0, n = chain.size(); i < n; i++) {
      final Element elem = (Element) chain.get(i);
      String nodeName = elem.getNodeName();
      if ("div".equalsIgnoreCase(nodeName)) {
        return findItemByElement(root, elem);
      }
    }

    return null;
  }

  private FastTreeTableItem findItemByElement(FastTreeTableItem item,
      Element elem) {
    if (item.getElement().equals(elem)) {
      return item;
    }
    for (int i = 0, n = item.getChildCount(); i < n; ++i) {
      FastTreeTableItem child = item.getChild(i);
      child = findItemByElement(child, elem);
      if (child != null) {
        return child;
      }
    }
    return null;
  }

  private void moveElementOverTarget(Element movable, Element target) {
    int containerTop = getAbsoluteTop();

    int top = DOM.getAbsoluteTop(target) - containerTop;
    int height = DOM.getElementPropertyInt(target, "offsetHeight");

    // Set the element's position and size to exactly underlap the
    // item's content element.

    DOM.setStyleAttribute(movable, "height", height + "px");
    DOM.setStyleAttribute(movable, "top", top + "px");
  }

  /**
   * Move the tree focus to the specified selected item.
   * 
   * @param selection
   */
  private void moveFocus(FastTreeTableItem selection) {
    moveSelectionBar(selection);
    DOM.scrollIntoView(focusable);
    HasFocus focusableWidget = selection.getFocusableWidget();
    if (focusableWidget != null) {
      focusableWidget.setFocus(true);
    } else {
      // Ensure Focus is set, as focus may have been previously delegated by
      // tree.

      impl.focus(focusable);
    }
  }

  /**
   * Moves to the next item, going into children as if dig is enabled.
   */
  private void moveSelectionDown(FastTreeTableItem sel, boolean dig) {
    if (sel == root) {
      return;
    }
    FastTreeTableItem parent = sel.getParentItem();
    if (parent == null) {
      parent = root;
    }
    int idx = parent.getChildIndex(sel);

    if (!dig || !sel.isOpen()) {
      if (idx < parent.getChildCount() - 1) {
        onSelection(parent.getChild(idx + 1), true, true);
      } else {
        moveSelectionDown(parent, false);
      }
    } else if (sel.getChildCount() > 0) {
      onSelection(sel.getChild(0), true, true);
    }
  }

  /**
   * Moves the selected item up one.
   */
  private void moveSelectionUp(FastTreeTableItem sel) {
    FastTreeTableItem parent = sel.getParentItem();
    if (parent == null) {
      parent = root;
    }
    int idx = parent.getChildIndex(sel);

    if (idx > 0) {
      FastTreeTableItem sibling = parent.getChild(idx - 1);
      onSelection(findDeepestOpenChild(sibling), true, true);
    } else {
      onSelection(parent, true, true);
    }
  }

  private native boolean shouldTreeDelegateFocusToElement(Element elem)
  /*-{
    var name = elem.nodeName;
    return ((name == "SELECT") ||
       (name == "INPUT")  ||
       (name == "TEXTAREA") ||
       (name == "OPTION") ||
       (name == "BUTTON") ||
       (name == "LABEL") 
    );
  }-*/;
}

/**
 * A collection of convenience factories for creating iterators for widgets.
 * This mostly helps developers support {@link HasWidgets} without having to
 * implement their own {@link Iterator}.
 */
class WidgetIterators {

  /**
   * Wraps an array of widgets to be returned during iteration.
   * <code>null</code> is allowed in the array and will be skipped during
   * iteration.
   * 
   * @param container the container of the widgets in <code>contained</code>
   * @param contained the array of widgets
   * @return the iterator
   */
  static final Iterator<Widget> createWidgetIterator(
      final HasWidgets container, final Widget[] contained) {
    return new Iterator<Widget>() {
      int index = -1, last = -1;
      boolean widgetsWasCopied = false;
      Widget[] widgets = contained;

      {
        gotoNextIndex();
      }

      public boolean hasNext() {
        return (index < contained.length);
      }

      public Widget next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        last = index;
        final Widget w = contained[index];
        gotoNextIndex();
        return w;
      }

      public void remove() {
        if (last < 0) {
          throw new IllegalStateException();
        }

        if (!widgetsWasCopied) {
          widgets = copyWidgetArray(widgets);
          widgetsWasCopied = true;
        }

        container.remove(contained[last]);
        last = -1;
      }

      private void gotoNextIndex() {
        ++index;
        while (index < contained.length) {
          if (contained[index] != null) {
            return;
          }
          ++index;
        }
      }
    };
  }

  private static Widget[] copyWidgetArray(final Widget[] widgets) {
    final Widget[] clone = new Widget[widgets.length];
    for (int i = 0; i < widgets.length; i++) {
      clone[i] = widgets[i];
    }
    return clone;
  }

  private WidgetIterators() {
    // Not instantiable.
  }

}