package com.example.mobiletechapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "MobileTech";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "colour";
    public static final String COLUMN_ISSELECTED = "isSelected";

    public MyDbHelper(Context context, String name,
                      SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_NAME + " text, " +
                        COLUMN_ISSELECTED + " text" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public long insertColour(String colour, String isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, colour);
        contentValues.put(COLUMN_ISSELECTED, isSelected);

        long id = db.insert(TABLE_NAME, null, contentValues);

        return id;
    }

    public ArrayList<String> getAllColours() {
        ArrayList<String> allColours = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") String colour = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String isSelected = cursor.getString(cursor.getColumnIndex(COLUMN_ISSELECTED));
            allColours.add(colour + "," + isSelected);
            cursor.moveToNext();
        }
        return allColours;
    }

    public int deleteColour(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "id = ? ", new String[]{Long.toString(id)});

        return result;
    }

    public int updateColour(Long id, String newColour, int newSelection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, newColour);
        contentValues.put(COLUMN_ISSELECTED, newSelection);
        int result = db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(id)});
        return result;
    }

    public void deleteAllColours() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }


}
