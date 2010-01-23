package org.gwt.mosaic.contacts.shared;

import java.io.Serializable;

import org.gwt.beansbinding.core.client.util.AbstractBean;

@SuppressWarnings("serial")
public class Contact extends AbstractBean implements Serializable {
	private String id;
	private String firstName;
	private String lastName;
	private String emailAddress;

	public Contact() {
		// Nothing to do here!
	}

	public Contact(String id, String firstName, String lastName,
			String emailAddress) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		String oldValue = this.id;
		this.id = id;
		firePropertyChange("id", oldValue, this.id);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		String oldValue = this.firstName;
		this.firstName = firstName;
		firePropertyChange("firstName", oldValue, this.firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		String oldValue = this.lastName;
		this.lastName = lastName;
		firePropertyChange("lastName", oldValue, this.lastName);
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		String oldValue = this.emailAddress;
		this.emailAddress = emailAddress;
		firePropertyChange("emailAddress", oldValue, this.emailAddress);
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public ContactDetails getLightWeightContact() {
		return new ContactDetails(id, getFullName());
	}
}
