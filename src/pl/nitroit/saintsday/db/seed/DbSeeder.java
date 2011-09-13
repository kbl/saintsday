/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday.db.seed;

import pl.nitroit.saintsday.db.SaintsDayDao;
import pl.nitroit.saintsday.db.seed.CsvParser.Record;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author kbl
 *
 */
public class DbSeeder {

	private static final String RAW_DATA_FILE_PATH = "/assets/pl_saints_days.csv";

	private SQLiteDatabase db;
	private ContentValues values;

	public DbSeeder(SQLiteDatabase db) {
		this.db = db;
		values = new ContentValues();
	}

	public void seedDb() {
		CsvParser parser = new CsvParser(RAW_DATA_FILE_PATH);
		try {
			db.beginTransaction();
			CsvParser.Record record = null;
			while((record = parser.readRecord()) != null) {
				insertRows(record);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			parser.close();
		}
	}

	private void insertRows(Record record) {
		values.put(SaintsDayDao.DAY_COLUMN, record.day);
		values.put(SaintsDayDao.MONTH_COLUMN, record.month);
		for(String name : record.names) {
			values.put(SaintsDayDao.NAME_COLUMN, name);
			db.insert(SaintsDayDao.TABLE, null, values);
		}
	}

}
