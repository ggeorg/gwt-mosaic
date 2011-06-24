package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Document;

public class EdgeToEdgeList extends Container {
  public EdgeToEdgeList() {
    super(Document.get().createULElement());
    setStyleName("mblEdgeToEdgeList");
  }
}
