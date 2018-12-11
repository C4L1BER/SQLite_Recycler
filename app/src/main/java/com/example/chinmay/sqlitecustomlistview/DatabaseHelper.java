package com.example.chinmay.sqlitecustomlistview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "users.db";
    private static final String TABLE_NAME = "users_data";
    private static final String COL1 = "ID";
    private static final String COL2 = "UNAME";
    private static final String COL3 = "EMAIL";
    private static final String COL4 = "PHONE";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " UNAME TEXT, EMAIL TEXT, PHONE TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Adds data to the database.
    public boolean addData(String uName, String eMail, String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        Log.i(TAG, "DatabaseItems "+count);
        if(count == 0)
        {
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
        }// Checks if there are any items in the database, if not then resets the autoincrement count to 0.
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, uName);
        contentValues.put(COL3, eMail);
        contentValues.put(COL4, phone);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // Gets all content from the database.
    public Cursor getListContents()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL1 + " ASC ", null);
        return data;
    }

    public boolean updateData(String id, String uName, String eMail, String phone)
    {
        // Checks whether there's a record for the entered ID.
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT "+COL1+" FROM "+TABLE_NAME+" WHERE ID= "+id;
        Log.i(TAG, "SQL query: "+sql);
        cursor = db.rawQuery(sql, null);
        Log.i(TAG, "Cursor Count: "+ cursor.getCount());

        // If record found then update, else return false
        if(cursor.getCount()>0)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, id);
            contentValues.put(COL2, uName);
            contentValues.put(COL3, eMail);
            contentValues.put(COL4, phone);
            db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
            cursor.close();
            return true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }

    // Deletes the data of the entered ID.
    public int deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int data = db.delete(TABLE_NAME, "ID = ?", new String[]{id});
        Log.i(TAG, "Number of data deleted: "+data);
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        Log.i(TAG, "Remaining data in the database: "+count);
        if(count == 0)
        {
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
            Log.i(TAG, "Reset AutoIncrement count!");
        }// Checks if there are any items in the database, if not then resets the autoincrement count to 0.
        return data;
    }
}
