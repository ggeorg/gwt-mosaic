package gwt.mosaic.mobile.client;

import gwt.mosaic.core.client.BoxModel;
import gwt.mosaic.core.client.Options;

import java.util.Iterator;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * A layout container that splits the window horizontally or vertically.
 * <p>
 * TODO
 */
public class FixedSplitter extends Container implements RequiresResize, ProvidesResize {

  public static enum Orientation {
    HORIZONTAL {
      @Override
      public String getType() {
        return "H";
      }
    },
    VERTICAL {
      @Override
      public String getType() {
        return "V";
      }
    };

    public abstract String getType();
  };

  private Orientation orientation = Orientation.HORIZONTAL;

  protected FixedSplitter() {
    super(Document.get().createDivElement());
    setStyleName("mblFixedSpliter");

    // handle orientation & resize events
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public void setOrientation(Orientation orientation) {
    if (this.orientation != orientation) {
      Iterator<Widget> iter = iterator();
      while (iter.hasNext()) {
        Widget child = iter.next();
        child.removeStyleName("mblFixedSplitterPane" + this.orientation.getType());
        child.addStyleName("mblFixedSplitterPane" + orientation.getType());
      }
      this.orientation = orientation;
    }
  }

  @Override
  protected void setupChild(Widget child) {
    child.addStyleName("mblFixedSplitterPane" + orientation.getType());
  }

  @Override
  protected void childRemoved(Widget child) {
    child.removeStyleName("mblFixedSplitterPane" + orientation.getType());
  }

  protected void layout() {
    Window.setTitle(Window.getClientWidth()*Window.getClientHeight()+"");
    int widgetCount;
    if ((widgetCount = getWidgetCount()) == 0) {
      return;
    }
    Options.Bounds box = new Options.Bounds();
    int offset = 0;
    for (int i = 0; i < widgetCount; i++) {
      Widget child = getWidget(i);
      if (orientation == Orientation.HORIZONTAL) {
        box.left = offset;
      } else {
        box.top = offset;
      }
      BoxModel.setMarginBox(child.getElement(), box, null);
      if (i < widgetCount - 1) {
        if (orientation == Orientation.HORIZONTAL) {
          offset += BoxModel.getMarginBox(child.getElement(), null).width;
        } else {
          offset += BoxModel.getMarginBox(child.getElement(), null).height;
        }
      }
    }

    box = BoxModel.getMarginBox(getElement(), null);
    System.out.println(box);
    
    if (orientation == Orientation.HORIZONTAL) {
      box.width -= offset;
      box.left = box.top = box.height = null;
      BoxModel.setMarginBox(getWidget(widgetCount - 1).getElement(), box, null);
    } else {
      box.height -= offset;
      box.left = box.top = box.width = null;
      System.out.println(box);
      BoxModel.setMarginBox(getWidget(widgetCount - 1).getElement(), box, null);
    }

    for (int i = 0, n = getWidgetCount(); i < n; i++) {
      Widget child = getWidget(i);
      if (child instanceof RequiresResize) {
        ((RequiresResize) child).onResize();
      }
    }
  }

  @Override
  public void onResize() {
    layout();
    super.onResize();
  }

}
