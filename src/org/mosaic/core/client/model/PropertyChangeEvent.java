package org.mosaic.core.client.model;

import java.io.Serializable;
import java.util.EventObject;

public class PropertyChangeEvent extends EventObject implements Serializable {
  private static final long serialVersionUID = -8649777609924711940L;

  private String property;

  private Object oldValue;

  private Object newValue;

  public PropertyChangeEvent(Object source, String property, Object oldValue,
      Object newValue) {
    super(source);
    this.property = property;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public Object getSource() {
    return source;
  }

  public String getProperty() {
    return property;
  }

  public Object getOldValue() {
    return oldValue;
  }

  public Object getNewValue() {
    return newValue;
  }

}
