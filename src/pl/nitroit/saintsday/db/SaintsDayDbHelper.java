/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday.db;

import pl.nitroit.saintsday.db.seed.DbSeeder;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author kbl
 *
 */
public class SaintsDayDbHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DB_NAME = "saints_day.db";

	private static final String CREATE_DB_STMT = "CREATE  TABLE names (" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
			"day INTEGER NOT NULL," +
			"month INTEGER NOT NULL," +
			"name VARCHAR NOT NULL);";

	// unfortunatelly context in parent isn't accessible ):
	private Context context;

	public SaintsDayDbHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB_STMT);
		new DbSeeder(context.getAssets(), db).seedDb();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}
