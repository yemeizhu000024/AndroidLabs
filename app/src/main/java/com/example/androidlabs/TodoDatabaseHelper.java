package com.example.androidlabs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TODOS = "todos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_URGENCY = "urgency";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK + " TEXT,"
                + COLUMN_URGENCY + " INTEGER" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }

    // Debugging function to print cursor data
    public void printCursor(Cursor c) {
        Log.d("Database Version", String.valueOf(getReadableDatabase().getVersion()));
        Log.d("Cursor Column Count", String.valueOf(c.getColumnCount()));
        Log.d("Cursor Row Count", String.valueOf(c.getCount()));

        // Loop through rows
        while (c.moveToNext()) {
            Log.d("Row", "ID: " + c.getInt(0) + ", Task: " + c.getString(1) + ", Urgency: " + c.getInt(2));
        }
    }
}
