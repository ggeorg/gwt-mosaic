package gwt.mosaic.mobile.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasAnimationStartHandlers extends HasHandlers {

  HandlerRegistration addAnimationStartHandler(AnimationStartHandler handler);
  
}
