package org.gwt.mosaic.showcase.client.content.other;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface CustomerService extends RemoteService {
  List<Customer> getCustomers();
}
