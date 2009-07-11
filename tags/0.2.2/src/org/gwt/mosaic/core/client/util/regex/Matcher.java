/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.core.client.util.regex;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class Matcher implements MatchResult {

  private Pattern pat = null;

  private String expression = null;

  Matcher(Pattern pat, CharSequence cs) {
    this.pat = pat;
    this.expression = cs.toString();
  }

  public boolean find() {
    return pat.matches(expression);
  }

  public int start() {
    String[] s = pat.match(expression);
    int start = -1;
    for (String ss : s)
      start = expression.indexOf(ss);
    return start;
  }

  public int end() {
    String[] s = pat.match(expression);
    int end = -1;
    for (String ss : s)
      end = expression.indexOf(ss) + ss.length();
    return end;
  }

}
