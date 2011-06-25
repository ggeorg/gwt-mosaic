package gwt.mosaic.core.client;

import gwt.mosaic.core.client.Options.Bounds;

import javax.swing.text.html.Option;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;

public class BoxModel {

  /**
   * Returns object with special values specifically useful for node fitting.
   * <p>
   * The returns object is mapped with a {@link Option.Bounds} instance with:
   * <ul>
   * <li>left/top = left/top padding (respectively)</li>
   * <li>width = the total of the left and right padding</li>
   * <li>height = the total of the top and bottom padding</li>
   * </ul>
   * <p>
   * Normally application code will not need to invoke this directly, and will
   * use the box functions instead.
   * 
   * @param elem
   * @param s
   * @return
   */
  public static Options.Bounds getPadExtends(Element elem, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    int left = s.getPaddingLeft();
    int top = s.getPaddingTop();
    int width = left + s.getPaddingRight();
    int height = top + s.getPaddingBottom();
    return new Options.Bounds(left, top, width, height);
  }

  public static Options.Bounds getBorderExtends(Element elem, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    int left = s.getBorderLeftWidth();
    int top = s.getBorderTopWidth();
    int width = left + s.getBorderRightWidth();
    int height = left + s.getBorderBottomWidth();
    return new Options.Bounds(left, top, width, height);
  }

  /**
   * Returns an object with properties useful for box fitting with regards to
   * padding.
   * 
   * <ul>
   * <li>left/top = the sum of left/top padding and left/top border
   * (respectively)</li>
   * <li>width = the sum of the left and right padding and border</li>
   * <li>height = the sum of the top and bottom padding and border</li>
   * </ul>
   * 
   * The width/height are used for calculating boxes. Normally application code
   * will not need to invoke this directly, and will use the box functions
   * instead.
   */
  public static Options.Bounds getPadBorderExtends(Element elem, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    Bounds p = getPadExtends(elem, s);
    Bounds b = getBorderExtends(elem, s);
    return new Bounds(p.left + b.left, p.top + b.top, p.width + b.width, p.height + b.height);
  }

  /**
   * Returns an object with properties for box fitting with regards to box
   * margins (i.e. the outer-box).
   * 
   * <ul>
   * <li>left/top = marginLeft, marginTop, respectively</li>
   * <li>width = total width, margin inclusive</li>
   * <li>height = total height, margin inclusive</li>
   * </ul>
   * 
   * The width/height are used for calculating boxes. Normally application code
   * will not need to invoke this directly, and will use the box functions
   * instead.
   */
  public static Options.Bounds getMarginExtends(Element elem, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    int l = s.getMarginLeft();
    int t = s.getMarginTop();
    int r = s.getMarginRight();
    int b = s.getMarginBottom();
    if (UserAgent.isWebkit() && !"absolute".equals(s.getProperty("position"))) {
      // FIXME: Safari's version of the computed right margin is the space
      // between our right edge and the right edge of our offsetParent.
      // What we are looking for is the actual margin value as determined by
      // CSS. Hack solution is to assume left/right margins are the same.
      r = l;
    }
    return new Options.Bounds(l, t, l + r, t + b);
  }

  /**
   * Returns an object that encodes the width, height, left and top positions of
   * the node's margin box.
   */
  public static Options.Bounds getMarginBox(Element elem, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    Bounds me = getMarginExtends(elem, s);
    int left = elem.getOffsetLeft() - me.left;
    int top = elem.getOffsetTop() - me.top;
    Element p = elem.getParentElement().cast();
    if (UserAgent.isMozilla()) {
      // If offsetParent has a computed overflow != visible, the offsetLeft is
      // decreased by the parent's border. We don't want to compute the parent's
      // style, so instead we examine node's computed left/top which is more
      // stable.
    } else if (UserAgent.isOpera()
        || ((UserAgent.isIE8() || UserAgent.isIE9()) && Document.get().isCSS1Compat())) {
      // On Opera and IE 8, offsetLeft/Top includes the parent's border
      if (p != null) {
        Options.Bounds be = getBorderExtends(p, null);
        left -= be.left;
        top -= be.top;
      }
    }
    return new Options.Bounds(left, top, elem.getOffsetWidth() + me.width, elem.getOffsetHeight()
        + me.height);
  }

  /**
   * Returns an object that encodes the width and height of the element's margin
   * box.
   * 
   * @param elem
   * @param computedStyle
   * @return
   */
  public Options.Dimensions getMarginSize(Element elem, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    Options.Bounds me = getMarginExtends(elem, s);

    BoundingClientRect size = BoundingClientRect.get(elem);
    return new Options.Dimensions((size.getRight() - size.getLeft()) + me.width,
        (size.getBottom() - size.getTop()) + me.height);
  }

  public static Options.Bounds getContentBox(Element elem, ComputedStyle s) {
    throw new UnsupportedOperationException();
  }

  public static Options.Bounds getBorderBox(Element elem, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    Options.Bounds pe = getPadExtends(elem, s);
    Options.Bounds cb = getContentBox(elem, s);

    return new Options.Bounds(cb.left - pe.left, cb.top - pe.top, cb.width + pe.width, cb.height
        + pe.height);
  }

  /**
   * Sets the width/height/left/top in the current (native) box-model
   * dimensions.
   * 
   * @param elem
   * @param left left offset from parent
   * @param top top offset from parent
   * @param width width in current box model
   * @param height height in current box model
   * @param unit unit measure to use for other measures (defaults to "px").
   */
  public static void setBox(Element elem, Double left, Double top, Double width, Double height,
      Unit unit) {
    if (unit == null) {
      unit = Unit.PX;
    }
    Style s = elem.getStyle();
    if (left != null) {
      s.setLeft(left, unit);
    }
    if (top != null) {
      s.setTop(top, unit);
    }
    if (width != null && width >= 0.0) {
      s.setWidth(width, unit);
    }
    if (height != null && height >= 0.0) {
      s.setHeight(height, unit);
    }
  }

  /**
   * Sets the size of the node's contents, irrespective of margins, padding, or
   * borders.
   * 
   * @param elem
   * @param widthPx
   * @param heightPx
   * @param s
   */
  public static void setContentSize(Element elem, Double widthPx, Double heightPx, ComputedStyle s) {
    // if (useBorderBox(elem)) {
    // Options.Bounds pb = getPadBorderExtends(elem, s);
    // if (widthPx >= 0) {
    // widthPx += pb.width;
    // }
    // if (heightPx >= 0) {
    // heightPx += pb.height;
    // }
    // }
    setBox(elem, null, null, widthPx, heightPx, Style.Unit.PX);
  }

  /**
   * Sets the size of the node's margin box and placement (left/top),
   * irrespective of box model.
   * 
   * @param leftPx
   * @param topPx
   * @param widthPx
   * @param heightPx
   */
  public static void setMarginBox(Element elem, Integer leftPx, Integer topPx, Integer widthPx,
      Integer heightPx, ComputedStyle s) {
    if (s == null) {
      s = ComputedStyle.get(elem);
    }
    // Some elements have special padding, margin, and box-model settings.
    // To use box functions you may need to set padding, margin explicitly.
    // Controlling box-model is harder, in a pinch you might set boxModel.
    // bb = usesBorderBox(elem);
    // pb = bb ? nilExtends : getPadBorderExtends(elem, s);
    Options.Bounds pb = getPadBorderExtends(elem, s);
    // if(UserAgent.isWebkit()) {
    // On Safari (3.1.2), button nodes with no explicit size have a default
    // margin
    // setting an explicit size eliminates the margin.
    // We have to swizzle the width to get correct margin reading.
    // if(isButtonTag(elem)) {
    // }
    // }
    Options.Bounds mb = getMarginExtends(elem, s);
    if (widthPx != null && widthPx >= 0) {
      widthPx = Math.max(widthPx - pb.width - mb.width, 0);
    }
    if (heightPx != null && heightPx >= 0) {
      heightPx = Math.max(heightPx - pb.height - mb.height, 0);
    }
    setBox(elem, leftPx != null ? leftPx.doubleValue() : null, topPx != null ? topPx.doubleValue()
        : null, widthPx != null ? widthPx.doubleValue() : null,
        heightPx != null ? heightPx.doubleValue() : null, Style.Unit.PX);
  }

  /**
   * Sets the size of the node's margin box and placement (left/top),
   * irrespective of box model.
   * 
   * @param elem
   * @param box
   * @param s
   */
  public static void setMarginBox(Element elem, Options.Bounds box, ComputedStyle s) {
    Integer leftPx = box.left;
    Integer topPx = box.top;
    Integer widthPx = box.width;
    Integer heightPx = box.height;
    setMarginBox(elem, leftPx, topPx, widthPx, heightPx, s);
  }
}
