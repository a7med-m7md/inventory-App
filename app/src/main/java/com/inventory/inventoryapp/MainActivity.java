package com.inventory.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    CursorAdapter adapter;
    SQLiteDatabase database;
    private MY_DB my_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lst = (ListView)findViewById(R.id.lst);
         adapter = new CursorAdapter(this , null);
        lst.setAdapter(adapter);
        TextView empty = (TextView)findViewById(R.id.txt_empty) ;
        lst.setEmptyView(empty);

        //getSupportLoaderManager().initLoader(0,null,this);
        getSupportLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Edit_activity.class);
                startActivity(i);
            }
        });

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this , Edit_activity.class);
                Uri uri = ContentUris.withAppendedId(Contract.Entry.ITEM_PATH,id);
                i.setData(uri);
                startActivity(i);
            }
        });



    }

    public void insert_item(){
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.ITEM_NAME,"Pepsi");
        values.put(Contract.Entry.ITEM_DESCRIPTION , "Fore all");
        values.put(Contract.Entry.ITEM_QUANTITY,4);
        values.put(Contract.Entry.ITEM_PRICE,10);
        getContentResolver().insert(Contract.Entry.ITEM_PATH,values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_all,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                delete_all();
                return true;
            case R.id.show:
                insert_item();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String [] projection = {Contract.Entry._ID,
                                Contract.Entry.ITEM_NAME,
                                Contract.Entry.ITEM_DESCRIPTION,
                                Contract.Entry.ITEM_QUANTITY,
                                Contract.Entry.ITEM_PRICE
                                };

        return new CursorLoader(this,
                Contract.Entry.ITEM_PATH,
                projection,
                null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void delete_all(){
        int deleted_rows = getContentResolver().delete(Contract.Entry.ITEM_PATH,null,null);
        Toast.makeText(MainActivity.this,"You deleted "+deleted_rows + " rows" ,Toast.LENGTH_LONG).show();

    }
}
