package com.inventory.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class Provider extends ContentProvider {
    private MY_DB my_db;
    private static final int items = 100;
    private static final int one_item = 101;
    static UriMatcher surimatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        surimatcher.addURI(Contract.authority, Contract.Entry.TABLE_NAME, items);
        surimatcher.addURI(Contract.authority, Contract.Entry.TABLE_NAME + "/#", one_item);
    }

    @Override
    public boolean onCreate() {
        my_db = new MY_DB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = my_db.getReadableDatabase();
        int match = surimatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case items:
                cursor = database.query(Contract.Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case one_item:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(Contract.Entry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't do query now for " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = surimatcher.match(uri);

        switch (match) {
            case items:
                return insert_item(uri, values);
            default:
                throw new IllegalArgumentException("Insertion isn't support for " + uri);
        }



    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = surimatcher.match(uri);
        int row_affected = 0;
        SQLiteDatabase database = my_db.getWritableDatabase();
        switch (match) {
            case items:
                row_affected = database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                break;
            case one_item:
                selection = Contract.Entry.ITEM_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                row_affected = database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can't process deletion process");

        }

        if(row_affected!=0)
            getContext().getContentResolver().notifyChange(uri,null);
            return row_affected;
    }



    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = surimatcher.match(uri);

        SQLiteDatabase database = my_db.getWritableDatabase();
        switch (match){
            case items:
                return update_item(uri,values,selection,selectionArgs);
            case one_item:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return update_item(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Error in updating !");
        }
    }

    private Uri insert_item(Uri uri , ContentValues values){
        SQLiteDatabase database = my_db.getWritableDatabase();
        long id = database.insert(Contract.Entry.TABLE_NAME,null,values);
        if(id == -1){
            Toast.makeText(getContext(),"Failed to insert new item !",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(),"new item add !" , Toast.LENGTH_LONG).show();
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    private int update_item(Uri uri , ContentValues values , String selection , String [] selectionArgs){
        SQLiteDatabase database = my_db.getWritableDatabase();
        int row_affected = database.update(Contract.Entry.TABLE_NAME,values,selection,selectionArgs);
        if(row_affected!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        if (values.size()==0)
            return 0;
        return row_affected;
    }

}
