package org.gwt.mosaic.ui.client.layout;

public class ColumnLayoutData extends LayoutData {
  private int flexibility;
  
  int calcWidth;

  public ColumnLayoutData() {
    this(1, false);
  }
  
  public ColumnLayoutData(boolean decorate) {
    this(1, decorate);
  }

  public ColumnLayoutData(int flex) {
    this(flex, false);
  }
  
  public ColumnLayoutData(int flex, boolean decorate) {
    super(decorate);
    setFlexibility(flex);
  }
  
  public ColumnLayoutData(String preferredWidth) {
    this(preferredWidth, false);
  }
  
  public ColumnLayoutData(String preferredWidth, boolean decorate) {
    super(decorate);
    setPreferredWidth(preferredWidth);
    setFlexibility(1);
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
