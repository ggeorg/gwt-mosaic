package org.gwt.mosaic.ui.client.list;

public class ListColumn {
  private String name;
  
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

}
