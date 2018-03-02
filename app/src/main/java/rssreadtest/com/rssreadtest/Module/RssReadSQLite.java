package rssreadtest.com.rssreadtest.Module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RssReadSQLite extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "RssRead.db";
    public static int VERSION = 1;
    private static SQLiteDatabase database;
    public RssReadSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RssReadSQLModule.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RssReadSQLModule.TABLE_NAME);
        onCreate(db);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new RssReadSQLite(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return database;
    }
}
