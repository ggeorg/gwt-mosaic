package org.mosaic.core.client.model;

import java.util.Vector;

public class ModelChangeListenerCollection extends Vector<ModelChangeListener> {
  private static final long serialVersionUID = 1716253883223087984L;

  public void fireElementAdded(ModelChangeEvent event) {
    for (ModelChangeListener listener : this) {
      listener.elementAdded(event);
    }
  }

  public void fireElementRemoved(ModelChangeEvent event) {
    for (ModelChangeListener listener : this) {
      listener.elementRemoved(event);
    }
  }

  public void fireElementMoved(ModelChangeEvent event) {
    for (ModelChangeListener listener : this) {
      listener.elementMoved(event);
    }
  }

}
