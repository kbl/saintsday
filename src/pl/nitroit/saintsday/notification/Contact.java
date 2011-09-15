/**
 * Sep 15, 2011
 */
package pl.nitroit.saintsday.notification;

/**
 * @author kbl
 *
 */
public class Contact {

	private long id;
	private String name;

	public Contact(long id, String name) {
		this.id = id;
		this.name = name;
	}

	protected long getId() {
		return id;
	}

	protected String getName() {
		return name;
	}

}
