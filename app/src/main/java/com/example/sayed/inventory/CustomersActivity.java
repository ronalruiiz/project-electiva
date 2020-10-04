package com.example.sayed.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.sayed.inventory.data.InventoryContract.CustomersEntry;

public class CustomersActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CUSTOMER_LOADER = 0;
    private CustomersAdapter customersAdapter;
    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        FloatingActionButton fab = findViewById(R.id.fabCustomer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomersActivity.this, EditCustomersActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_customers);
        //View emptyView = findViewById(R.id.empty_view);
        //listView.setEmptyView(emptyView);
        customersAdapter = new CustomersAdapter(this, null);
        listView.setAdapter(customersAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomersActivity.this, EditCustomersActivity.class);

                Uri currentProductionUri = ContentUris.withAppendedId(CustomersEntry.CONTENT_URI_2, id);

                intent.setData(currentProductionUri);
                startActivity(intent);
            }
        });
        setTitle("List Customers");
        getLoaderManager().initLoader(CUSTOMER_LOADER, null,this);
    }
    private void insertCustomer(){

        ContentValues values = new ContentValues();
        values.put(CustomersEntry.CUSTOMER_NAME, "Jone");
        values.put(CustomersEntry.CUSTOMER_ADDRESS, "Country-city-Street");
        values.put(CustomersEntry.CUSTOMER_PHONE, "01234567890");
        values.put(CustomersEntry.CUSTOMER_EMAIL, "example@gmail.com");
        values.put(CustomersEntry.CUSTOMER_NOTES, "notes for this the customer");

        Uri newUri = getContentResolver().insert(CustomersEntry.CONTENT_URI_2, values);
    }
    private void deleteAll() {

        int deletAll = getContentResolver().delete(CustomersEntry.CONTENT_URI_2, null, null);
        Log.v("CustomerActivity", deletAll + " rows deleted from Customer database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customers, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.insert_dummy_customers:
                insertCustomer();
                //displayInfo();
                return true;
            case R.id.delete_all_customers:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CustomersEntry._ID,
                CustomersEntry.CUSTOMER_NAME,
                CustomersEntry.CUSTOMER_PHONE
        };
        return new android.content.CursorLoader(this,
                CustomersEntry.CONTENT_URI_2,
                projection,
                null,
                null,null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        customersAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        customersAdapter.swapCursor(null);
    }
}
