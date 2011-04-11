package org.gwt.mosaic.contacts.client;

import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.ui.client.adapters.HasValueAdapterProvider;
import org.gwt.mosaic.application.client.Application;
import org.gwt.mosaic.application.client.ApplicationClientBundle;
import org.gwt.mosaic.application.client.ApplicationConstants;
import org.gwt.mosaic.application.client.ApplicationResources;
import org.gwt.mosaic.application.client.CmdAction;
import org.gwt.mosaic.application.client.SingleFrameApplication;
import org.gwt.mosaic.beansbinding.client.adapters.ListBoxAdapterProvider;
import org.gwt.mosaic.contacts.client.controller.ContactsController;
import org.gwt.mosaic.contacts.client.ui.ContactsView;
import org.gwt.mosaic.contacts.client.ui.EditContact;
import org.gwt.mosaic.contacts.shared.Contact;
import org.gwt.mosaic.contacts.shared.ContactDetails;
import org.gwt.mosaic.pagebus.client.PageBus;
import org.gwt.mosaic.pagebus.client.QueryCallback;
import org.gwt.mosaic.pagebus.client.SubscriberCallback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Contacts extends SingleFrameApplication implements EntryPoint {
  private static final ContactsServiceAsync rpcService = GWT.create(ContactsService.class);

  private boolean itemsSelected = false, singleSelection = false;

  private ContactsController contactsCtrl;

  public void onModuleLoad() {
    Application.launch(this, new ApplicationResources() {
      final ContactsConstants constants = GWT.create(ContactsConstants.class);
      final ContactsClientBundle clientBundle = GWT.create(ContactsClientBundle.class);

      public ApplicationConstants getConstants() {
        return constants;
      }

      public ApplicationClientBundle getClientBundle() {
        return clientBundle;
      }
    });
  }

  @Override
  protected void initialize() {

    // Register all Adapters used in code.
    BeanAdapterFactory.addProvider(new HasValueAdapterProvider<String>());
    BeanAdapterFactory.addProvider(new ListBoxAdapterProvider());

    // Initialize all controllers
    contactsCtrl = new ContactsController(rpcService);

    // Subject: CONTACTS_SELECTION
    PageBus.subscribe(PageBusSubjects.CONTACTS_STORE_SELECTION,
        new SubscriberCallback() {
          @SuppressWarnings("unchecked")
          public void onMessage(String subject, Object message,
              Object subscriberData) {
            onSelectionChanged((List<ContactDetails>) message);
          }
        });

  }

  @Override
  protected void startup() {
    show(ContactsView.get().getHistoryToken());
  }

  // Getters & Setters -----------------------------------------------------

  /**
   * @return the itemsSelected
   */
  public boolean isItemsSelected() {
    return itemsSelected;
  }

  /**
   * @param itemsSelected the itemsSelected to set
   */
  public void setItemsSelected(boolean itemsSelected) {
    Boolean oldValue = this.itemsSelected;
    this.itemsSelected = itemsSelected;
    firePropertyChange("itemsSelected", oldValue, this.itemsSelected);
  }

  /**
   * @return the singleSelection
   */
  public boolean isSingleSelection() {
    return singleSelection;
  }

  /**
   * @param singleSelection the singleSelection to set
   */
  public void setSingleSelection(boolean singleSelection) {
    Boolean oldValue = this.singleSelection;
    this.singleSelection = singleSelection;
    firePropertyChange("singleSelection", oldValue, this.singleSelection);
  }

  /**
   * @return the contactsCtrl
   */
  public ContactsController getContactsCtrl() {
    return contactsCtrl;
  }

  // Command Actions -----------------------------------------------------

  @CmdAction(name = "ContactsView")
  public void showContactsView() {
    show(ContactsView.get(), true);
  }

  @CmdAction(name = "EditContact")
  public void showEditContact() {
    show(EditContact.get(), true);
  }

  @CmdAction(description = "doAddContactShortDescription", image = "form_add_action")
  public void doAddContact() {
    getContactsCtrl().doAddContact();
  }

  @CmdAction(description = "doEditContactShortDescription", image = "form_edit_action", enabledProperty = "singleSelection")
  public void doEditContact() {
    PageBus.query(PageBusSubjects.CONTACTS_STORE_SELECTION,
        new QueryCallback() {
          @SuppressWarnings("unchecked")
          public boolean onResult(String subject, Object value, Object data) {
            if (value != null && value instanceof List<?>) {
              getContactsCtrl().editSelectedContact(
                  ((List<ContactDetails>) value).get(0));
            }
            return false;
          }
        });
  }

  @CmdAction(description = "doDeleteContactsShortDescription", image = "form_delete_action", enabledProperty = "itemsSelected")
  public void doDeleteContacts() {
    PageBus.query(PageBusSubjects.CONTACTS_STORE_SELECTION,
        new QueryCallback() {
          @SuppressWarnings("unchecked")
          public boolean onResult(String subject, Object value, Object data) {
            if (value != null && value instanceof List<?>) {
              getContactsCtrl().deleteSelectedContacts(
                  (List<ContactDetails>) value);
            }
            return false;
          }
        });
  }

  @CmdAction
  public void doSaveContact() {
    PageBus.query(PageBusSubjects.CONTACTS_STORE_CURRENT, new QueryCallback() {
      public boolean onResult(String subject, Object value, Object data) {
        if (value != null && value instanceof Contact) {
          getContactsCtrl().saveContact((Contact) value);
        }
        return false;
      }
    });
  }

  @CmdAction
  public void doCancelEditContact() {
    show(ContactsView.get(), true);
  }

  // -----------------------------------------------------------------------

  protected void onSelectionChanged(List<ContactDetails> selection) {
    setItemsSelected(selection != null && selection.size() > 0);
    setSingleSelection(selection != null && selection.size() == 1);
  }
}
