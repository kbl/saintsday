/**
 * Sep 14, 2011
 */
package pl.nitroit.saintsday.notification;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pl.nitroit.saintsday.R;
import pl.nitroit.saintsday.db.SaintsDayDao;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

	public static final String CONTACTS_IDS = "UserNotifier.contacts_ids";
	public static final int NOTIFICATION_ID = 123321;

	private static final String WHERE_EXISTS_PHONE_NUMBER = ContactsContract.Data.HAS_PHONE_NUMBER + " = 1";

	private Context context;
	private String[] todaySaints;
	private SaintsDayDao dao;

	public UserNotifier(Context context, String[] todaySaints) {
		this.context = context;
		this.todaySaints = todaySaints;
		dao = new SaintsDayDao(context);
	}

	public void notifiyAboutTodaySaints() {
		dao.open();
		if(notificationShouldBeSend()) {
			long[] contactIds = obtainContactIdsToNotify();
			if(contactIds.length != 0) {
				NotificationManager manager =
						(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				manager.notify(NOTIFICATION_ID, createNotification(contactIds));
				dao.setLastNotifiedAndRemoveOldTimestamp(new Date());
			}
		}
		dao.close();
	}

	private boolean notificationShouldBeSend() {
		long notificationTimestamp = dao.getLastNotifiedTimestamp();
		return notificationTimestamp == SaintsDayDao.NO_NOTIFICATION ||
				!DateUtils.isToday(notificationTimestamp);
	}

	private long[] obtainContactIdsToNotify() {
		Set<Long> contacts = new HashSet<Long>();
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
					contacts.add(contactsCursor.getLong(0));
				} while(contactsCursor.moveToNext());
			}
		}
		return createArrayFromSet(contacts);
	}

	private long[] createArrayFromSet(Set<Long> contacts) {
		long[] returnedArray = new long[contacts.size()];
		int arrayIndex = 0;
		for(Long contactId : contacts) {
			returnedArray[arrayIndex++] = contactId;
		}
		return returnedArray;
	}

	private Notification createNotification(long[] contactIds) {
		Resources resources = context.getResources();
		Notification notification = new Notification(
				R.drawable.icon,
				resources.getString(R.string.notificationTicker),
				SystemClock.currentThreadTimeMillis());
		Intent intent = new Intent(context, UserNotificationActivity.class);
		intent.putExtra(CONTACTS_IDS, contactIds);
		PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, 0);
		notification.setLatestEventInfo(
				context,
				resources.getString(R.string.notificationTitle),
				MessageFormat.format(resources.getString(R.string.notificationText), contactIds.length),
				contentIntent);

		return notification ;
	}

}
