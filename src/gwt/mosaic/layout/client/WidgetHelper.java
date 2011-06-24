package gwt.mosaic.layout.client;

import gwt.mosaic.core.client.BoxModel;
import gwt.mosaic.core.client.ComputedStyle;
import gwt.mosaic.core.client.Options;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class WidgetHelper {

  /**
   * Call this to resize a widget, or after its size has changed.
   * <p>
   * When {@code changeSize} is specified, it changes the margin box of the
   * widget and forces it to re-layout if widget extends {@link LayoutWidget}.
   * <p>
   * If {@code resultSize} is specified it indicates the size the widget will
   * become after {@code changeSize} has been applied.
   * <p>
   * When {@code changeSize} is {@code null}, indicates that the caller has
   * already changed the size of the widget, or perhaps it changed because the
   * browser window was resized.
   * <p>
   * If {@code resultSize} is also specified it indicates the size the widget
   * has become.
   * <p>
   * In either mode, this method also:
   * <ol>
   * <li>Sets the border box and content box to the new size of the widget.
   * Queries the current DOM node size if necessary.</li>
   * <li>Calls layout() to resize contents (and maybe adjust widgets) if widget
   * extends {@link LayoutPanel}.
   * </ol>
   * 
   * @param w
   * @param changeSize
   * @param resultSize
   */
  public static void resize(Widget w, Options.Bounds changeSize, Options.Dimensions resultSize) {
    Element elem = w.getElement();
    ComputedStyle cs = ComputedStyle.get(elem);

    // set the margin box size, unless it wasn't specified, in which case use
    // current size
    if (changeSize != null) {
      BoxModel.setMarginBox(elem, changeSize, cs);

      // set offset of the node
      if (changeSize.top != null) {
        elem.getStyle().setTop(changeSize.top, Unit.PX);
      }
      if (changeSize.left != null) {
        elem.getStyle().setLeft(changeSize.left, Unit.PX);
      }
    }

    // If either height or width wasn't specified by the user, then query node
    // for it. But note that setting the margin box and then immediately
    // querying dimensions may return inaccurate results, so try not to depend
    // on it.
    Options.Dimensions mb = resultSize != null ? resultSize : new Options.Dimensions();
    // TODO changeSize overrides resultSize
    if (mb.height == null || mb.width == null) {
      // Just use margin box to fill in missing values.
      // BoxModel.getMarginBox(elem, computedStyle);
    }

    // Compute and save the size of my border box and content box
    // (w/out calling BoxModel.contentBox() since that may fail if size recently
    // set).
    Options.Bounds me = BoxModel.getMarginExtends(elem, cs);
    Options.Bounds be = BoxModel.getBorderExtends(elem, cs);
    Options.Dimensions bb =
        new Options.Dimensions(mb.width - (me.width + be.width), mb.height
            - (mb.height + be.height));
    Options.Bounds pe = BoxModel.getPadExtends(elem, cs);
    elem.setPropertyObject("_contentBox", new Options.Bounds(cs.getPaddingLeft(), cs
        .getPaddingTop(), bb.width - pe.width, bb.height - pe.height));

    // Callback for widget to adjust size of children
    if (w instanceof LayoutWidget) {
      ((LayoutWidget) w).layout();
    } else if (w instanceof RequiresResize) {
      ((RequiresResize) w).onResize();
    }
  }

}
