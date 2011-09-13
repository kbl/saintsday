/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author kbl
 *
 */
public class SaintsDayDao {

	public static final String TABLE = "names";

	public static final String DAY_COLUMN = "day";
	public static final String MONTH_COLUMN = "month";
	public static final String NAME_COLUMN = "name";

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

	public List<String> getSaintsForDate(Integer month, Integer day) {
		Cursor names = db.query(
				TABLE,
				new String[] { NAME_COLUMN },
				"WHERE month = ? AND day = ?",
				new String[] { String.valueOf(month), String.valueOf(day)}, null, null, null);

		return getNamesFromCursor(names);
	}

	private List<String> getNamesFromCursor(Cursor names) {
		List<String> returnedNames = new ArrayList<String>(names.getCount());

		int nameColumnIndex = 0;
		names.moveToFirst();
		do {
			returnedNames.add(names.getString(nameColumnIndex));
		} while(names.moveToNext());

		return returnedNames;
	}

}
