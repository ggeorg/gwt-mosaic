package org.mosaic.ui.client;

import org.mosaic.core.client.DOM;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

public class ImageButton extends Widget implements SourcesClickEvents {

  private ClickListenerCollection clickListeners;

  private AbstractImagePrototype image;

  public ImageButton() {
    this(null);
  }

  public ImageButton(AbstractImagePrototype image) {
    setElement(DOM.createAnchor());
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);

    if (image != null) {
      this.image = image;
      getElement().setInnerHTML(image.getHTML());
    }

    setStyleName("mosaic-ImageButton");
  }

  public AbstractImagePrototype getImage() {
    return image;
  }

  public void setImage(AbstractImagePrototype image) {
    this.image = image;
  }

  public void addClickListener(ClickListener listener) {
    if (clickListeners == null) {
      clickListeners = new ClickListenerCollection();
    }
    clickListeners.add(listener);
  }

  @Override
  public void onBrowserEvent(Event event) {
    DOM.eventPreventDefault(event);
    if (DOM.eventGetType(event) == Event.ONCLICK) {
      if (clickListeners != null) {
        clickListeners.fireClick(this);
      }
    }
    event.cancelBubble(true);
  }

  public void removeClickListener(ClickListener listener) {
    if (clickListeners != null) {
      clickListeners.remove(listener);
    }
  }

}
