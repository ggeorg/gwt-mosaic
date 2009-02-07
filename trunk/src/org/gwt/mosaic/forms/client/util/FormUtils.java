/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopolos.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
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
package org.gwt.mosaic.forms.client.util;

/**
 * Consists only of static utility methods.
 * 
 * This class may be merged with the FormLayoutUtils extra - or not.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class FormUtils {

  // Instance *************************************************************

  private FormUtils() {
    // Suppresses default constructor, prevents instantiation.
  }

  // API ********************************************************************

  /**
   * Throws an exception if the specified text is blank using the given text
   * description.
   * 
   * @param text the text to check
   * @param description describes the text, used in the exception message
   * 
   * @throws NullPointerException if {@code text} is {@code null}
   * @throws IllegalArgumentException if {@code text} is empty, or blank
   */
  public static void assertNotBlank(String text, String description) {
    if (text == null)
      throw new NullPointerException("The " + description
          + " must not be null.");
    if (FormUtils.isBlank(text)) {
      throw new IllegalArgumentException("The " + description
          + " must not be empty, or whitespace. "
          + "See FormUtils.isBlank(String)");
    }
  }

  /**
   * Throws an NPE if the given object is {@code null} that uses the specified
   * text to describe the object.
   * 
   * @param object the text to check
   * @param description describes the object, used in the exception message
   * 
   * @throws NullPointerException if {@code object} is {@code null}
   */
  public static void assertNotNull(Object object, String description) {
    if (object == null)
      throw new NullPointerException("The " + description
          + " must not be null.");
  }

  /**
   * Checks and answers if the two objects are both {@code null} or equal.
   * 
   * <pre>
     * #equals(null, null)  == true
     * #equals("Hi", "Hi")  == true
     * #equals("Hi", null)  == false
     * #equals(null, "Hi")  == false
     * #equals("Hi", "Ho")  == false
     * </pre>
   * 
   * @param o1 the first object to compare
   * @param o2 the second object to compare
   * @return boolean {@code true} if and only if both objects are {@code null}
   *         or equal
   */
  public static boolean equals(Object o1, Object o2) {
    return ((o1 != null) && (o2 != null) && (o1.equals(o2)))
        || ((o1 == null) && (o2 == null));
  }

  /**
   * Checks and answers if the given string is whitespace, empty (""), or
   * {@code null}.
   * 
   * <pre>
   * FormUtils.isBlank(null)    == true
   * FormUtils.isBlank("")      == true
   * FormUtils.isBlank(" ")     == true
   * FormUtils.isBlank(" abc")  == false
   * FormUtils.isBlank("abc ")  == false
   * FormUtils.isBlank(" abc ") == false
   * </pre>
   * 
   * @param str the string to check, may be{@code null}
   * @return {@code true} if the string is whitespace, empty, or {@code null}
   */
  public static boolean isBlank(String str) {
    int length;
    if ((str == null) || ((length = str.length()) == 0)) {
      return true;
    }
    for (int i = length - 1; i >= 0; i--) {
      if (!isWhitespace(str.charAt(i))) {
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
     * FormUtils.isNotBlank(null)    == false
     * FormUtils.isNotBlank("")      == false
     * FormUtils.isNotBlank(" ")     == false
     * FormUtils.isNotBlank(" abc")  == true
     * FormUtils.isNotBlank("abc ")  == true
     * FormUtils.isNotBlank(" abc ") == true
     * </pre>
   * 
   * @param str the string to check, may be {@code null}
   * @return {@code true} if the string is not empty and not {@code null} and
   *         not whitespace only
   */
  public static boolean isNotBlank(String str) {
    int length;
    if ((str == null) || ((length = str.length()) == 0))
      return false;
    for (int i = length - 1; i >= 0; i--) {
      if (!isWhitespace(str.charAt(i)))
        return true;
    }
    return false;
  }

  /**
   * Answers whether the character is a whitespace character in Java.
   * 
   * @param c the character
   * @return true if the supplied <code>c</code> is a whitespace character in
   *         Java, otherwise false.
   */
  public static boolean isWhitespace(char c) {
    // Optimized case for ASCII
    if ((c >= 0x1c && c <= 0x20) || (c >= 0x9 && c <= 0xd)) {
      return true;
    }
    if (c == 0x1680) {
      return true;
    }
    if (c < 0x2000 || c == 0x2007) {
      return false;
    }
    return c <= 0x200b || c == 0x2028 || c == 0x2029 || c == 0x3000;
  }

}
