package org.gwt.mosaic.contacts.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ActionEvent;
import org.gwt.mosaic.application.client.Application;
import org.gwt.mosaic.contacts.client.ContactsServiceAsync;
import org.gwt.mosaic.contacts.client.PageBusSubjects;
import org.gwt.mosaic.contacts.shared.Contact;
import org.gwt.mosaic.contacts.shared.ContactDetails;
import org.gwt.mosaic.pagebus.client.PageBus;
import org.gwt.mosaic.pagebus.client.SubscriberCallback;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContactsController {
  private final ContactsServiceAsync rpcService;

  public ContactsController(ContactsServiceAsync rpcService) {
    this.rpcService = rpcService;

    // Subject: CONTACTS_MANAGEMENT_FETCH (optional)
    PageBus.subscribe(PageBusSubjects.CONTACTS_MANAGEMENT_FETCH,
        new SubscriberCallback() {
          public void onMessage(String subject, Object message,
              Object subscriberData) {
            fetchContactDetails();
          }
        });
  }

  protected void fetchContactDetails() {
    rpcService.getContactDetails(new AsyncCallback<ArrayList<ContactDetails>>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error fetching contact details");
      }

      public void onSuccess(ArrayList<ContactDetails> result) {
        PageBus.store(PageBusSubjects.CONTACTS_STORE, result);
      }
    });
  }

  public void doAddContact() {
    PageBus.store(PageBusSubjects.CONTACTS_STORE_CURRENT, new Contact());
    invokeAction("EditContact");
  }

  public void editSelectedContact(ContactDetails contactDetails) {
    rpcService.getContact(contactDetails.getId(), new AsyncCallback<Contact>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error retrieving contact");
      }

      public void onSuccess(Contact result) {
        PageBus.store(PageBusSubjects.CONTACTS_STORE_CURRENT, result);
        invokeAction("EditContact");
      }
    });
  }

  public void deleteSelectedContacts(List<ContactDetails> selection) {
    ArrayList<String> ids = new ArrayList<String>();

    for (ContactDetails contactDetail : selection) {
      ids.add(contactDetail.getId());
    }

    rpcService.deleteContacts(ids,
        new AsyncCallback<ArrayList<ContactDetails>>() {
          public void onFailure(Throwable caught) {
            Window.alert("Error deleting selected contacts");
          }

          public void onSuccess(ArrayList<ContactDetails> result) {
            PageBus.store(PageBusSubjects.CONTACTS_STORE, result);
          }
        });
  }

  public void saveContact(Contact contact) {
    rpcService.updateContact(contact, new AsyncCallback<Contact>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error updating contact");
      }

      public void onSuccess(Contact result) {
        invokeAction("ContactsView");
      }
    });
  }

  private void invokeAction(String actionName) {
    final Action action = Application.getInstance().getContext().getActionMap().get(
        actionName);
    if (action != null) {
      action.actionPerformed(new ActionEvent(action, this));
    }
  }

}
