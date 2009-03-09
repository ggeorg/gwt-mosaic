package org.gwt.mosaic.showcase.client.content.other;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CustomerServiceAsync {
  void getCustomers(AsyncCallback<List<Customer>> asyncCallback);
}
