package com.inventory.inventoryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CursorAdapter extends android.widget.CursorAdapter {
    public CursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //ImageView item_img = (ImageView)view.findViewById(R.id.item_img);
        TextView item_txt_name = (TextView)view.findViewById(R.id.item_name);
        TextView item_txt_desc = (TextView)view.findViewById(R.id.item_description);
        TextView item_txt_quantity = (TextView)view.findViewById(R.id.item_quantity);

        int name = cursor.getColumnIndex(Contract.Entry.ITEM_NAME);
        int description = cursor.getColumnIndex(Contract.Entry.ITEM_DESCRIPTION);
        int quantity = cursor.getColumnIndex(Contract.Entry.ITEM_QUANTITY);

        String txt_name = cursor.getString(name);
        String txt_desc = cursor.getString(description);
        int txt_quantity = cursor.getInt(quantity);
        if(txt_quantity == 0 ){
            item_txt_quantity.setText("Not available");

        }
        else {
            item_txt_quantity.setText("Available");

        }
        item_txt_name.setText(txt_name);
        item_txt_desc.setText(txt_desc);

        //item_img.setImageResource(R.drawable.ic_add_circle_black_24dp);
    }
}
