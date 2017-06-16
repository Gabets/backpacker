package com.blessedtactics.programs.backpacker.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    public ItemsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemContract.ItemsDbColumns.TABLE_NAME + " ("
                + ItemContract.ItemsDbColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemContract.ItemsDbColumns.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemContract.ItemsDbColumns.COLUMN_TYPE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_ITEMS_TABLE);

        String SQL_ADD_ITEMS = "INSERT INTO " + ItemContract.ItemsDbColumns.TABLE_NAME + " ("
                + ItemContract.ItemsDbColumns.COLUMN_NAME + ", "
                + ItemContract.ItemsDbColumns.COLUMN_TYPE + ") "
                + "VALUES ('Meal', 'c');";
        db.execSQL(SQL_ADD_ITEMS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
