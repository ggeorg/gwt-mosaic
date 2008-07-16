package org.mosaic.core.client.model;

import java.util.EventListener;

public interface PropertyChangeListener extends EventListener {
  void onPropertyChange(PropertyChangeEvent event);
}
