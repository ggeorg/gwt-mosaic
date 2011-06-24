package gwt.mosaic.mobile.client.events;

import com.google.gwt.event.dom.client.DomEvent;

public class AnimationStartEvent extends DomEvent<AnimationStartHandler> {

  private static final Type<AnimationStartHandler> TYPE =
      new Type<AnimationStartHandler>("webkitAnimationStart", new AnimationStartEvent());

  public static Type<AnimationStartHandler> getType() {
    return TYPE;
  }

  protected AnimationStartEvent() {
  }

  @Override
  public final Type<AnimationStartHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(AnimationStartHandler handler) {
    handler.onAnimationStart(this);
  }

}
