/**
 * Sep 14, 2011
 */
package pl.nitroit.saintsday.notification;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author kbl
 *
 */
public class UserNotificationActivity extends ListActivity {

	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<Contact> contactIds = (List<Contact>) getIntent().getSerializableExtra(UserNotifier.CONTACTS_IDS);
		setListAdapter(new ContactArrayAdapter(this, contactIds));
	}

}
