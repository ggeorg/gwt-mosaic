package org.gwt.mosaic.ui.client.table;

import com.google.gwt.user.client.EventListener;

/**
 * Defines the interface for an object that listens to changes in a
 * {@link TableModel}.
 * 
 * @author ggeorgopoulos.georgios(at)gmail.com
 * @see TableModel
 */
public interface TableModelListener extends EventListener {

  /**
   * This fine grain notification tells listeners the exact range of cells,
   * rows, or columns that changed.
   * 
   * @param event the event that notifies listeners for changes in a
   *          {@link TableModel}
   */
  public void tableChanged(TableModelEvent event);
}
