/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pl.nitroit.saintsday.db.SaintsDayDao;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author kbl
 *
 */
public class SaintsDayWidget extends AppWidgetProvider {

	private static final String WHERE_EXISTS_PHONE_NUMBER = ContactsContract.Data.HAS_PHONE_NUMBER + " = 1";

	private static final int REAL_MONTH = 1;

	private SaintsDayDao dao;
	private StringBuilder builder;
	private Context context;

	private String[] todaySaints;

	@Override
	public void onUpdate(
			Context context,
			AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		builder = new StringBuilder();
		this.context = context;

		initializeDao();
		updateWidgetView(appWidgetManager, appWidgetIds);
		notifiyAboutTodaySaints();

		dao.close();
	}

	private void initializeDao() {
		if(dao == null) {
			dao = new SaintsDayDao(context);
		}
		dao.open();
	}

	private void updateWidgetView(
			AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		String todaysSaints = prepareTodaySaints();
		for(int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			views.setTextViewText(R.id.saintsDayNames, todaysSaints);
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}
	}

	private String prepareTodaySaints() {
		Calendar calendar = GregorianCalendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int realMonth = calendar.get(Calendar.MONTH) + REAL_MONTH;
		todaySaints = dao.getSaintsForDate(realMonth, day);
		return concatenateNames();
	}

	private String concatenateNames() {
		builder.setLength(0);
		for(String name : todaySaints) {
			builder.append(name);
			builder.append(" ");
		}
		return builder.toString();
	}

	private void notifiyAboutTodaySaints() {
		long notificationTimestamp = dao.getLastNotifiedTimestamp();
		if(notificationTimestamp == SaintsDayDao.NO_NOTIFICATION || !DateUtils.isToday(notificationTimestamp)) {
			dao.setLastNotifiedAndRemoveOldTimestamp(new Date());

			List<String> x = new ArrayList<String>();
			ContentResolver contentResolver = context.getContentResolver();

			for(String saintName : todaySaints) {
				Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, saintName);
				Cursor contactsCursor = contentResolver.query(
						contactUri,
						new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.Contacts.DISPLAY_NAME},
						WHERE_EXISTS_PHONE_NUMBER,
						null,
						null);
				Log.d("x", contactUri.toString());
				Log.d("x", String.valueOf(contactsCursor.getCount()));
				if(contactsCursor.moveToFirst()) {
					do {
						int id = contactsCursor.getInt(0);
						String displayName = contactsCursor.getString(1);
						x.add(displayName);
						Log.d("x", displayName + " " + id);
					} while(contactsCursor.moveToNext());
				}
			}

		}
	}

}
