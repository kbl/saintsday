/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import pl.nitroit.saintsday.db.SaintsDayDao;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
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
		new UserNotifier().notifiyAboutTodaySaints();

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

	private final class UserNotifier {

		public void notifiyAboutTodaySaints() {
			if(notificationShouldBeSend()) {
				Set<Integer> contacts = obtainContactsToNotify();
				dao.setLastNotifiedAndRemoveOldTimestamp(new Date());
			}
		}

		private Set<Integer> obtainContactsToNotify() {
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

		private boolean notificationShouldBeSend() {
			long notificationTimestamp = dao.getLastNotifiedTimestamp();
			return notificationTimestamp == SaintsDayDao.NO_NOTIFICATION ||
					!DateUtils.isToday(notificationTimestamp);
		}

	}

}
