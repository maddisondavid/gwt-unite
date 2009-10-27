package org.gwtunite.testing.client.tests.rpc;

import java.io.Serializable;

public class Person implements Serializable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((salutation == null) ? 0 : salutation.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (salutation == null) {
			if (other.salutation != null)
				return false;
		} else if (!salutation.equals(other.salutation))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	private static final long serialVersionUID = -6102976703621615960L;
	private String salutation;
	private String surname;
	
	public Person() {
		// Required by GWT RPC
	}
	
	public Person(String salutation, String surname) {
		this.setSalutation(salutation);
		this.setSurname(surname);
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}
}
