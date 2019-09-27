package com.inventory.inventoryapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class Edit_activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText item_name , item_description  ;
    private TextView item_quantity,item_price;
    private Uri my_uri;
    private int counter , counter2 ;
    private Button btn_plus,btn_minus,btn_plus1,btn_minus1;
    private boolean changed;

    private View.OnTouchListener mTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            changed = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        item_name = (EditText)findViewById(R.id.itemm_name);
        item_description = (EditText)findViewById(R.id.itemm_description);
        item_quantity = (TextView) findViewById(R.id.itemm_quantity);
        item_price = (TextView)findViewById(R.id.itemm_price);

        item_name.setOnTouchListener(mTouch);
        item_description.setOnTouchListener(mTouch);
        item_quantity.setOnTouchListener(mTouch);
        item_price.setOnTouchListener(mTouch);

        counter = Integer.parseInt(item_quantity.getText().toString());
        btn_plus = (Button)findViewById(R.id.plus);
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = Integer.parseInt(item_quantity.getText().toString())+1;
                item_quantity.setText(String.valueOf(counter));
            }
        });

        btn_minus = (Button)findViewById(R.id.minus);
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(item_quantity.getText().toString())>=0) {
                    counter = Integer.parseInt(item_quantity.getText().toString())-1;
                    item_quantity.setText(String.valueOf(counter));
                }
            }
        });

        counter2 = Integer.parseInt(item_price.getText().toString()) ;
        btn_plus1 = (Button)findViewById(R.id.plus1);
        btn_plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter2 = Integer.parseInt(item_price.getText().toString()) + 1;
                item_price.setText(String.valueOf(counter2));
            }
        });

        btn_minus1 = (Button)findViewById(R.id.minus1);
        btn_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(item_price.getText().toString())>=0){
                counter2 = Integer.parseInt(item_price.getText().toString())-1;
                item_price.setText(String.valueOf(counter2));}
            }
        });




        Intent i = getIntent();
        my_uri = i.getData();

        if (my_uri==null){
            setTitle("Add item");
        }
        else {
            getSupportLoaderManager().initLoader(0, null, this);
            setTitle("Edit item");

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                //insert_item();
                update_item();
                finish();
              return true;
            case R.id.delete_for_ever:
                int deleted_row = getContentResolver().delete(my_uri,null,null);
                if (deleted_row==0)
                    Toast.makeText(this,"Deletion failed !" , Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"You delete "+ deleted_row + " items" , Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case android.R.id.home:

                if (!changed) {
                    NavUtils.navigateUpFromSameTask(Edit_activity.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(Edit_activity.this);
                            }
                        };


                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    private void insert_item(){

        String name = item_name.getText().toString().trim();
        String description = item_description.getText().toString().trim();
        String quantity = item_quantity.getText().toString().trim();

        ContentValues values1 = new ContentValues();
        values1.put(Contract.Entry.ITEM_NAME,name);
        values1.put(Contract.Entry.ITEM_DESCRIPTION,description);
        values1.put(Contract.Entry.ITEM_QUANTITY,quantity);

        getContentResolver().insert(Contract.Entry.ITEM_PATH,values1);
    }
*/
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String [] projection = {Contract.Entry._ID,
        Contract.Entry.ITEM_NAME,
        Contract.Entry.ITEM_DESCRIPTION,
        Contract.Entry.ITEM_QUANTITY,
        Contract.Entry.ITEM_PRICE};
        return new CursorLoader(this,my_uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount()<1){
            return;
        }

        if (cursor.moveToFirst()){
            int name = cursor.getColumnIndex(Contract.Entry.ITEM_NAME);
            int description = cursor.getColumnIndex(Contract.Entry.ITEM_DESCRIPTION);
            int quantity = cursor.getColumnIndex(Contract.Entry.ITEM_QUANTITY);
            int price = cursor.getColumnIndex(Contract.Entry.ITEM_PRICE);


            String sname = cursor.getString(name);
            String sdescription = cursor.getString(description);
            String squantity = cursor.getString(quantity);
            String sprice = cursor.getString(price);

            item_name.setText(sname);
            item_description.setText(sdescription);
            item_quantity.setText(squantity);
            item_price.setText(sprice);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        item_quantity.setText("");
        item_name.setText("");
        item_description.setText("");
        item_price.setText("");
    }

    private void update_item() {

        String name = item_name.getText().toString().trim();
        String description = item_description.getText().toString().trim();
        String quantity = item_quantity.getText().toString().trim();
        String price = item_price.getText().toString().trim();
        if (my_uri == null && TextUtils.isEmpty(name) && TextUtils.isEmpty(description) && TextUtils.isEmpty(quantity)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Contract.Entry.ITEM_NAME, name);
        values.put(Contract.Entry.ITEM_DESCRIPTION, description);
        values.put(Contract.Entry.ITEM_QUANTITY, quantity);
        values.put(Contract.Entry.ITEM_PRICE,price);
        if (my_uri == null) {
            Uri new_uri = getContentResolver().insert(Contract.Entry.ITEM_PATH, values);
            if (new_uri == null)
                Toast.makeText(Edit_activity.this, "Falied to insert", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Edit_activity.this, "Successfully added !", Toast.LENGTH_SHORT).show();

        } else {
            int row_affected = getContentResolver().update(my_uri, values, null, null);
            if (row_affected == 0)
                Toast.makeText(Edit_activity.this, "Update failed", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Edit_activity.this, "Update successful !", Toast.LENGTH_SHORT).show();
        }
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Go Back without saving ?");
        builder.setPositiveButton("Yes", discardButtonClickListener);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(!changed)
            super.onBackPressed();
        DialogInterface.OnClickListener discard = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangesDialog(discard);
    }
}
