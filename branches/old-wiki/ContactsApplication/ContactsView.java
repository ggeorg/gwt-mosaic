package org.gwt.mosaic.contacts.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.observablecollections.client.ObservableCollections;
import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ActionEvent;
import org.gwt.mosaic.application.client.Application;
import org.gwt.mosaic.application.client.HasHistoryToken;
import org.gwt.mosaic.application.client.util.ApplicationFramework;
import org.gwt.mosaic.beansbinding.client.GWTMosaicBindings;
import org.gwt.mosaic.beansbinding.client.ListBoxBinding;
import org.gwt.mosaic.contacts.client.PageBusSubjects;
import org.gwt.mosaic.contacts.shared.ContactDetails;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.pagebus.client.PageBus;
import org.gwt.mosaic.pagebus.client.QueryCallback;
import org.gwt.mosaic.pagebus.client.SubscriberCallback;
import org.gwt.mosaic.pagebus.client.Subscription;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ListBox.ColumnComparator;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;

public class ContactsView extends LayoutComposite implements HasHistoryToken {
  private static ContactsView instance;

  public static ContactsView get() {
    if (instance == null) {
      instance = new ContactsView();
    }
    return instance;
  }

  // -----------------------------------------------------------------------

  private final ListBox<ContactDetails> contactsTable;

  private Subscription contactsStoreSubscription;

  private ContactsView() {
    super(new BoxLayout(Orientation.VERTICAL));

    final LayoutPanel layoutPanel = getLayoutPanel();

    // Create the menu
    final ToolBar toolBar = new ToolBar();
    toolBar.add(ApplicationFramework.newToolButton("doAddContact"));
    toolBar.add(ApplicationFramework.newToolButton("doEditContact"));
    toolBar.addSeparator();
    toolBar.add(ApplicationFramework.newToolButton("doDeleteContacts"));
    layoutPanel.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Create the contacts list
    contactsTable = new ListBox<ContactDetails>();
    layoutPanel.add(contactsTable, new BoxLayoutData(FillStyle.BOTH));

    contactsTable.setMultipleSelect(true);
    contactsTable.setColumnComparator(new ColumnComparator<ContactDetails>() {
      public int compare(ContactDetails t1, ContactDetails t2, int column) {
        switch (column) {
          case 0:
            try {
              return Integer.parseInt(t1.getId())
                  - Integer.parseInt(t2.getId());
            } catch (Exception ex) {
              return 0;
            }
          case 1:
            try {
              return t1.getDisplayName().compareTo(t2.getDisplayName());
            } catch (Exception ex) {
              return 0;
            }
        }
        return 0;
      }
    });

    contactsTable.addRowSelectionHandler(new RowSelectionHandler() {
      public void onRowSelection(RowSelectionEvent event) {
        Set<Integer> selectedIndices = contactsTable.getSelectedIndices();
        ArrayList<ContactDetails> selectedItems = new ArrayList<ContactDetails>();
        if (selectedIndices != null && selectedIndices.size() > 0) {
          for (Integer index : selectedIndices) {
            selectedItems.add(contactsTable.getItem(index));
          }
        }
        PageBus.store(PageBusSubjects.CONTACTS_STORE_SELECTION, selectedItems);
      }
    });
    contactsTable.addDoubleClickHandler(new DoubleClickHandler() {
      public void onDoubleClick(DoubleClickEvent event) {
        invokeAction("doEditContact");
      }
    });
  }

  public String getHistoryToken() {
    return "ContactsView";
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    bind();

    fetchContactDetails();
  }

  @Override
  protected void onUnload() {
    super.onUnload();

    unbind();
  }

  private void bind() {
    // Subject: CONTACTS_STORE
    contactsStoreSubscription = PageBus.subscribe(
        PageBusSubjects.CONTACTS_STORE, new SubscriberCallback() {
          public void onMessage(String subject, Object message,
              Object subscriberData) {
            getContactDetailsData();
          }
        });
  }

  private void unbind() {
    // Subject: CONTACTS_STORE
    if (contactsStoreSubscription != null) {
      PageBus.unsubscribe(contactsStoreSubscription);
      contactsStoreSubscription = null;
    }
  }

  private void fetchContactDetails() {
    PageBus.publish(PageBusSubjects.CONTACTS_MANAGEMENT_FETCH, null);
  }

  private BindingGroup bindingGroup = new BindingGroup();

  protected void getContactDetailsData() {

    if (bindingGroup != null) {
      bindingGroup.unbind();
    }
    bindingGroup = new BindingGroup();

    PageBus.query(PageBusSubjects.CONTACTS_STORE, new QueryCallback() {
      @SuppressWarnings("unchecked")
      public boolean onResult(String subject, Object value, Object data) {
        if (value != null && value instanceof List<?>) {
          List<ContactDetails> list = ObservableCollections.observableList((List<ContactDetails>) value);

          // create the binding from List to ListBox
          final ListBoxBinding<ContactDetails, List<ContactDetails>, ListBox<ContactDetails>> listBoxBinding = GWTMosaicBindings.createListBoxBinding(
              UpdateStrategy.READ, list, contactsTable);

          BeanProperty<ContactDetails, String> idP = BeanProperty.<ContactDetails, String> create("id");
          BeanProperty<ContactDetails, String> nameP = BeanProperty.<ContactDetails, String> create("displayName");

          // add columns bindings to the ListBoxBinding
          listBoxBinding.addColumnBinding(idP).setColumnName("Id");
          listBoxBinding.addColumnBinding(nameP).setColumnName("Name");

          bindingGroup.addBinding(listBoxBinding);
          bindingGroup.bind();

          contactsTable.setColumnWidth(0, DOM.toPixelSize("8em", true));
          contactsTable.layout();
        }
        return false;
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
