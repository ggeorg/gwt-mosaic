package org.gwt.mosaic.ui.client;

import java.util.Set;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.widgetideas.table.client.SortableGrid;

public class ListBox<T> extends Composite {

  private static final int INSERT_AT_END = -1;

  public ListBox() {
    initWidget(new SortableGrid());
  }

  @Override
  protected SortableGrid getWidget() {
    return (SortableGrid) super.getWidget();
  }

  /**
   * Adds an item to the list box.
   * 
   * @param item the item to be added
   */
  public void addItem(T item) {
    insertItem(item, INSERT_AT_END);
  }

  /**
   * Removes all items from the list box.
   */
  public void clear() {
    getWidget().clearAll();
  }

  /**
   * Gets the number of items present in the list box.
   * 
   * @return the number of items
   */
  public int getItemCount() {
    return getWidget().getRowCount();
  }

  public T getItem(int index) {
    return null;// TODO
  }

  /**
   * Gets the currently-selected item. If multiple items are selected, this
   * method will returns the first selected item ({@link #isItemeSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or {@code -1} if none is selected
   */
  public int getSelectedIndex() {
    Set<Integer> selection = getWidget().getSelectedRows();
    for (Integer i : selection) {
      return i.intValue();
    }
    return -1;
  }

  /**
   * Inserts an item into the list box.
   * 
   * @param item the item to be inserted
   * @param index the index at which to insert it
   */
  public void insertItem(T item, int index) {
    // TODO
  }

  /**
   * Determines whether as individual list is selected.
   * 
   * @param index the index of the item to be tested
   * @return {@code true} if the item is selected
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public boolean isItemSelected(int index) {
    checkIndex(index);
    return getWidget().isRowSelected(index);
  }

  public boolean isMultipleSelect() {
    return false; // TODO
  }

  /**
   * Removes the item at the specified index.
   * 
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void removeItem(int index) {
    checkIndex(index);
    // TODO
  }

  /**
   * Sets whether an individual list item is selected.
   * <p>
   * Note that setting the selection programmatically does <em>not</em> cause
   * the {@link ChangeListener#onChange(Widget)} event to be fired.
   * 
   * @param index the index of the item to be selected or unselected
   * @param selected {@code true} to select the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemSelected(int index, boolean selected) {
    checkIndex(index);
    getWidget().selectRow(index, false);
  }

  /**
   * Sets whether this list allows multiple selections.
   * 
   * @param multiple {@code true} to allow multiple selections
   */
  public void setMultipleSelect(boolean multiple) {
    // TODO
  }

  /**
   * Sets the currently selected index.
   * <p>
   * After calling this method, only the specified item in the list will remain
   * selected. For a {@code ListBox} with multiple selection enabled, see
   * {@link #setItemSelected(int, boolean)} to select multiple items at a time.
   * <p>
   * Note that setting the selected index programmatically does <em>not</em>
   * cause the {@link ChangeListener#onChange(Widget)} event to be fired.
   * 
   * @param index the index of the item to be selected
   */
  public void setSelectedIndex(int index) {
    checkIndex(index);
    getWidget().selectRow(index, true);
  }

  /**
   * Sets the number of items that are visible. If only one item is visible,
   * then the box will be displayed as a drop-down list.
   * 
   * @param visibleItems the visible item count
   */
  public void setVisibleItemCount(int visibleItems) {
    // TODO
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException();
    }
  }
}
