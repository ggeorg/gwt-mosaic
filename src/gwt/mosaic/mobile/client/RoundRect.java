package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Document;

public class RoundRect extends Container {

  private boolean shadow = false;

  public RoundRect() {
    super(Document.get().createDivElement());
    setStyleName("mblRoundRect");
  }

  public boolean isShadow() {
    return shadow;
  }

  public void setShadow(boolean shadow) {
    if ((this.shadow = shadow)) {
      addStyleName("mblShadow");
    } else {
      removeStyleName("mblShadow");
    }
  }

}
