package org.mosaic.core.client.impl;

public class BackCompatImpl extends CSS1CompatImpl {

  @Override
  public boolean isStandardsMode() {
    return false;
  }

}
