package com.archi.tithetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "tithe_three.db";
    private static final String CREATE_QUERY = "create table "
            + PC.PE.TABLE_NAME + "(" + PC.PE.ID + " integer primary key autoincrement,"
            + PC.PE.INPUT + " text," + PC.PE.DATE  + " text);";

    DbOperations(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        Log.d("Database operations", "Database created...");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created...");
    }

    public void addInformation(SQLiteDatabase db, String input, String date) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PC.PE.INPUT, input);
        contentValues.put(PC.PE.DATE, date);
        db.insert(PC.PE.TABLE_NAME, null, contentValues);
        Log.d("Database operations", "One Row Inserted.....");
    }

    public Cursor getInfromation(SQLiteDatabase db) {

        String[] projections = {PC.PE.ID, PC.PE.INPUT, PC.PE.DATE};
        Cursor cursor = db.query(PC.PE.TABLE_NAME,projections,
                null,null,null,null,null);

        return cursor;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PC.PE.TABLE_NAME, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + PC.PE.TABLE_NAME + "'");
        db.close();
    }

    public int deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + PC.PE.TABLE_NAME + "'");
        return db.delete(PC.PE.TABLE_NAME, "ID = ?", new String[] {id});
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + PC.PE.TABLE_NAME + "'");
        Cursor res = db.rawQuery("select * from " + PC.PE.TABLE_NAME, null);
        return res;
    }
}