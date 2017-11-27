package data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static data.DatabaseContract.Measures.TABLE_NAME;


/**
 * Помощник для базы данных. Инициализация будет через контекст, чтоб всегда был лишь один помощник
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper sInstance;
    private static final String DATABASE_NAME = "database.db"; //name
    private static final int DATABASE_VERSION = 2; // version

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_DATABASE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + DatabaseContract.Measures._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.Measures.COLUMN_NAME + " TEXT NOT NULL, " +
                DatabaseContract.Measures.COLUMN_DISEASE + " TEXT NOT NULL, " +
                DatabaseContract.Measures.COLUMN_GENDER + " INTEGER NOT NULL DEFAULT 3, " +
                DatabaseContract.Measures.COLUMN_AGE + " INTEGER NOT NULL DEFAULT 0, " +
                DatabaseContract.Measures.COLUMN_MISC + " TEXT, " +
                DatabaseContract.Measures.COLUMN_MEASURE + " INTEGER NOT NULL DEFAULT 0, " +
                DatabaseContract.Measures.COLUMN_AREA + " TEXT NOT NULL, "
                + DatabaseContract.Measures.COLUMN_COMPARE + " INTEGER NOT NULL)";

        db.execSQL(SQL_CREATE_DATABASE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновлено с версии " + oldVersion + " до версии " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
