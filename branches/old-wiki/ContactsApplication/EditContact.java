package org.gwt.mosaic.contacts.client.ui;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.mosaic.application.client.HasHistoryToken;
import org.gwt.mosaic.application.client.util.ApplicationFramework;
import org.gwt.mosaic.contacts.client.PageBusSubjects;
import org.gwt.mosaic.contacts.shared.Contact;
import org.gwt.mosaic.forms.client.builder.DefaultFormBuilder;
import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.pagebus.client.PageBus;
import org.gwt.mosaic.pagebus.client.QueryCallback;
import org.gwt.mosaic.ui.client.LayoutComposite;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;

public class EditContact extends LayoutComposite implements HasHistoryToken {
  private static EditContact instance;

  public static EditContact get() {
    if (instance == null) {
      instance = new EditContact();
    }
    return instance;
  }

  // -----------------------------------------------------------------------

  private final BindingGroup bindingGroup = new BindingGroup();
  
  private final TextBox firstName = new TextBox();
  private final TextBox lastName = new TextBox();
  private final TextBox emailAddress = new TextBox();

  private EditContact() {
    super();

    DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:pref, 3dlu, fill:pref:grow"));

    builder.appendSeparator("Contact Details");
    builder.append("First Name:", firstName);
    builder.append("Last Name:", lastName);
    builder.append("Email Address:", emailAddress);
    builder.append(ButtonBarFactory.buildRightAlignedBar(
        ApplicationFramework.newButton("doSaveContact"),
        ApplicationFramework.newButton("doCancelEditContact")), 3);
    
    getLayoutPanel().add(builder.getLayoutPanel());
    getLayoutPanel().setPadding(5);
  }
  
  public String getHistoryToken() {
    return "EditContact";
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    bind();

    firstName.setFocus(true);
  }

  @Override
  protected void onUnload() {
    super.onUnload();

    unbind();
  }

  private void bind() {
    // Subject: CONTACTS_STORE_CURRENT
    PageBus.query(PageBusSubjects.CONTACTS_STORE_CURRENT, new QueryCallback() {
      public boolean onResult(String subject, Object value, Object data) {
        bindContactToUI((Contact) value);        
        return false;
      }
    });
  }

  private void unbind() {
    bindingGroup.unbind();
  }
  
  private void bindContactToUI(Contact contact) {
    BeanProperty<HasValue<String>, String> valueP = BeanProperty.create("value");

    Binding<Contact, String, HasValue<String>, String> firstNameB = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, contact,
        BeanProperty.<Contact, String> create("firstName"), firstName,
        valueP);

    Binding<Contact, String, HasValue<String>, String> lastNameB = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, contact,
        BeanProperty.<Contact, String> create("lastName"), lastName, valueP);

    Binding<Contact, String, HasValue<String>, String> emailAddressB = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, contact,
        BeanProperty.<Contact, String> create("emailAddress"),
        emailAddress, valueP);

    bindingGroup.addBinding(firstNameB);
    bindingGroup.addBinding(lastNameB);
    bindingGroup.addBinding(emailAddressB);
    
    bindingGroup.bind();
  }
}
