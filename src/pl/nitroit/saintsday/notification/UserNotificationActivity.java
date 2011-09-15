/**
 * Sep 14, 2011
 */
package pl.nitroit.saintsday.notification;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author kbl
 *
 */
public class UserNotificationActivity extends ListActivity {

	private long[] contactIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contactIds = getIntent().getLongArrayExtra(UserNotifier.CONTACTS_IDS);
		setListAdapter(new ContactArrayAdapter(this, prepareContacts(contactIds)));
	}

	private List<Contact> prepareContacts(long[] contactIds) {
		List<Contact> returned = new ArrayList<Contact>(contactIds.length);

		returned.add(new Contact(1, "Marcin Pietraszek"));
		returned.add(new Contact(1, "Zenon Laskowik"));
		returned.add(new Contact(1, "Marian Opania"));

		return returned;
	}

}
