/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import pl.nitroit.saintsday.db.SaintsDayDao;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.RemoteViews;

/**
 * @author kbl
 *
 */
public class SaintsDayWidget extends AppWidgetProvider {

	private static final int REAL_MONTH = 1;

	private SaintsDayDao dao;
	private StringBuilder builder;

	private String[] todaySaints;

	@Override
	public void onUpdate(
			Context context,
			AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		initializeDao(context);
		builder = new StringBuilder();

		String todaysSaints = prepareTodaySaints();
		for(int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			views.setTextViewText(R.id.saintsDayNames, todaysSaints);
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}

		dao.close();

		List<String> x = new ArrayList<String>();

		for(String saintName : todaySaints) {
			Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, saintName);
			Cursor contactsCursor = context.getContentResolver().query(
					contactUri,
					new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.Contacts.DISPLAY_NAME},
					null, null, null);
			if(contactsCursor.moveToFirst()) {
				do {
					int id = contactsCursor.getInt(0);
					String displayName = contactsCursor.getString(1);
					x.add(displayName);
				} while(contactsCursor.moveToNext());
			}
		}

	}

	private void initializeDao(Context context) {
		if(dao == null) {
			dao = new SaintsDayDao(context);
		}
		dao.open();
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

}
