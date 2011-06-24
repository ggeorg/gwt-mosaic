package gwt.mosaic.layout.client;

import gwt.mosaic.core.client.BoxModel;
import gwt.mosaic.core.client.ComputedStyle;
import gwt.mosaic.core.client.Options;

import java.util.Iterator;
import java.util.Map;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for a container widget which is responsible for laying out its
 * children. Widgets that extend this class must define {@link #layout()} to
 * manage placement and sizing of the children.
 * <p>
 * This widget implements {@link ProvidesResize} indicating that that this
 * widget is going to call {@link RequiresResize#onResize()} on its child
 * widgets, setting their size, when they become visible.
 */
public abstract class LayoutWidget extends Composite implements ProvidesResize, RequiresResize,
    HasWidgets {

  /**
   * This style name is applied to the widget's DOM node and also may be used to
   * generate names for sub nodes.
   */
  protected String baseStyleName;
  
  protected Options.Bounds borderBox, contentBox;

  protected LayoutWidget() {
    initWidget(new AbsolutePanel() {
      @Override
      protected void setWidgetPositionImpl(Widget w, int left, int top) {
        Element h = w.getElement();
        // if (left == -1 && top == -1) {
        // changeToStaticPositioning(h);
        // } else {
        DOM.setStyleAttribute(h, "position", "absolute");
        DOM.setStyleAttribute(h, "left", left + "px");
        DOM.setStyleAttribute(h, "top", top + "px");
        // }
      }

      @Override
      public void add(Widget w) {
        super.add(w);
        onChildAdded(w);
      }

      @Override
      public boolean remove(Widget w) {
        boolean result = super.remove(w);
        if (result) {
          onChildRemoved(w);
        }
        return result;
      }
    });

    String cn = getClass().getName();
    cn = cn.substring(cn.lastIndexOf('.') + 1);
    baseStyleName = "m-" + cn;
  }

  /**
   * Called after all the widgets have been instantiated and their DOM nodes
   * have been inserted.
   * <p>
   * Widgets should override this method to do any initialization dependent on
   * other widgets existing, and then call this superclass method to finish
   * things off.
   * <p>
   * {@link #startup()} in subclasses should't do anything size related because
   * the size of the widget hand't been set yet.
   */
  protected void onLoad() {
    // If I am not being controlled by a parent layout widget...
    Widget parent = getParent();
    if (!(parent instanceof ProvidesResize)) {
      // Do recursive sizing and layout of all my descendants
      // (passing in no argument to resize means that it has to glean the
      // size itself)
      onResize();

      // Since my parent isn't a layout container, and my style *may be*
      // width=height=100% or something similar (either set directly or
      // via CSS class), monitor when my size changes so that I can
      // re-layout. For browsers where I can't directly monitor when my
      // size changes, monitor when the viewport changes size, which *may*
      // indicate a size change for me.
      Window.addResizeHandler(new ResizeHandler() {
        @Override
        public void onResize(ResizeEvent event) {
          LayoutWidget.this.onResize();
        }
      });
    }
  }

  /**
   * Call this to indicate that the size of this widget has changed.
   * <p>
   * This method also:
   * <ol>
   * <li>Sets this.borderBox and this.contentBox to the new size of the widget
   * by querying the current DOM node size.</li>
   * <li>Class {@link #layout()} to resize contents (and maybe adjust child
   * widgets).</li>
   * </ol>
   */
  @Override
  public void onResize() {
    resize(null, null);
  }

  /**
   * Call this to resize a widget, or after its size has changed.
   * <p>
   * When {@code changeSize} is specified, it changes the marginBox of this
   * widget and forces it to re-layout its contents accordingly.
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
   * <li>Sets the {@link #borderBox} and {@link #contentBox} to the new size of the widget.
   * Queries the current DOM node size if necessary.</li>
   * <li>Calls {@link #layout()} to resize contents (and maybe adjust widgets) if widget
   * extends {@link LayoutPanel}.
   * </ol>
   * 
   * @param w
   * @param changeSize
   * @param resultSize
   */
  public void resize(Options.Bounds changeSize, Options.Dimensions resultSize) {
    Element elem = getElement();
    ComputedStyle cs = ComputedStyle.get(elem);

    // set margin box size, unless it wasn't specified, in which use current
    // size
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
    layout();
  }

  /**
   * Widgets override this method to size and position their contents/children.
   * When this is called {@code _contentBox} is guaranteed to be set (see
   * {@link WidgetHelper#resize(Widget, gwt.mosaic.core.client.Options.Bounds, gwt.mosaic.core.client.Options.Dimensions)}
   * .
   * <p>
   * This is called after {@link #startup()}, and also when the widget's size
   * has been changed.
   */
  protected abstract void layout();

  /**
   * 
   */
  protected abstract void setupChild(Widget w);

  /**
   * 
   * <p>
   * {@code Widget}s can be added to a layout widget before or after it's been
   * started (via the startup() call on it, or an ancestor in it's hierarchy).
   * Thus {@code add()} and {@link #remove(Widget)} must work before or after
   * the widget has been started.
   */
  @Override
  public final void add(Widget w) {
    ((AbsolutePanel) getWidget()).add(w);
  }

  /**
   * 
   */
  @Override
  public final boolean remove(Widget w) {
    return ((AbsolutePanel) getWidget()).remove(w);
  }

  @Override
  public final void clear() {
    ((AbsolutePanel) getWidget()).clear();
  }

  @Override
  public Iterator<Widget> iterator() {
    return ((AbsolutePanel) getWidget()).iterator();
  }

  protected void onChildAdded(Widget w) {
    if (isAttached()) {
      setupChild(w);
    }
  }

  protected void onChildRemoved(Widget w) {
    w.removeStyleName(baseStyleName + "-child");
  }

  protected void size(Widget w, Options.Bounds dim) {
//    // size the child
//    Options.Dimensions newSize = WidgetHelper.resize(w, dim, null);
//
//    // record child's size
//    if (newSize != null) {
//      // if the child returned it's new size then use that
//
//    } else {
//      // otherwise, call marginBox(), but favor our own numbers when we have
//      // them - the browser lies sometimes
//      Options.Bounds mb = BoxModel.getMarginBox(w.getElement(), null);
//
//    }
  }

  // -----------------------------------------------------------------------

  private Map<String, Object> layoutProperties;

  public Object getLayoutProperty(String key) {
    return layoutProperties.get(key);
  }

  public Object setLayoutProperty(String key, Object value) {
    Object oldValue = layoutProperties.get(key);
    if (value == null) {
      layoutProperties.remove(key);
    } else {
      layoutProperties.put(key, value);
    }
    return oldValue;
  }
}
