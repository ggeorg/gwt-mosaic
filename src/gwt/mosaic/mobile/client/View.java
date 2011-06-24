package gwt.mosaic.mobile.client;

import gwt.mosaic.core.client.EventHandle;
import gwt.mosaic.mobile.client.events.AnimationEndEvent;
import gwt.mosaic.mobile.client.events.AnimationEndHandler;
import gwt.mosaic.mobile.client.events.AnimationStartEvent;
import gwt.mosaic.mobile.client.events.AnimationStartHandler;
import gwt.mosaic.mobile.client.events.HasAnimationHandlers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * A widget that represents a view that occupies the full screen.
 * <p>
 * {@code View} acts as a container for any HTML and/or widgets. An entire HTML
 * page can have multiple {@code View} widgets and the user can navigate through
 * the views back and forth without page transitions.
 */
public class View extends Container implements HasWidgets, HasAnimationHandlers {

  public static enum Direction {
    FORWARD {
      @Override
      public String getType() {
        return "forward";
      }
    },
    REVERSE {
      @Override
      public String getType() {
        return "reverse";
      }
    };

    public abstract String getType();
  }

  public static enum Transition {
    SLIDE {
      @Override
      public String getType() {
        return "slide";
      }
    },
    FADE {
      @Override
      public String getType() {
        return "fade";
      }
    },
    FLIP {
      @Override
      public String getType() {
        return "flip";
      }
    };

    public abstract String getType();
  }

  private static final Map<String, View> viewMap = new HashMap<String, View>();

  public static View findByElementId(String id) {
    return viewMap.get(id);
  }

  private static View currentView = null;

  public static View getCurrentView() {
    return currentView;
  }

  /**
   * If {@code true}, the view is displayed at startup time.
   */
  private boolean selected = false;

  /**
   * If {@code true}, the scroll position is kept between views.
   */
  private boolean keepScrollPos = false;

  private boolean _started = false;

  private EventHandle webkitAnimationEndCallback;
  private EventHandle webkitAnimationStartCallback;

  public View() {
    super(Document.get().createDivElement());
    setStyleName("mblView");

    Style elemStyle = getElement().getStyle();
    elemStyle.setVisibility(Visibility.HIDDEN);

    addAnimationStartHandler(new AnimationStartHandler() {
      @Override
      public void onAnimationStart(AnimationStartEvent event) {
      }
    });

    addAnimationEndHandler(new AnimationEndHandler() {
      @Override
      public void onAnimationEnd(AnimationEndEvent event) {
        if (getStyleName().contains("out")) { // FIXME
          getElement().getStyle().setDisplay(Display.NONE);
        }
        removeStyleName("in");
        removeStyleName("out");
        removeStyleName("reverse");
        removeStyleName(Transition.FADE.getType());
        removeStyleName(Transition.FLIP.getType());
        removeStyleName(Transition.SLIDE.getType());
      }
    });
  }

  public String getElementId() {
    return getElement().getId();
  }

  public void setElementId(String id) {
    getElement().setId(id);
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean isKeepScrollPos() {
    return keepScrollPos;
  }

  public void setKeepScrollPos(boolean keepScrollPos) {
    this.keepScrollPos = keepScrollPos;
  }

  @Override
  protected void onLoad() {
    webkitAnimationEndCallback = EventHandle.connect(getElement(), "webkitAnimationEnd", this);
    webkitAnimationStartCallback = EventHandle.connect(getElement(), "webkitAnimationStart", this);
    startup();
  }

  @Override
  protected void onUnload() {
    webkitAnimationEndCallback.disconnect();
    webkitAnimationStartCallback.disconnect();
  }

  protected void startup() {
    if (_started) {
      return;
    }

    _started = true;

    Style elemStyle = getElement().getStyle();
    if (!selected) {
      elemStyle.setDisplay(Display.NONE);
    } else {
      View.currentView = View.this;
      onStartView();
    }
    elemStyle.setVisibility(Visibility.VISIBLE);

    // register view
    String id = getElementId();
    if (id != null && id.trim().length() > 0) {
      viewMap.put(id, this);
    }
  }

  protected void onStartView() {
    // Stub function to connect to from your application.
    // Called only when this view is shown at startup time.
  }

  /**
   * Method to perform the various types of view transitions, such as fade,
   * slide, and flip.
   * 
   * @param moveTo The destination view to transition the current view.
   * @param dir The transition direction.
   * @param transition The type of transition to perform.
   */
  public void performTransition(View moveTo, Direction dir, Transition transition) {
    if (moveTo == null) {
      return;
    }
    Element fromNode = getElement();
    Element toNode = moveTo.getElement();
    toNode.getStyle().setVisibility(Visibility.HIDDEN);
    toNode.getStyle().setProperty("display", "");
    // perform view transition keeping the scroll position
    if (keepScrollPos) {
      int scrollTop = Document.get().getBody().getScrollTop();
      if (dir == Direction.FORWARD) {
        toNode.getStyle().setTop(0, Unit.PX);
        if (scrollTop > 1) {
          fromNode.getStyle().setTop(-scrollTop, Unit.PX);
          // TODO something related with hide address bar
        }
      } else {
        if (scrollTop > 1 || toNode.getOffsetTop() != 0) {
          int toTop = -toNode.getOffsetTop();
          toNode.getStyle().setTop(0, Unit.PX);
          fromNode.getStyle().setTop(toTop - scrollTop, Unit.PX);
          // TODO something related with hide address bar
        }
      }
    } else {
      toNode.getStyle().setTop(0, Unit.PX);
    }

    toNode.getStyle().setDisplay(Display.NONE);
    toNode.getStyle().setVisibility(Visibility.VISIBLE);
    _doTransition(moveTo, dir, transition);
  }

  private void _doTransition(View moveTo, Direction dir, Transition transition) {
    Element fromNode = getElement();
    Element toNode = moveTo.getElement();
    String rev = (dir == Direction.REVERSE) ? " reverse" : "";
    toNode.getStyle().setProperty("display", "");
    if (transition == null) {
      fromNode.getStyle().setDisplay(Display.NONE);
    } else {
      fromNode.addClassName(transition.getType() + " out" + rev);
      toNode.addClassName(transition.getType() + " in" + rev);
    }
  }

  @Override
  public HandlerRegistration addAnimationEndHandler(AnimationEndHandler handler) {
    return addDomHandler(handler, AnimationEndEvent.getType());
  }

  @Override
  public HandlerRegistration addAnimationStartHandler(AnimationStartHandler handler) {
    return addDomHandler(handler, AnimationStartEvent.getType());
  }

}
