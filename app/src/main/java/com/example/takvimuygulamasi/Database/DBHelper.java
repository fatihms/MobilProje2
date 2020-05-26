package com.example.takvimuygulamasi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.takvimuygulamasi.Model.EventModel;
import com.example.takvimuygulamasi.Model.ReminderModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG = "DBHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarApp";

    private static final String TABLE_EVENT = "events";
    private static final String TABLE_REMINDER = "reminders";

    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_START_DATE = "start_time";
    private static final String KEY_END_DATE = "end_time";
    private static final String KEY_LOCATION = "location";

    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_EVENT_NAME = "event_name";
    private static final String KEY_TIME = "reminder_time";
    private static final String KEY_DATE = "reminder_date";
    private static final String KEY_REQUEST = "reminder_request";
    private static final String KEY_REPEAT = "reminder_repeat";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE "
            + TABLE_EVENT + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_START_DATE + " TEXT,"
            + KEY_END_DATE + " TEXT,"
            + KEY_LOCATION + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_REMINDER = "CREATE TABLE "
            + TABLE_REMINDER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EVENT_ID + " INTEGER,"
            + KEY_TIME + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_REQUEST + " INTEGER,"
            + KEY_REPEAT + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);

        onCreate(db);
    }

    // ------------- event ---------------------------------


    public long createEvent(EventModel event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_START_DATE, event.getStartDate());
        values.put(KEY_END_DATE, event.getEndDate());
        values.put(KEY_LOCATION, event.getLocation());
        //values.put(KEY_CREATED_AT, getDateTime());

        long event_id = db.insert(TABLE_EVENT, null, values);

        return event_id;
    }

    public EventModel getEvent(long event_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_EVENT + " WHERE "
                + KEY_ID + " = " + event_id;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        EventModel td = new EventModel();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        td.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
        td.setStartDate((c.getString(c.getColumnIndex(KEY_START_DATE))));
        td.setEndDate((c.getString(c.getColumnIndex(KEY_END_DATE))));
        td.setLocation((c.getString(c.getColumnIndex(KEY_LOCATION))));
        //td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return td;
    }

    public List<EventModel> getAllEvents() {
        List<EventModel> events = new ArrayList<EventModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                EventModel td = new EventModel();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                td.setDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
                td.setStartDate((c.getString(c.getColumnIndex(KEY_START_DATE))));
                td.setEndDate((c.getString(c.getColumnIndex(KEY_END_DATE))));
                td.setLocation((c.getString(c.getColumnIndex(KEY_LOCATION))));
                //td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                events.add(td);
            } while (c.moveToNext());
        }

        return events;
    }

    public int updateEvent(EventModel event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_START_DATE, event.getStartDate());
        values.put(KEY_END_DATE, event.getEndDate());
        values.put(KEY_LOCATION, event.getLocation());
        //values.put(KEY_CREATED_AT, getDateTime());

        return db.update(TABLE_EVENT, values, KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public void deleteEvent(long event_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT, KEY_ID + " = ?",
                new String[]{String.valueOf(event_id)});
    }


    // ----------------reminder ----------------//

    public long createReminder(long event_id, ReminderModel reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_ID, event_id);
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_REQUEST, reminder.getRequest());
        values.put(KEY_REPEAT, reminder.getRepeat());
        //EventModel ev = getEvent(event_id);
        //values.put(KEY_EVENT_NAME, ev.getName());
        //values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_REMINDER, null, values);

        return id;
    }


    public List<ReminderModel> getAllReminders() {
        List<ReminderModel> reminders = new ArrayList<ReminderModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ReminderModel td = new ReminderModel();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setTime((c.getString(c.getColumnIndex(KEY_TIME))));
                td.setDate((c.getString(c.getColumnIndex(KEY_DATE))));
                td.setRepeat((c.getString(c.getColumnIndex(KEY_REPEAT))));
                td.setRequest(c.getInt(c.getColumnIndex(KEY_REQUEST)));
                td.setEventID(c.getInt(c.getColumnIndex(KEY_EVENT_ID)));
                //td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                reminders.add(td);
            } while (c.moveToNext());
        }

        return reminders;
    }

    public void deleteReminder(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDER, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}