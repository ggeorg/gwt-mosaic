package org.mosaic.ui.client.event;

import org.mosaic.core.client.model.ModelChangeEvent;

public class TableColumnModelEvent extends ModelChangeEvent {
  private static final long serialVersionUID = -3254470094177500005L;

  public TableColumnModelEvent(Object source, int from, int to) {
    super(source, from, to);
  }

  public TableColumnModelEvent(ModelChangeEvent event) {
    super(event.getSource(), event.getFromIndex(), event.getToIndex());
  }

}
