/**
 * Sep 14, 2011
 */
package pl.nitroit.saintsday.notification;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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
		setListAdapter(new ArrayAdapter<Long>(this, android.R.layout.simple_list_item_1, prepareContacts(contactIds)));
	}

	private List<Long> prepareContacts(long[] contactIds) {
		List<Long> returned = new ArrayList<Long>(contactIds.length);
		for(long x : contactIds) {
			returned.add(x);
		}
		return returned;
	}

}
