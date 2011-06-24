package gwt.mosaic.layout.client;

import gwt.mosaic.core.client.BoxModel;
import gwt.mosaic.core.client.Options;

import java.util.Iterator;
import java.util.Locale;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides layout in up to 5 regions, a mandatory center with optional borders
 * along its 4 sides.
 * <p>
 */
public class BorderContainer extends LayoutWidget {

  public static final String REGION_KEY = "region";

  /**
   * The design options of the {@link BorderContainer}.
   */
  public enum Design {
    /**
     * The <em>top</em> and <em>bottom</em> extends the full width of the
     * container (default).
     */
    HEADLINE,

    /**
     * The <em>left</em> and <em>right</em> sides extends from top to bottom.
     */
    SIDEBAR
  }

  public enum Region {
    LEFT, RIGHT, TOP, BOTTOM, CENTER, LEADING, TRAILING
  }

  /** Which design is used for the layout. */
  private Design design;

  /**
   * Give each pane a border and margin. When {@code false} , only re-sizable
   * panes have a gutter (i.e. draggable splitter) for resizing.
   */
  private boolean gutters = true;

  /**
   * Specifies whether splitters resize as you drag ({@code true}) or only upon
   * mouse-up ({@code false}).
   */
  private boolean liveSplitters = true;

  @Override
  protected void onLoad() {
    Iterator<Widget> iter = iterator();
    while (iter.hasNext()) {
      Widget w = iter.next();
      setupChild(w);
    }
    super.onLoad();
  }

  @Override
  protected void setupChild(Widget w) {
    Region region = getRegion(w);
    if (region != null) {
      w.addStyleName(baseStyleName + "Pane");

      boolean ltr = !LocaleInfo.getCurrentLocale().isRTL();
      if (region == Region.LEADING) {
        region = ltr ? Region.LEFT : Region.RIGHT;
      }
      if (region == Region.TRAILING) {
        region = ltr ? Region.RIGHT : Region.LEFT;
      }

      // Create draggable splitter for resizing pane, or alternately if
      // splitter=false but BorderContainer.gutters=true then insert dummy div
      // just for spacing.

      if (region != Region.CENTER && (getSplitter(w) || gutters) && getSplitterWidget(w) == null) {

      }

      setRegion(w, region);
    }
  }

  private Region getRegion(Widget w) {
    return (w instanceof LayoutWidget) ? (Region) ((LayoutWidget) w).getLayoutProperty(REGION_KEY)
        : null;
  }

  private void setRegion(Widget w, Region region) {
    if (w instanceof LayoutWidget) {
      ((LayoutWidget) w).setLayoutProperty(REGION_KEY, region);
    }
  }

  private Object getSplitterWidget(Widget w) {
    // TODO Auto-generated method stub
    return null;
  }

  private boolean getSplitter(Widget w) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected void layout() {
    layoutChildren(null, null);
  }

  @Override
  protected void onChildAdded(Widget w) {
    super.onChildAdded(w);
    if (isAttached()) {
      layout();
    }
  }

  @Override
  protected void onChildRemoved(Widget w) {
    Region region = getRegion(w);
    // Widget splitter = getSplitter(w);
    // if(splitter != null) {
    // splitter.destory();
    // setSplitterWidget(null);
    // }
    super.onChildRemoved(w);

    if (isAttached()) {
      layout();
    }

    // Clean up whatever style changes we made to the child pane.
    // Unclear how height and width should be handled.
    w.removeStyleName(baseStyleName + "Pane");
    Style s = w.getElement().getStyle();
    s.setProperty("top", "auto");
    s.setProperty("bottom", "auto");
    s.setProperty("left", "auto");
    s.setProperty("right", "auto");
    s.setPosition(Position.STATIC);
    if (region == Region.TOP || region == Region.BOTTOM) {
      s.setProperty("width", "auto");
    } else {
      s.setProperty("height", "auto");
    }
  }

  @Override
  public Iterator<Widget> iterator() {
    // TODO change it to only return real children, not the splitters!
    return super.iterator();
  }

  /**
   * This is the main routine for setting size/position of each child.
   * <p>
   * With no arguments, measures the height of top/bottom panes, the width of
   * left/right panes, and then sizes all panes accordingly.
   * <p>
   * With {@code changedRegion} specified it changes that region's width/height
   * to {@code changedRegionSize} and then resizes other regions that were
   * affected.
   * 
   * @param changedChildId The Id of the child which should be resized because
   *          splitter was dragged.
   * @param changeChildSize The new width/height (in pixels) to make specified
   *          child.
   */
  private void layoutChildren(Object changedChildId, Object changeChildSize) {
    // if(this._borderBox || !this._borderBox.h) {
    // We are currently hidden, or we haven't been sized by our parent yet.
    // Abort. Someone will resize us later.
    // TODO return;
    // }

    // Compute the box in which to lay out my children
    Options.Bounds pe = BoxModel.getPadExtends(getElement(), null);
    Options.Bounds dim =
        new Options.Bounds(pe.left, pe.top, borderBox.width - pe.width, borderBox.height
            - pe.height);

    // Layout the children, possibly changing size due to a splitter drag
    Iterator<Widget> iter = super.iterator();
    while (iter.hasNext()) {
      Widget w = iter.next();

      Element elem = w.getElement();
      Region region = getRegion(w);

      // copy dim because we are going to modify it
      Options.Bounds _dim = new Options.Bounds(dim);

      // set w to upper left corner of unused space; may move it later
      Style elemStyle = w.getElement().getStyle();
      elemStyle.setLeft(_dim.left, Unit.PX);
      elemStyle.setTop(_dim.top, Unit.PX);
      elemStyle.setPosition(Position.ABSOLUTE);

      // Size adjustments to make to this widget
      Options.Dimensions sizeSetting = new Options.Dimensions();

      // Check for optional size adjustments due to splitter drag (height
      // adjustment for top/bottom align panes and width adjustment for
      // left/right align panes).
      // TODO

      // set size && adjust record of remaining space.
      // not that setting the width of a <div> may effect its height.
//      if (region == Region.TOP || region == Region.BOTTOM) {
//        sizeSetting.width = _dim.width;
//        size(w, sizeSetting);
//        _dim.height -= WidgetHelper.getHeight(w);
//        if (region == Region.TOP) {
//          _dim.top += WidgetHelper.getHeight(w);
//        } else {
//          elemStyle.setTop(_dim.top + _dim.height, Unit.PX);
//        }
//      } else if (region == Region.LEFT || region == Region.RIGHT) {
//        sizeSetting.height = _dim.height;
//        size(w, sizeSetting);
//        if (region == Region.LEFT) {
//          _dim.left += WidgetHelper.getWidth(w);
//        } else {
//          elemStyle.setLeft(_dim.left + _dim.width, Unit.PX);
//        }
//      } else if (region == Region.CENTER) {
//        size(w, _dim);
//      }
    }
  }

  private class Wrapper {
    Widget pane;
    int regionWidth;

  }

}
