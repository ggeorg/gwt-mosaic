package gwt.mosaic.core.client;

import com.google.gwt.core.client.GWT;

public class UserAgent {

  private static UserAgentImpl impl = GWT.create(UserAgentImpl.class);
  
  public static boolean isIE6() {
    return impl.isIE6();
  }
  
  public static boolean isIE8() {
    return impl.isIE8();
  }
  
  public static boolean isIE9() {
    return impl.isIE9();
  }
  
  public static boolean isMozilla() {
    return impl.isMozilla();
  }
  
  public static boolean isWebkit() {
    return impl.isWebkit();
  }
  
  public static boolean isOpera() {
    return impl.isOpera();
  }
}
