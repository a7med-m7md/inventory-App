package com.inventory.inventoryapp;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    public Contract(){}
    public final static String authority = "com.inventory.inventoryapp";
    public final static String path = "items";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://"+authority);

    public final static String my_uri = "content://inventory.com"+Entry.TABLE_NAME;

    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public  static final  Uri ITEM_PATH = Uri.withAppendedPath(BASE_CONTENT_URI,path);
        public static final String ITEM_ID = "_id";
        public static final String ITEM_NAME = "name";
        public static final String ITEM_DESCRIPTION = "description";
        public static final String ITEM_QUANTITY = "quantity";
        public static final String ITEM_PICTURE = "image";
        public static final String ITEM_PRICE = "price";

    }
}
