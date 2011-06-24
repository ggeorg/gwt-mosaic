package gwt.mosaic.mobile.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasAnimationEndHandlers extends HasHandlers {

  HandlerRegistration addAnimationEndHandler(AnimationEndHandler handler);
  
}
