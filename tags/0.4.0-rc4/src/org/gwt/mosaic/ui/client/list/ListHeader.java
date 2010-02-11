package org.gwt.mosaic.ui.client.list;

import java.util.ArrayList;

public class ListHeader extends ArrayList<ListColumn> {
  private static final long serialVersionUID = -7303926643389910078L;

  public ListHeader() {
    super();
  }

  public ListHeader(String[] names) {
    super();

    if (names != null) {
      for (String name : names) {
        add(new ListColumn(name));
      }
    }
  }

}
