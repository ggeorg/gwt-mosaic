package org.gwt.mosaic.core.client.util;

public class FloatParser {

  /**
   * Parses a string and returns a float.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number, {@code
   * parseFloat} returns {@code defaultValue}.
   * 
   * @param str the string to be parsed
   * @param defaultValue the value to return if the parsed value was
   *          <code>null</code>
   * @return the parsed value
   */
  public static Float parseFloat(String str, float defaultValue) {
    final Float result = parseFloat(str);
    return result == null ? defaultValue : result;
  }

  /**
   * Parses a string and returns a float.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number, {@code
   * parseFloat} returns {@code null}.
   * 
   * @param str the string to be parsed
   * @return the parsed value
   */
  public native static Float parseFloat(String str)
  /*-{
    var number = parseFloat(str);
    if (isNaN(number))
      return null;
    else
      return @java.lang.Float::valueOf(F)(number);
  }-*/;
}
