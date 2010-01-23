package org.gwt.mosaic.contacts.shared;

import java.io.Serializable;

import org.gwt.beansbinding.core.client.util.AbstractBean;

@SuppressWarnings("serial")
public class ContactDetails extends AbstractBean implements Serializable {
	private String id;
	private String displayName;

	public ContactDetails() {
		this("0", "");
	}

	public ContactDetails(String id, String displayName) {
		super();
		this.id = id;
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		String oldValue = this.id;
		this.id = id;
		firePropertyChange("id", oldValue, this.id);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		String oldValue = displayName;
		this.displayName = displayName;
		firePropertyChange("displayName", oldValue, this.displayName);
	}
}
