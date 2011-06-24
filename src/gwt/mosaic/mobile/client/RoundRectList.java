package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Document;

public class RoundRectList extends Container {
  public RoundRectList() {
    super(Document.get().createULElement());
    setStyleName("mblRoundRectList");
  }
}
