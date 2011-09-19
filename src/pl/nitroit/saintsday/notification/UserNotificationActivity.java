/**
 * Sep 14, 2011
 */
package pl.nitroit.saintsday.notification;

import java.util.List;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
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

		cancelNotification();
	}

	private void cancelNotification() {
		NotificationManager manager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(UserNotifier.NOTIFICATION_ID);
	}

}
