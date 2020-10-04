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

import com.example.sayed.inventory.data.InventoryContract.StocksEntry;

public class Stocks extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private StocksAdapter stocksAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        setTitle("Stocks");

        FloatingActionButton fab = findViewById(R.id.fab_stock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stocks.this, EditStock.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_stock);
        //View emptyView = findViewById(R.id.empty_view);
        //listView.setEmptyView(emptyView);
        stocksAdapter = new StocksAdapter(this, null);
        listView.setAdapter(stocksAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Stocks.this, EditStock.class);
                Uri currentProductionUri = ContentUris.withAppendedId(StocksEntry.CONTENT_URI_5, id);

                intent.setData(currentProductionUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(0, null,this);
    }

    private void insertStock(){

        ContentValues values = new ContentValues();
        values.put(StocksEntry.STOCK_NAME, "AZ");
        values.put(StocksEntry.PRODUCT_NAME, "Product");
        values.put(StocksEntry.QUANTITY, 500);
        values.put(StocksEntry.UNIT_PRICE, 8);
        values.put(StocksEntry.SELLING_PRICE, 10);
        values.put(StocksEntry.TOTAL, 10000);

        Uri newUri = getContentResolver().insert(StocksEntry.CONTENT_URI_5, values);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.insert_dummy_stock:
                insertStock();
                //displayInfo();
                return true;
            case R.id.delete_all_stocks:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {

        int deletAll = getContentResolver().delete(StocksEntry.CONTENT_URI_5, null, null);
        Log.v("Stocks", deletAll + " rows deleted from reports database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                StocksEntry._ID,
                StocksEntry.STOCK_NAME,
                StocksEntry.PRODUCT_NAME,
                StocksEntry.QUANTITY,
                StocksEntry.UNIT_PRICE,
                StocksEntry.SELLING_PRICE,
                StocksEntry.TOTAL
        };
        return new android.content.CursorLoader(this,
                StocksEntry.CONTENT_URI_5,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        stocksAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stocksAdapter.swapCursor(null);
    }

}

