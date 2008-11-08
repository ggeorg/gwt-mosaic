package org.gwt.mosaic.core.client.impl;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.Element;

public class DOMImplSafari extends DOMImpl {
  /**
   * Gets the cell index of a cell within a table row.
   * 
   * The cellIndex property is not defined in Safari, so we must calculate the
   * cell index manually.
   * 
   * @param td the cell element
   * @return the cell index
   */
  @Override
  public int getCellIndex(Element td) {
    return DOM.getChildIndex(DOM.getParent(td), td);
  }
}
