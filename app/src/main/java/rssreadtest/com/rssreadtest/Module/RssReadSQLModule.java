package rssreadtest.com.rssreadtest.Module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class RssReadSQLModule {
    public static final String TABLE_NAME = "rss_data";
    public static final String KEY_ID = "_id";
    public static final String DATE = "date";
    public static final String DAY_OR_NIGHT_COLUMN = "day";
    public static final String TEMP_COLUMN = "temp";
    public static final String WEATHER_COLUMN = "weather";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATE + " TEXT NOT NULL, " +
                    DAY_OR_NIGHT_COLUMN + " TEXT NOT NULL, " +
                    TEMP_COLUMN + " TEXT NOT NULL, " +
                    WEATHER_COLUMN + " TEXT NOT NULL)";
    private SQLiteDatabase db;

    public RssReadSQLModule(Context context) {
        db = RssReadSQLite.getDatabase(context);
    }

    public void close() {
        db.close();
    }

    public RssReadDataItem insert(RssReadDataItem item) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, item.getDate());
        cv.put(DAY_OR_NIGHT_COLUMN, item.getDayOrNight());
        cv.put(TEMP_COLUMN, item.getTemp());
        cv.put(WEATHER_COLUMN, item.getWeather());
        long id = db.insert(TABLE_NAME, null, cv);
        item.setId(id);
        return item;
    }

    public boolean update(RssReadDataItem item) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, item.getDate());
        cv.put(DAY_OR_NIGHT_COLUMN, item.getDayOrNight());
        cv.put(TEMP_COLUMN, item.getTemp());
        cv.put(WEATHER_COLUMN, item.getWeather());
        String where = KEY_ID + "=" + item.getId();
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    public boolean delete(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    public void deleteAll(){
        db.execSQL("DROP TABLE IF EXISTS " + RssReadSQLModule.TABLE_NAME);
        db.execSQL(RssReadSQLModule.CREATE_TABLE);
    }

    public List<RssReadDataItem> getAll() {
        List<RssReadDataItem> result = new ArrayList<RssReadDataItem>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    public RssReadDataItem getRecord(Cursor cursor) {
        RssReadDataItem result = new RssReadDataItem();
        result.setId(cursor.getLong(0));
        result.setDate(cursor.getString(1));
        result.setDayOrNight(cursor.getString(2));
        result.setTemp(cursor.getString(3));
        result.setWeather(cursor.getString(4));
        return result;
    }

    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }
}
