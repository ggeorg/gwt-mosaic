package gwt.mosaic.client.examples;

import gwt.mosaic.shared.beans.Bean;

@Bean
public class Contact {
	private String id;
	private String name;
	private Address address;
	private String phoneNumber;
	private String emailAddress;
	private IMAccount imAccount;

	public Contact() {
		this(null, null, null, null, null, null);
	}

	public Contact(String id, String name, Address address, String phoneNumber,
			String emailAddress, IMAccount imAccount) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.imAccount = imAccount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public IMAccount getIMAccount() {
		return imAccount;
	}

	public IMAccount getImAccount() {
		return getIMAccount();
	}

}