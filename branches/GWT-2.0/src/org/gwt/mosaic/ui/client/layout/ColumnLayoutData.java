package org.gwt.mosaic.ui.client.layout;

public class ColumnLayoutData extends LayoutData {
  private int flexibility;

  public ColumnLayoutData() {
    super(false);
    setFlexibility(1);
  }

  public ColumnLayoutData(int flex) {
    super(false);
    setFlexibility(flex);
  }

  /**
   * @return the flexibility
   */
  public int getFlexibility() {
    return flexibility;
  }

  /**
   * @param flexibility the flexibility to set
   */
  public void setFlexibility(int flexibility) {
    if (flexibility < 1) {
      throw new IllegalArgumentException("flexibility can't be < 1 ("
          + flexibility + ")");
    }
    this.flexibility = flexibility;
  }

}
