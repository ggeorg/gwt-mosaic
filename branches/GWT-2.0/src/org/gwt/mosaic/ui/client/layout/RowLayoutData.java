package org.gwt.mosaic.ui.client.layout;

public class RowLayoutData extends LayoutData {
  private int flexibility;

  public RowLayoutData() {
    super(false);
    setFlexibility(1);
  }

  public RowLayoutData(int flex) {
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
