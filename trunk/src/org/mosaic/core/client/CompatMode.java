package org.mosaic.core.client;

import org.mosaic.core.client.impl.CSS1CompatImpl;

import com.google.gwt.core.client.GWT;

public class CompatMode {
  
  private static final CSS1CompatImpl impl = GWT.create(CSS1CompatImpl.class);
  
  public static boolean isStandardsMode() {
    return impl.isStandardsMode();
  }

}
