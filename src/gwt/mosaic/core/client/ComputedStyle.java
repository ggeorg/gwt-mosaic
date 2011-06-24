package gwt.mosaic.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * Provides programmatic access to properties of the computed style object.
 */
public class ComputedStyle extends JavaScriptObject {

  private static final ComputedStyleImpl impl = GWT.create(ComputedStyleImpl.class);

  private static final String STYLE_FONT_SIZE = "fontSize";
  private static final String STYLE_COLOR = "color";
  private static final String STYLE_BACKGROUND_COLOR = "backgroundColor";

  private static final String STYLE_MARGIN_TOP = "marginTop";
  private static final String STYLE_MARGIN_RIGHT = "marginRight";
  private static final String STYLE_MARGIN_LEFT = "marginLeft";
  private static final String STYLE_MARGIN_BOTTOM = "marginBottom";

  private static final String STYLE_BORDER_TOP_WIDTH = "borderTopWidth";
  private static final String STYLE_BORDER_RIGHT_WIDTH = "borderRightWidth";
  private static final String STYLE_BORDER_LEFT_WIDTH = "borderBottomWidth";
  private static final String STYLE_BORDER_BOTTOM_WIDTH = "borderLeftWidth";

  private static final String STYLE_PADDING_TOP = "paddingTop";
  private static final String STYLE_PADDING_RIGHT = "paddingRight";
  private static final String STYLE_PADDING_LEFT = "paddingLeft";
  private static final String STYLE_PADDING_BOTTOM = "paddingBottom";

  private static final String STYLE_WIDTH = "width";
  private static final String STYLE_HEIGHT = "height";

  private static final String STYLE_LINE_HEIGHT = "lineHeight";

  /**
   * Gets this element's computed style object which can be used to gather
   * information about the current state of the rendered node.
   * <p>
   * Note that this method is expensive. Wherever possible, reuse the returned
   * object.
   * 
   * @param elem the element
   * @return the computed style
   */
  public static ComputedStyle get(Element elem) {
    return impl.getComputedStyle(elem);
  }

  protected ComputedStyle() {
  }

  /**
   * Gets the value of a named property.
   */
  public final String getProperty(String name) {
    assertCamelCase(name);
    return impl.getProperty(this, name);
  }

  /**
   * Get the font-size computed style property.
   */
  public final int getFontSize() {
    return parseInt(getProperty(STYLE_FONT_SIZE), 10, 0);
  }

  /**
   * Get the color computed style property.
   */
  public final String getColor() {
    return getProperty(STYLE_COLOR);
  }

  /**
   * Get the background-color computed style property.
   */
  public final String getBackgroundColor() {
    return getProperty(STYLE_BACKGROUND_COLOR);
  }

  /**
   * Get the margin-top computed style property.
   */
  public final int getMarginTop() {
    return parseInt(getProperty(STYLE_MARGIN_TOP), 10, 0);
  }

  /**
   * Get the margin-left computed style property.
   */
  public final int getMarginLeft() {
    return parseInt(getProperty(STYLE_MARGIN_LEFT), 10, 0);
  }

  public final int getMarginRight() {
    return parseInt(getProperty(STYLE_MARGIN_RIGHT), 10, 0);
  }

  public final int getMarginBottom() {
    return parseInt(getProperty(STYLE_MARGIN_BOTTOM), 10, 0);
  }

  public final int getBorderTopWidth() {
    return parseInt(getProperty(STYLE_BORDER_TOP_WIDTH), 10, 0);
  }

  public final int getBorderLeftWidth() {
    return parseInt(getProperty(STYLE_BORDER_LEFT_WIDTH), 10, 0);
  }

  public final int getBorderRightWidth() {
    return parseInt(getProperty(STYLE_BORDER_RIGHT_WIDTH), 10, 0);
  }

  public final int getBorderBottomWidth() {
    return parseInt(getProperty(STYLE_BORDER_BOTTOM_WIDTH), 10, 0);
  }

  public final int getPaddingTop() {
    return parseInt(getProperty(STYLE_PADDING_TOP), 10, 0);
  }

  public final int getPaddingLeft() {
    return parseInt(getProperty(STYLE_PADDING_LEFT), 10, 0);
  }

  public final int getPaddingRight() {
    return parseInt(getProperty(STYLE_PADDING_RIGHT), 10, 0);
  }

  public final int getPaddingBottom() {
    return parseInt(getProperty(STYLE_PADDING_BOTTOM), 10, 0);
  }

  public final int getWidth() {
    return parseInt(getProperty(STYLE_WIDTH), 10, 0);
  }

  public final int getHeight() {
    return parseInt(getProperty(STYLE_HEIGHT), 10, 0);
  }

  public final int getLineHeight() {
    return parseInt(getProperty(STYLE_LINE_HEIGHT), 10, 0);
  }

  /**
   * Parses a string and returns an integer.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number,
   * <code>parseInt()</code> returns <code>null</code>.
   * 
   * @param str the string to be parsed
   * @return the parsed value
   */
  protected static Integer parseInt(String str) {
    return parseInt(str, 10);
  }

  /**
   * Parses a string and returns an integer.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number,
   * <code>parseInt()</code> returns <code>defaultValue</code>.
   * 
   * @param str the string to be parsed
   * @param radix a number (from 2 to 36) that represents the numeric system to
   *          be used
   * @param defaultValue the value to return if the parsed value was
   *          <code>null</code>
   * @return the parsed value
   */
  public static int parseInt(String str, int radix, int defaultValue) {
    final Integer result = parseInt(str, radix);
    return result == null ? defaultValue : result;
  }

  /**
   * Parses a string and returns an integer.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number, {@code
   * parseInt()} returns {@code null}.
   * 
   * @param str the string to be parsed
   * @param radix a number (from 2 to 36) that represents the numeric system to
   *          be used
   * @return the parsed value
   */
  public native static Integer parseInt(String str, int radix)
  /*-{
    var number = parseInt(str, radix);
    if (isNaN(number))
      return null;
    else
      return @java.lang.Integer::valueOf(I)(number);
  }-*/;

  /**
   * Assert that the specified property does not contain a hyphen.
   * 
   * @param name the property name
   */
  private void assertCamelCase(String name) {
    assert !name.contains("-") : "The style name '" + name + "' should be in camelCase format";
  }

}