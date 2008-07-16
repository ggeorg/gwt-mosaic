package org.mosaic.core.client;

import org.mosaic.core.client.impl.UserAgentImpl;

import com.google.gwt.core.client.GWT;

public final class UserAgent {
  
  private static final UserAgentImpl impl = GWT.create(UserAgentImpl.class);
  
  public static final boolean isGecko() {
    return impl.isGecko();
  }
  
  public static final boolean isGecko18() {
    return impl.isGecko18();
  }
  
  public static final boolean isIE6() {
    return impl.isIE6();
  }
  
  public static final boolean isOpera() {
    return impl.isOpera();
  }
  
  public static final boolean isSafari() {
    return impl.isSafari();
  }

}
