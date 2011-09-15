/**
 * Sep 15, 2011
 */
package pl.nitroit.saintsday.notification;

import java.io.Serializable;

/**
 * @author kbl
 *
 */
public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String phoneNumber;

	public Contact(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = "+48606724038";
	}

	protected String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

}
