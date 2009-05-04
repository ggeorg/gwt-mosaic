/*
 * Copyright (c) 2003-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.gwt.mosaic.validation.client.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Consists exclusively of static methods for validating input values by testing
 * and comparing single and multiple values.
 * <p>
 * The <a href="http://jakarta.apache.org/commons/lang.html">Jakarta Commons
 * Lang</a> library contains more classes and methods useful for validation. The
 * Utils string and character tests in this ValidationUtils class are compatible
 * with the Jakarta Commons Lang <code>StringUtils</code> methods.
 * 
 * @author Karsten Lentzsch
 * 
 * @see Calendar
 */
public final class ValidationUtils {

  private ValidationUtils() {
    // Override default constructor; prevents instantiation.
  }

  // Object Comparison ******************************************************

  /**
   * Checks and answers if the two objects are both {@code null} or equal.
   * 
   * <pre>
   * ValidationUtils.equals(null, null)  == true
   * ValidationUtils.equals(&quot;Hi&quot;, &quot;Hi&quot;)  == true
   * ValidationUtils.equals(&quot;Hi&quot;, null)  == false
   * ValidationUtils.equals(null, &quot;Hi&quot;)  == false
   * ValidationUtils.equals(&quot;Hi&quot;, &quot;Ho&quot;)  == false
   * </pre>
   * 
   * @param o1 the first object to compare
   * @param o2 the second object to compare
   * @return boolean {@code true} if and only if both objects are {@code null}
   *         or equal
   */
  public static boolean equals(Object o1, Object o2) {
    return (o1 != null && o2 != null && o1.equals(o2))
        || (o1 == null && o2 == null);
  }

  // String Validations ***************************************************

  /**
   * Checks and answers if the given string is whitespace, empty ("") or {@code
   * null}.
   * 
   * <pre>
   * ValidationUtils.isBlank(null)    == true
   * ValidationUtils.isBlank(&quot;&quot;)      == true
   * ValidationUtils.isBlank(&quot; &quot;)     == true
   * ValidationUtils.isBlank(&quot; abc&quot;)  == false
   * ValidationUtils.isBlank(&quot;abc &quot;)  == false
   * ValidationUtils.isBlank(&quot; abc &quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string is whitespace, empty or {@code null}
   * 
   * @see #isEmpty(String)
   */
  public static boolean isBlank(String str) {
    int length;
    if ((str == null) || ((length = str.length()) == 0)) {
      return true;
    }
    for (int i = length - 1; i >= 0; i--) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks and answers if the given string is not empty (""), not {@code null}
   * and not whitespace only.
   * 
   * <pre>
   * ValidationUtils.isNotBlank(null)    == false
   * ValidationUtils.isNotBlank(&quot;&quot;)      == false
   * ValidationUtils.isNotBlank(&quot; &quot;)     == false
   * ValidationUtils.isNotBlank(&quot; abc&quot;)  == true
   * ValidationUtils.isNotBlank(&quot;abc &quot;)  == true
   * ValidationUtils.isNotBlank(&quot; abc &quot;) == true
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string is not empty and not {@code null} and
   *         not whitespace only
   * 
   * @see #isEmpty(String)
   */
  public static boolean isNotBlank(String str) {
    int length;
    if ((str == null) || ((length = str.length()) == 0)) {
      return false;
    }
    for (int i = length - 1; i >= 0; i--) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks and answers if the given string is empty ("") or {@code null}.
   * 
   * <pre>
   * ValidationUtils.isEmpty(null)  == true
   * ValidationUtils.isEmpty(&quot;&quot;)    == true
   * ValidationUtils.isEmpty(&quot; &quot;)   == false
   * ValidationUtils.isEmpty(&quot;Hi &quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string is empty or {@code null}
   * 
   * @see #isBlank(String)
   */
  public static boolean isEmpty(String str) {
    return (str == null) || (str.length() == 0);
  }

  /**
   * Checks and answers if the given string is not empty ("") and not {@code
   * null}.
   * 
   * <pre>
   * ValidationUtils.isNotEmpty(null)  == false
   * ValidationUtils.isNotEmpty(&quot;&quot;)    == false
   * ValidationUtils.isNotEmpty(&quot; &quot;)   == true
   * ValidationUtils.isNotEmpty(&quot;Hi&quot;)  == true
   * ValidationUtils.isNotEmpty(&quot;Hi &quot;) == true
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string is not empty and not {@code null}
   * 
   * @see #isBlank(String)
   */
  public static boolean isNotEmpty(String str) {
    return (str != null) && (str.length() > 0);
  }

  /**
   * Checks and answers if the given string has at least the specified minimum
   * length. Strings that are {@code null} or contain only blanks have length 0.
   * 
   * <pre>
   * ValidationUtils.hasMinimumLength(null,  2) == false
   * ValidationUtils.hasMinimumLength(&quot;&quot;,    2) == false
   * ValidationUtils.hasMinimumLength(&quot; &quot;,   2) == false
   * ValidationUtils.hasMinimumLength(&quot;   &quot;, 2) == false
   * ValidationUtils.hasMinimumLength(&quot;Hi &quot;, 2) == true
   * ValidationUtils.hasMinimumLength(&quot;Ewa&quot;, 2) == true
   * </pre>
   * 
   * @param str the string to check
   * @param min the minimum length
   * @return {@code true} if the length is greater or equal to the minimum,
   *         {@code false} otherwise
   */
  public static boolean hasMinimumLength(String str, int min) {
    int length = str == null ? 0 : str.trim().length();
    return min <= length;
  }

  /**
   * Checks and answers if the given string is shorter than the specified
   * maximum length. Strings that are {@code null} or contain only blanks have
   * length 0.
   * 
   * <pre>
   * ValidationUtils.hasMaximumLength(null,  2) == true
   * ValidationUtils.hasMaximumLength(&quot;&quot;,    2) == true
   * ValidationUtils.hasMaximumLength(&quot; &quot;,   2) == true
   * ValidationUtils.hasMaximumLength(&quot;   &quot;, 2) == true
   * ValidationUtils.hasMaximumLength(&quot;Hi &quot;, 2) == true
   * ValidationUtils.hasMaximumLength(&quot;Ewa&quot;, 2) == false
   * </pre>
   * 
   * @param str the string to check
   * @param max the maximum length
   * @return {@code true} if the length is less than or equal to the minimum,
   *         {@code false} otherwise
   */
  public static boolean hasMaximumLength(String str, int max) {
    int length = str == null ? 0 : str.trim().length();
    return length <= max;
  }

  /**
   * Checks and answers if the length of the given string is in the bounds as
   * specified by the interval [min, max]. Strings that are {@code null} or
   * contain only blanks have length 0.
   * 
   * <pre>
   * ValidationUtils.hasBoundedLength(null,  1, 2) == false
   * ValidationUtils.hasBoundedLength(&quot;&quot;,    1, 2) == false
   * ValidationUtils.hasBoundedLength(&quot; &quot;,   1, 2) == false
   * ValidationUtils.hasBoundedLength(&quot;   &quot;, 1, 2) == false
   * ValidationUtils.hasBoundedLength(&quot;Hi &quot;, 1, 2) == true
   * ValidationUtils.hasBoundedLength(&quot;Ewa&quot;, 1, 2) == false
   * </pre>
   * 
   * @param str the string to check
   * @param min the minimum length
   * @param max the maximum length
   * @return {@code true} if the length is in the interval, {@code false}
   *         otherwise
   * @throws IllegalArgumentException if min > max
   */
  public static boolean hasBoundedLength(String str, int min, int max) {
    if (min > max) {
      throw new IllegalArgumentException(
          "The minimum length must be less than or equal to the maximum length.");
    }
    int length = str == null ? 0 : str.trim().length();
    return (min <= length) && (length <= max);
  }

  // Character Validations **************************************************

  /**
   * Checks and answers if the given string contains only unicode letters.
   * {@code null} returns false, an empty string ("") returns {@code true}.
   * 
   * <pre>
   * ValidationUtils.isAlpha(null)   == false
   * ValidationUtils.isAlpha(&quot;&quot;)     == true
   * ValidationUtils.isAlpha(&quot;   &quot;)  == false
   * ValidationUtils.isAlpha(&quot;abc&quot;)  == true
   * ValidationUtils.isAlpha(&quot;ab c&quot;) == false
   * ValidationUtils.isAlpha(&quot;ab2c&quot;) == false
   * ValidationUtils.isAlpha(&quot;ab-c&quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string contains only unicode letters, and is
   *         non-{@code null}
   * 
   * @since 1.2
   */
  public static boolean isAlpha(String str) {
    if (str == null) {
      return false;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
      if (!Character.isLetter(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks and answers if the given string contains only unicode letters and
   * space (' '). {@code null} returns false, an empty string ("") returns
   * {@code true}.
   * 
   * <pre>
   * ValidationUtils.isAlphaSpace(null)   == false
   * ValidationUtils.isAlphaSpace(&quot;&quot;)     == true
   * ValidationUtils.isAlphaSpace(&quot;   &quot;)  == true
   * ValidationUtils.isAlphaSpace(&quot;abc&quot;)  == true
   * ValidationUtils.isAlphaSpace(&quot;ab c&quot;) == true
   * ValidationUtils.isAlphaSpace(&quot;ab2c&quot;) == false
   * ValidationUtils.isAlphaSpace(&quot;ab-c&quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string contains only unicode letters and space,
   *         and is non-{@code null}
   */
  public static boolean isAlphaSpace(String str) {
    if (str == null) {
      return false;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
      char c = str.charAt(i);
      if (!Character.isLetter(c) && (c != ' ')) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks and answers if the given string contains only unicode letters or
   * digits. {@code null} returns false, an empty string ("") returns {@code
   * true}.
   * 
   * <pre>
   * ValidationUtils.isAlphanumeric(null)   == false
   * ValidationUtils.isAlphanumeric(&quot;&quot;)     == true
   * ValidationUtils.isAlphanumeric(&quot;   &quot;)  == false
   * ValidationUtils.isAlphanumeric(&quot;abc&quot;)  == true
   * ValidationUtils.isAlphanumeric(&quot;ab c&quot;) == false
   * ValidationUtils.isAlphanumeric(&quot;ab2c&quot;) == true
   * ValidationUtils.isAlphanumeric(&quot;ab-c&quot;) == false
   * ValidationUtils.isAlphanumeric(&quot;123&quot;)  == true
   * ValidationUtils.isAlphanumeric(&quot;12 3&quot;) == false
   * ValidationUtils.isAlphanumeric(&quot;12-3&quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string contains only unicode letters or digits,
   *         and is non-{@code null}
   */
  public static boolean isAlphanumeric(String str) {
    if (str == null) {
      return false;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
      if (!Character.isLetterOrDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks and answers if the given string contains only unicode letters or
   * digits or space (' '). {@code null} returns false, an empty string ("")
   * returns {@code true}.
   * 
   * <pre>
   * ValidationUtils.isAlphanumericSpace(null)   == false
   * ValidationUtils.isAlphanumericSpace(&quot;&quot;)     == true
   * ValidationUtils.isAlphanumericSpace(&quot;   &quot;)  == true
   * ValidationUtils.isAlphanumericSpace(&quot;abc&quot;)  == true
   * ValidationUtils.isAlphanumericSpace(&quot;ab c&quot;) == true
   * ValidationUtils.isAlphanumericSpace(&quot;ab2c&quot;) == true
   * ValidationUtils.isAlphanumericSpace(&quot;ab-c&quot;) == false
   * ValidationUtils.isAlphanumericSpace(&quot;123&quot;)  == true
   * ValidationUtils.isAlphanumericSpace(&quot;12 3&quot;) == true
   * ValidationUtils.isAlphanumericSpace(&quot;12-3&quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string contains only unicode letters, digits or
   *         space (' '), and is non-{@code null}
   */
  public static boolean isAlphanumericSpace(String str) {
    if (str == null) {
      return false;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
      char c = str.charAt(i);
      if (!Character.isLetterOrDigit(c) && (c != ' ')) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks and answers if the given string contains only unicode digits. A
   * decimal point is not a unicode digit and returns {@code false}. {@code
   * null} returns false, an empty string ("") returns {@code true}.
   * 
   * <pre>
   * ValidationUtils.isNumeric(null)   == false
   * ValidationUtils.isNumeric(&quot;&quot;)     == true
   * ValidationUtils.isNumeric(&quot;   &quot;)  == false
   * ValidationUtils.isNumeric(&quot;abc&quot;)  == false
   * ValidationUtils.isNumeric(&quot;ab c&quot;) == false
   * ValidationUtils.isNumeric(&quot;ab2c&quot;) == false
   * ValidationUtils.isNumeric(&quot;ab-c&quot;) == false
   * ValidationUtils.isNumeric(&quot;123&quot;)  == true
   * ValidationUtils.isNumeric(&quot;12 3&quot;) == false
   * ValidationUtils.isNumeric(&quot;12-3&quot;) == false
   * ValidationUtils.isNumeric(&quot;12.3&quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string contains only unicode digits, and is
   *         non-{@code null}
   */
  public static boolean isNumeric(String str) {
    if (str == null) {
      return false;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks and answers if the given string contains only unicode digits or
   * space (' '). A decimal point is not a unicode digit and returns {@code
   * false}. {@code null} returns false, an empty string ("") returns {@code
   * true}.
   * 
   * <pre>
   * ValidationUtils.isNumericSpace(null)   == false
   * ValidationUtils.isNumericSpace(&quot;&quot;)     == true
   * ValidationUtils.isNumericSpace(&quot;   &quot;)  == true
   * ValidationUtils.isNumericSpace(&quot;abc&quot;)  == false
   * ValidationUtils.isNumericSpace(&quot;ab c&quot;) == false
   * ValidationUtils.isNumericSpace(&quot;ab2c&quot;) == false
   * ValidationUtils.isNumericSpace(&quot;ab-c&quot;) == false
   * ValidationUtils.isNumericSpace(&quot;123&quot;)  == true
   * ValidationUtils.isNumericSpace(&quot;12 3&quot;) == true
   * ValidationUtils.isNumericSpace(&quot;12-3&quot;) == false
   * ValidationUtils.isNumericSpace(&quot;12.3&quot;) == false
   * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string contains only unicode digits or space,
   *         and is non-{@code null}
   */
  public static boolean isNumericSpace(String str) {
    if (str == null) {
      return false;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
      char c = str.charAt(i);
      if (!Character.isDigit(c) && (c != ' ')) {
        return false;
      }
    }
    return true;
  }

  // Date Validations *******************************************************

  /**
   * Determines and answers if the day of the given <code>Date</code> is in the
   * past.
   * 
   * @param date the date to check
   * @return {@code true} if in the past, {@code false} otherwise
   */
  public static boolean isPastDay(Date date) {
    Calendar in = new GregorianCalendar();
    in.setTime(date);
    Calendar today = getRelativeCalendar(0);
    return in.before(today);
  }

  /**
   * Determines and answers if the given <code>Date</code> is yesterday.
   * 
   * @param date the date to check
   * @return {@code true} if yesterday, {@code false} otherwise
   */
  public static boolean isYesterday(Date date) {
    Calendar in = new GregorianCalendar();
    in.setTime(date);
    Calendar yesterday = getRelativeCalendar(-1);
    Calendar today = getRelativeCalendar(0);
    return !in.before(yesterday) && in.before(today);
  }

  /**
   * Determines and answers if the given <code>Date</code> is today.
   * 
   * @param date the date to check
   * @return {@code true} if today, {@code false} otherwise
   */
  public static boolean isToday(Date date) {
    GregorianCalendar in = new GregorianCalendar();
    in.setTime(date);
    Calendar today = getRelativeCalendar(0);
    Calendar tomorrow = getRelativeCalendar(+1);
    return !in.before(today) && in.before(tomorrow);
  }

  /**
   * Determines and answers if the given <code>Date</code> is tomorrow.
   * 
   * @param date the date to check
   * @return {@code true} if tomorrow, {@code false} otherwise
   */
  public static boolean isTomorrow(Date date) {
    GregorianCalendar in = new GregorianCalendar();
    in.setTime(date);
    Calendar tomorrow = getRelativeCalendar(+1);
    Calendar dayAfter = getRelativeCalendar(+2);
    return !in.before(tomorrow) && in.before(dayAfter);
  }

  /**
   * Determines and answers if the day of the given <code>Date</code> is in the
   * future.
   * 
   * @param date the date to check
   * @return {@code true} if in the future, {@code false} otherwise
   */
  public static boolean isFutureDay(Date date) {
    Calendar in = new GregorianCalendar();
    in.setTime(date);
    Calendar tomorrow = getRelativeCalendar(+1);
    return !in.before(tomorrow);
  }

  /**
   * Computes the day that has the given offset in days to today and returns it
   * as an instance of <code>Date</code>.
   * 
   * @param offsetDays the offset in day relative to today
   * @return the <code>Date</code> that is the begin of the day with the
   *         specified offset
   */
  public static Date getRelativeDate(int offsetDays) {
    return getRelativeCalendar(offsetDays).getTime();
  }

  /**
   * Computes the day that has the given offset in days to today and returns it
   * as an instance of <code>Calendar</code>.
   * 
   * @param offsetDays the offset in day relative to today
   * @return a <code>Calendar</code> instance that is the begin of the day with
   *         the specified offset
   */
  public static Calendar getRelativeCalendar(int offsetDays) {
    Calendar today = new GregorianCalendar();
    return getRelativeCalendar(today, offsetDays);
  }

  /**
   * Computes the day that has the given offset in days from the specified
   * <em>from</em> date and returns it as an instance of <code>Calendar</code>.
   * 
   * @param from the base date as <code>Calendar</code> instance
   * @param offsetDays the offset in day relative to today
   * @return a <code>Calendar</code> instance that is the begin of the day with
   *         the specified offset from the given day
   */
  public static Calendar getRelativeCalendar(Calendar from, int offsetDays) {
    Calendar temp = new GregorianCalendar(from.get(Calendar.YEAR),
        from.get(Calendar.MONTH), from.get(Calendar.DATE), 0, 0, 0);
    temp.add(Calendar.DATE, offsetDays);
    return temp;
  }

}
