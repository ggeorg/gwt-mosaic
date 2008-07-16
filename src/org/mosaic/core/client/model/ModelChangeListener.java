package org.mosaic.core.client.model;

import java.util.EventListener;

public interface ModelChangeListener extends EventListener {

  /** Tells listeners that an element was added to the model. */
  public void elementAdded(ModelChangeEvent e);
  
  /** Tells listeners that an element was removed from the model. */
  public void elementRemoved(ModelChangeEvent e);
  
  /** Tells listeners that an element was repositioned. */
  public void elementMoved(ModelChangeEvent e);

}
