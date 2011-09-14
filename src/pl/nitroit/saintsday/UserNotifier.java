/**
 * Sep 14, 2011
 */
package pl.nitroit.saintsday;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pl.nitroit.saintsday.db.SaintsDayDao;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.text.format.DateUtils;

/**
 * @author kbl
 *
 */
public class UserNotifier {

	public static final int NOTIFICATION_ID = 123321;

	private static final String WHERE_EXISTS_PHONE_NUMBER = ContactsContract.Data.HAS_PHONE_NUMBER + " = 1";

	private Context context;
	private String[] todaySaints;
	private SaintsDayDao dao;

	public UserNotifier(Context context) {
		this.context = context;
		dao = new SaintsDayDao(context);
	}

	public void notifiyAboutTodaySaints() {
		if(notificationShouldBeSend()) {
			Set<Integer> contactIds = obtainContactIdsToNotify();
			if(!contactIds.isEmpty()) {
				NotificationManager manager =
						(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				manager.notify(NOTIFICATION_ID, createNotification(contactIds));
				dao.open().setLastNotifiedAndRemoveOldTimestamp(new Date());
			}
		}
		dao.close();
	}

	private boolean notificationShouldBeSend() {
		long notificationTimestamp = dao.getLastNotifiedTimestamp();
		return notificationTimestamp == SaintsDayDao.NO_NOTIFICATION ||
				!DateUtils.isToday(notificationTimestamp);
	}

	private Set<Integer> obtainContactIdsToNotify() {
		Set<Integer> contacts = new HashSet<Integer>();
		ContentResolver contentResolver = context.getContentResolver();

		for(String saintName : todaySaints) {
			Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, saintName);
			Cursor contactsCursor = contentResolver.query(
					contactUri,
					new String[] {ContactsContract.Contacts.Data._ID},
					WHERE_EXISTS_PHONE_NUMBER,
					null,
					null);
			if(contactsCursor.moveToFirst()) {
				do {
					contacts.add(contactsCursor.getInt(0));
				} while(contactsCursor.moveToNext());
			}
		}
		return contacts;
	}

	private Notification createNotification(Set<Integer> contactIds) {
		Notification notification = new Notification(
				R.drawable.icon,
				"works?",
				SystemClock.currentThreadTimeMillis());
		Intent intent = new Intent(context, this.getClass());
		intent.getExtras().putLongArray("abc", new long[0]);
		PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, 0);
		notification.setLatestEventInfo(context, "bu", "buz", contentIntent);

		return notification ;
	}

}
