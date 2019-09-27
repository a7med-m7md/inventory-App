package com.inventory.inventoryapp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MY_DB extends SQLiteOpenHelper {

    public static final String DB_name = "store.db";
    public static final int DB_version = 1;

    public MY_DB(Context context) {
        super(context, DB_name, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DB = "CREATE TABLE " + Contract.Entry.TABLE_NAME + " ( "
                + Contract.Entry.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + Contract.Entry.ITEM_NAME + " TEXT , "
                + Contract.Entry.ITEM_DESCRIPTION + " TEXT , "
                + Contract.Entry.ITEM_QUANTITY + " INTEGER DEFAULT 0 , "
                +Contract.Entry.ITEM_PICTURE + " BLOD , "
                +Contract.Entry.ITEM_PRICE +" INTEGER DEFAULT 0  "+");";
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*String DROP_DB = "DROP TABLE " + Contract.Entry.TABLE_NAME + " ;";
        db.execSQL(DROP_DB);
        onCreate(db);*/
    }
}
