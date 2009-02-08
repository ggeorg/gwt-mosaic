package org.gwt.mosaic.showcase.client.content.other;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class CustomerServiceFactory {
  private static final String SERVLET_MAPPING = "customerService";
  
  private static CustomerServiceAsync SERVICE = null;
  
  public static CustomerServiceAsync get() {
    if (SERVICE == null) {
      // Create the client PROXY.
      SERVICE = (CustomerServiceAsync) GWT.create(CustomerService.class);
      // Specify the URL at which our service implementation is running.
      ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
      String moduleRelativeURL = GWT.getModuleBaseURL() + SERVLET_MAPPING;
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return SERVICE;
  }
}
