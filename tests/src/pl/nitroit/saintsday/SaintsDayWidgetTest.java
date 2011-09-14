/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.test.AndroidTestCase;

/**
 * @author kbl
 *
 */
public class SaintsDayWidgetTest extends AndroidTestCase {

	private SaintsDayWidget widget;
	private SaintsContext context;
	private Intent intent;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		widget = new SaintsDayWidget();
		context = new SaintsContext();
		intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	}

	public void test() {
		widget.onReceive(context, intent);
	}

}
