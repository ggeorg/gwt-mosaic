package org.gwt.mosaic.application.client;

import org.gwt.mosaic.actions.client.ActionConstants;

public interface ApplicationConstants extends ActionConstants {

  @DefaultStringValue("[ApplicationId]")
  String applicationId();
  
  @DefaultStringValue("[Application Title]")
  String applicationTitle();
  
  @DefaultStringValue("v0.0.0")
  String applicationVersion();
  
  @DefaultStringValue("[Application Vendor]")
  String applicationVendor();
  
  @DefaultStringValue("[ApplicationVenforId]")
  String applicationVendorId();
  
  @DefaultStringValue("")
  String applicationHomepage();
  
  @DefaultStringValue("[Application Description]")
  String applicationDescription();

}
