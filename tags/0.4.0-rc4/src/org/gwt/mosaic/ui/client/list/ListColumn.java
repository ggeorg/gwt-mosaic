package org.gwt.mosaic.ui.client.list;

public class ListColumn {
  private String name;
  private boolean sortable = true;
  
  public ListColumn() {
    super();
  }
  
  public ListColumn(String name) {
    super();
    this.name = name;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the sortable
   */
  public boolean isSortable() {
    return sortable;
  }

  /**
   * @param sortable the sortable to set
   */
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

}
