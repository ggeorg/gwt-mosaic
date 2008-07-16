package org.mosaic.core.client.model;

import java.util.EventObject;

public class ModelChangeEvent extends EventObject {
  private static final long serialVersionUID = 1417961656197328010L;

  protected final int fromIndex;

  protected final int toIndex;

  public ModelChangeEvent(Object source) {
    this(source, -1, -1);
  }

  public ModelChangeEvent(Object source, int from, int to) {
    super(source);
    fromIndex = from;
    toIndex = to;
  }

  public int getFromIndex() {
    return fromIndex;
  }

  public int getToIndex() {
    return toIndex;
  }

}
