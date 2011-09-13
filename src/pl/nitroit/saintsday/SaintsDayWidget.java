/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import pl.nitroit.saintsday.db.SaintsDayDao;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * @author kbl
 *
 */
public class SaintsDayWidget extends AppWidgetProvider {

	private static final int REAL_MONTH = 1;

	private SaintsDayDao dao;
	private StringBuilder builder;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		dao = new SaintsDayDao(context);
		builder = new StringBuilder();
	}

	@Override
	public void onUpdate(
			Context context,
			AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		String todaysSaints = prepareTodaySaints();
		for(int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			views.setTextViewText(R.id.saintsDayNames, todaysSaints);
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}
	}

	private String prepareTodaySaints() {
		Calendar x = GregorianCalendar.getInstance();
		int day = x.get(Calendar.DAY_OF_MONTH);
		int realMonth = x.get(Calendar.MONTH) + REAL_MONTH;
		List<String> names = dao.getSaintsForDate(realMonth, day);
		return concatenateNames(names);
	}

	private String concatenateNames(List<String> names) {
		builder.setLength(0);
		for(String name : names) {
			builder.append(name);
			builder.append(" ");
		}
		return builder.toString();
	}

}
