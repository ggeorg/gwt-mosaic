package org.gwt.mosaic.core.client.util.regex;

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
