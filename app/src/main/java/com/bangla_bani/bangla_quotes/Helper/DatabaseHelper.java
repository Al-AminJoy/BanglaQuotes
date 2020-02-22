package com.bangla_bani.bangla_quotes.Helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "QuotesList.db";
    private static String TABLE_NAME = "QuotesTable";
    private static String ID = "Id";
    private static String Quotes = "Quotes";
    private static String CATAGORY = "Catagory";
    private static String FAVOURITE = "Favourite";
    private static int Version = 23;

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Quotes + " VARCHAR(10000)," + CATAGORY + " VARCHAR(200)," + FAVOURITE + " VARCHAR(5))";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SHOW_ALL_DATA = "SELECT * FROM " + TABLE_NAME;
    private static final String SHOW_FAV_DATA = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=?" + " AND " + FAVOURITE + " ='true'";
    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            //Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Exception : " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            //Toast.makeText(context, "Database Updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //Insert Data
    public long insertData(String quotes, String catagory, String favourite) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Quotes, quotes);
        contentValues.put(CATAGORY, catagory);
        contentValues.put(FAVOURITE, favourite);
        long rowID = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return rowID;
    }

    // Read All Data
    public Cursor readData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SHOW_ALL_DATA, null);
        return cursor;
    }

    //Update Data
    public boolean updateData(String id, String quotes, String catagory, String favourite) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(Quotes, quotes);
        contentValues.put(CATAGORY, catagory);
        contentValues.put(FAVOURITE, favourite);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    //Read Only Favourite Data
    public Cursor readFavData(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SHOW_FAV_DATA, new String[]{id});
        return cursor;
    }
}