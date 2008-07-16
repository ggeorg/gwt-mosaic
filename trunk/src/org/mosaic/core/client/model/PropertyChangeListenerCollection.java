package org.mosaic.core.client.model;

import java.util.Vector;

public class PropertyChangeListenerCollection extends Vector<PropertyChangeListener> {
  private static final long serialVersionUID = 5152119467561494763L;

  public void firePropertyChangeEvent(Object source, String propertyName,
      Object oldValue, Object newValue) {
    PropertyChangeEvent event = new PropertyChangeEvent(source, propertyName, oldValue,
        newValue);
    firePropertyChangeEvent(event);
  }

  public void firePropertyChangeEvent(PropertyChangeEvent event) {
    for (PropertyChangeListener listener : this) {
      listener.onPropertyChange(event);
    }
  }
}
