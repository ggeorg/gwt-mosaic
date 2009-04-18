package org.gwt.mosaic.ui.client.list;

/**
 * 
 * 
 * @author ggeorg
 *
 * @param <T>
 */
public interface ComboBoxModel<T> extends ListModel<T> {

  void setSelectedItem(T item);
  
  T getSelectedItem();
}
