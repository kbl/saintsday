/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday.db;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author kbl
 *
 */
public class SaintsDayDao {

	public static final int NO_NOTIFICATION = 0;

	private static final String NOTIFICATIONS_HISTORY_TABLE = "notifications_history";
	private static final String NOTIFICATIONS_HISTORY_TIMESTAMP_COLUMN = "notification_timestamp";

	public static final String NAMES_TABLE = "names";

	public static final String NAMES_DAY_COLUMN = "day";
	public static final String NAMES_MONTH_COLUMN = "month";
	public static final String NAMES_NAME_COLUMN = "name";

	private SaintsDayDbHelper helper;
	private SQLiteDatabase db;

	public SaintsDayDao(Context context) {
		helper = new SaintsDayDbHelper(context);
	}

	public SaintsDayDao open() {
		if(db == null || !db.isOpen()) {
			db = helper.getReadableDatabase();
		}
		return this;
	}

	public void close() {
		db.close();
	}

	public String[] getSaintsForDate(Integer month, Integer day) {
		Cursor names = db.query(
				NAMES_TABLE,
				new String[] { NAMES_NAME_COLUMN },
				"month = ? AND day = ?",
				new String[] { String.valueOf(month), String.valueOf(day)}, null, null, null);

		return getNamesFromCursor(names);
	}

	private String[] getNamesFromCursor(Cursor names) {
		String[] returnedNames = new String[names.getCount()];

		int nameColumnIndex = 0;
		int arrayIndex = 0;
		names.moveToFirst();
		do {
			returnedNames[arrayIndex++] = names.getString(nameColumnIndex);
		} while(names.moveToNext());

		return returnedNames;
	}

	public long getLastNotifiedTimestamp() {
		Cursor notificationHistory = db.query(
				NOTIFICATIONS_HISTORY_TABLE,
				new String[] {NOTIFICATIONS_HISTORY_TIMESTAMP_COLUMN},
				null, null, null, null,
				"_id desc");
		if(notificationHistory.moveToFirst()) {
			return notificationHistory.getLong(0);
		}
		return NO_NOTIFICATION;
	}

	public void setLastNotifiedAndRemoveOldTimestamp(Date notificationTime) {
		try {
			db.beginTransaction();

			deleteOldAndInsertNewNotificationTimestamp(notificationTime);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	private void deleteOldAndInsertNewNotificationTimestamp(
			Date notificationTime) {
		db.delete(NOTIFICATIONS_HISTORY_TABLE, null, null);
		ContentValues values = new ContentValues();
		values.put(NOTIFICATIONS_HISTORY_TIMESTAMP_COLUMN, notificationTime.getTime());
		db.insert(NOTIFICATIONS_HISTORY_TABLE, null, values);
	}

}
