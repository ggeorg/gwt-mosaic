package org.mosaic.ui.client.event;

public interface TableColumnModelListener {

  /** Tells listeners that an element was added to the model. */
  public void columnAdded(TableColumnModelEvent e);
  
  /** Tells listeners that an element was removed from the model. */
  public void columnRemoved(TableColumnModelEvent e);
  
  /** Tells listeners that an element was repositioned. */
  public void columnMoved(TableColumnModelEvent e);

}
