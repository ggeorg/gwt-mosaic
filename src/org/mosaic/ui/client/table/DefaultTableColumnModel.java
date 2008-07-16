package org.mosaic.ui.client.table;

import java.util.Iterator;
import java.util.Vector;

import org.mosaic.core.client.model.ModelChangeEvent;
import org.mosaic.core.client.model.ModelChangeListener;
import org.mosaic.core.client.model.ModelElement;
import org.mosaic.ui.client.event.TableColumnModelEvent;
import org.mosaic.ui.client.event.TableColumnModelListener;

public class DefaultTableColumnModel extends ModelElement implements TableColumnModel,
/* PropertyChangeListener, */ModelChangeListener /* , ListSelectionListener */{
  private static final long serialVersionUID = 8226823396234629343L;

  public static final String COLUMN_SELECTION_ALLOWED = "columnSelectionAllowed";

  public DefaultTableColumnModel() {
    super();
    
    setColumnSelectionAllowed(false);
  }

  public void addColumn(TableColumn column) {
    super.addElement(column);
  }

  public TableColumn getColumn(int index) {
    return (TableColumn) super.getElement(index);
  }

  public int getColumnCount() {
    return super.getElementCount();
  }

  public int getColumnIndex(TableColumn column) {
    return super.getElementIndex(column);
  }

  public Iterator<TableColumn> getColumns() {
    final Iterator<ModelElement> iter = super.getElements();
    return new Iterator<TableColumn>() {
      public boolean hasNext() {
        return iter.hasNext();
      }

      public TableColumn next() {
        return (TableColumn) iter.next();
      }

      public void remove() {
        iter.remove();
      }
    };
  }

  public boolean getColumnSelectionAllowed() {
    return (Boolean) getProperty(COLUMN_SELECTION_ALLOWED);
  }

  public int getSelectedColumnCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  public int[] getSelectedColumns() {
    // TODO Auto-generated method stub
    return null;
  }

  public void moveColumn(int index, int newIndex) {
    super.moveElement(index, newIndex);
  }

  public void removeColumn(TableColumn column) {
    super.removeElement(column);
  }

  public void setColumnSelectionAllowed(boolean flag) {
    setProperty(COLUMN_SELECTION_ALLOWED, flag);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.table.TableColumnModel#addColumnModelListener(org.mosaic.ui.client.event.TableColumnModelListener)
   */
  public void addColumnModelListener(TableColumnModelListener listener) {
    if (listeners == null) {
      listeners = new Vector<TableColumnModelListener>();
      super.addModelChangeListener(this);
    }
    if (listeners.indexOf(listener) == -1) {
      listeners.add(listener);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.table.TableColumnModel#removeColumnModelListener(org.mosaic.ui.client.event.TableColumnModelListener)
   */
  public void removeColumnModelListener(TableColumnModelListener listener) {
    if (listeners != null) {
      listeners.remove(listener);
    }
  }

  protected Vector<TableColumnModelListener> listeners;

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.core.client.model.ModelChangeListener#elementAdded(org.mosaic.core.client.model.ModelChangeEvent)
   */
  public void elementAdded(ModelChangeEvent e) {
    TableColumnModelEvent event = new TableColumnModelEvent(e);
    for (TableColumnModelListener listener : listeners) {
      listener.columnAdded(event);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.core.client.model.ModelChangeListener#elementMoved(org.mosaic.core.client.model.ModelChangeEvent)
   */
  public void elementMoved(ModelChangeEvent e) {
    TableColumnModelEvent event = new TableColumnModelEvent(e);
    for (TableColumnModelListener listener : listeners) {
      listener.columnMoved(event);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.core.client.model.ModelChangeListener#elementRemoved(org.mosaic.core.client.model.ModelChangeEvent)
   */
  public void elementRemoved(ModelChangeEvent e) {
    TableColumnModelEvent event = new TableColumnModelEvent(e);
    for (TableColumnModelListener listener : listeners) {
      listener.columnRemoved(event);
    }
  }

}
