package org.gwt.mosaic.ui.client.treetable;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;

import org.gwt.mosaic.ui.client.table.TableModelHelper.ColumnSortList;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;

/**
 * Request object taking care of the tree nodes state
 */
@SuppressWarnings("serial")
public class TreeRequest extends Request implements IsSerializable {
  private Set<String> invertedNodes;
  private boolean open, flattened;

  public TreeRequest() {
  }

  public TreeRequest(int startRow, int numRows, ColumnSortList columnSortList,
  /* ColumnFilterList columnFilterList, */boolean open,
      Set<String> invertedNodes, boolean flattened) {
    super(startRow, numRows, columnSortList/* , columnFilterList */);
    this.open = open;
    this.flattened = flattened;
    this.invertedNodes = invertedNodes;
  }

  public Set<String> getInvertedNodes() {
    return invertedNodes;
  }

  public boolean isOpen() {
    return open;
  }

  public boolean isFlattened() {
    return flattened;
  }
}