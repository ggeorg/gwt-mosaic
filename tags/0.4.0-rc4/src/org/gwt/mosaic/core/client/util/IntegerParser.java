package org.gwt.mosaic.core.client.util;

public class IntegerParser {

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

}
