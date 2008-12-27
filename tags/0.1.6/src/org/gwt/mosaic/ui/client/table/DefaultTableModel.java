package org.gwt.mosaic.ui.client.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * The default implementation of {@link TableModel} based on a
 * {@code java.util.Vector}.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class DefaultTableModel extends Vector<Vector<Object>> implements
    TableModel<Vector<Object>>, Serializable {
  private static final long serialVersionUID = -6152131797498407068L;

  /** List of {@link TableModelListener TableModelListeners}. */
  protected List<TableModelListener> listenerList = new ArrayList<TableModelListener>();

  /**
   * Constructs an empty {@code DefaultListModel} instance.
   */
  public DefaultTableModel() {
    super();
  }

  /**
   * Constructs a model containing the elements of the specified collection, in
   * the order they are returned by the collection's iterator.
   * 
   * @param c the collection whose elements are to be placed into this model
   * @throws {@code NullPointerException} if the specified collection is
   *           {@code null}
   */
  public DefaultTableModel(Collection<? extends Vector<Object>> c) {
    super(c);
  }

  /**
   * Adds a listener to the list that's notified each time a change to the data
   * model occurs.
   * 
   * @param listener
   * @see com.google.gwt.widgetideas.table.client.TableModel#addTableModelListener(com.google.gwt.widgetideas.table.client.TableModelListener)
   */
  public void addTableModelListener(TableModelListener listener) {
    listenerList.add(listener);
  }

  /**
   * Removes a listener from the list.
   * 
   * @param listener the {@link TableModelListener}
   */
  public void removeTableModelListener(TableModelListener listener) {
    listenerList.remove(listener);
  }

  /**
   * Returns an array of all the table model listeners registered on this model.
   * 
   * @return all of this model's {@code TableModelListeners} or an empty array
   *         if no table model listeners are currently registered
   * 
   * @see #addTableModelListener
   * @see #removeTableModelListener
   */
  public TableModelListener[] getTableModelListeners() {
    return (TableModelListener[]) listenerList.toArray(new TableModelListener[listenerList.size()]);
  }

  /**
   * Notifies all listeners that all cell values in the table's rows may have
   * changed. The number of rows may also have changed and the {@code Table}
   * should redraw the table from scratch. The structure of the table (as in the
   * order of the columns) is assumed to be the same.
   * 
   * @see TableModelEvent
   * @see EventListenerList
   * @see javax.swing.JTable#tableChanged(TableModelEvent)
   */
  public void fireTableDataChanged() {
    fireTableChanged(new TableModelEvent(this));
  }

  /**
   * Notifies all listeners that the table's structure has changed. The number
   * of columns in the table, and the names and types of the new columns may be
   * different from the previous state. If the <code>JTable</code> receives
   * this event and its <code>autoCreateColumnsFromModel</code> flag is set it
   * discards any table columns that it had and reallocates default columns in
   * the order they appear in the model. This is the same as calling
   * <code>setModel(TableModel)</code> on the <code>JTable</code>.
   * 
   * @see TableModelEvent
   * @see EventListenerList
   */
  public void fireTableStructureChanged() {
    fireTableChanged(new TableModelEvent(this, 0 /* TableModelEvent.HEADER_ROW, */));
  }

  /**
   * Notifies all listeners that rows in the range
   * <code>[firstRow, lastRow]</code>, inclusive, have been inserted.
   * 
   * @param firstRow the first row
   * @param lastRow the last row
   * 
   * @see TableModelEvent
   * @see EventListenerList
   * 
   */
  public void fireTableRowsInserted(int firstRow, int lastRow) {
    fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
        TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.INSERT));
  }

  /**
   * Notifies all listeners that rows in the range
   * <code>[firstRow, lastRow]</code>, inclusive, have been updated.
   * 
   * @param firstRow the first row
   * @param lastRow the last row
   * 
   * @see TableModelEvent
   * @see EventListenerList
   */
  public void fireTableRowsUpdated(int firstRow, int lastRow) {
    fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
        TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.UPDATE));
  }

  /**
   * Notifies all listeners that rows in the range
   * <code>[firstRow, lastRow]</code>, inclusive, have been deleted.
   * 
   * @param firstRow the first row
   * @param lastRow the last row
   * 
   * @see TableModelEvent
   * @see EventListenerList
   */
  public void fireTableRowsDeleted(int firstRow, int lastRow) {
    fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
        TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.DELETE));
  }

  /**
   * Notifies all listeners that the value of the cell at
   * <code>[row, column]</code> has been updated.
   * 
   * @param row row of cell which has been updated
   * @param column column of cell which has been updated
   * @see TableModelEvent
   * @see EventListenerList
   */
  public void fireTableCellUpdated(int row, int column) {
    fireTableChanged(new TableModelEvent(this, row, row, column));
  }

  /**
   * Forwards the given notification event to all
   * <code>TableModelListeners</code> that registered themselves as listeners
   * for this table model.
   * 
   * @param e the event to be forwarded
   * 
   * @see #addTableModelListener
   * @see TableModelEvent
   * @see EventListenerList
   */
  public void fireTableChanged(TableModelEvent event) {
    for (TableModelListener listener : listenerList) {
      listener.tableChanged(event);
    }
  }

  /**
   * Inserts the specified element at the specified position.
   * 
   * @param index index at which the specified element is to be inserted
   * @param element element to be inserted
   * @throws ArrayIndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
   */
  @Override
  public void add(int index, Vector<Object> element) {
    super.add(index, element);
    fireTableRowsInserted(index, index);
  }

  /**
   * Appends the specified element to the end of this model.
   * 
   * @param e element to be appended to this model
   * @return {@code true} (as specified by {@link Collection#add})
   */
  @Override
  public boolean add(Vector<Object> e) {
    boolean result = super.add(e);
    if (result) {
      int index = super.size() - 1;
      fireTableRowsInserted(index, index);
      return true;
    }
    return false;
  }

  /**
   * Appends all of the elements in the specified Collection to the end of this
   * model, in the order that they are returned by the specified Collection's
   * Iterator.
   * 
   * @param c elements to be inserted
   * @return {@code true} if this model changed as a result of the call
   * @throws NullPointerException if the specified collection is null
   */
  public boolean addAll(Collection<? extends Vector<Object>> c) {
    return addAll(super.size(), c);
  }

  /**
   * Inserts all of the elements in the specified Collection into this model at
   * the specified position.
   * 
   * @param index index at which to insert the first element from the specified
   *          collection
   * @param c elements to be inserted
   * @return {@code true} if this model changed as a result of the call
   * @throws ArrayIndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
   * @throws NullPointerException if the specified collection is null
   */
  public boolean addAll(int index, Collection<? extends Vector<Object>> c) {
    if (super.addAll(index, c)) {
      fireTableRowsInserted(index, index + c.size() - 1);
      return true;
    }
    return false;
  }

  /**
   * Adds the specified element to the end of this model.
   * 
   * @param obj the component to be added
   * @deprecated Replaced by {@link #add(Object)}
   */
  @Override
  public void addElement(Vector<Object> o) {
    add(o);
  }

  /**
   * Removes all of the elements from this model.
   */
  @Override
  public void clear() {
    int index1 = super.size() - 1;
    super.clear();
    fireTableRowsDeleted(0, index1);
  }

  /**
   * Returns the number of elements in this model.
   * 
   * @return the number of elements in this model
   * @deprecated Replaced by {@link #getRowCount()}
   */
  public int size() {
    return super.size();
  }

  /**
   * Gets the size of the list.
   * 
   * @return the number of elements currently in the list
   * @see org.gwt.mosaic.ui.client.list.ListModel#getSize()
   */
  public int getRowCount() {
    return super.size();
  }

  /**
   * Replaces the element at the specified position in this Vector with the
   * specified element.
   * 
   * @param index
   * @param element
   * @return
   */
  @Override
  public Vector<Object> set(int index, Vector<Object> element) {
    Vector<Object> result = super.set(index, element);
    fireTableRowsUpdated(index, index);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.table.TableModel#getValueAt(int, int)
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    Vector<?> element = super.elementAt(rowIndex);
    return element.elementAt(columnIndex);
  }

  /* (non-Javadoc)
   * @see org.gwt.mosaic.ui.client.table.TableModel#setValueAt(java.lang.Object, int, int)
   */
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    Vector<Object> element = super.elementAt(rowIndex);
    element.setElementAt(value, columnIndex);
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#remove(int)
   */
  @Override
  public Vector<Object> remove(int index) {
    Vector<Object> element = super.remove(index);
    fireTableRowsDeleted(index, index);
    return element;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeAll(java.util.Collection)
   */
  @Override
  public boolean removeAll(Collection<?> c) {
    int index1 = super.size() - 1;
    boolean result = super.removeAll(c);
    if (result) {
      fireTableRowsDeleted(0, index1);
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeAllElements()
   */
  @Override
  public void removeAllElements() {
    clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeElement(java.lang.Object)
   */
  @Override
  public boolean removeElement(Object o) {
    return remove(o);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeElementAt(int)
   */
  @Override
  public void removeElementAt(int index) {
    remove(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#setSize(int)
   */
  @Override
  public void setSize(int size) {
    // TODO
  }
}
