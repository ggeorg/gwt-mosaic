package org.gwt.mosaic.ui.client.layout;

import org.gwt.mosaic.ui.client.CollapsedListener;

public interface Collapsible {
  void addCollapsedListener(CollapsedListener listener);

  void removeCollapsedListener(CollapsedListener listener);
}
