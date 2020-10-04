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

import com.example.sayed.inventory.data.InventoryContract.DocumentsEntry;

public class ReportsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private ReportsAdapter reportsAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        setTitle("Clientes");

        FloatingActionButton fab = findViewById(R.id.fab_reports);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsActivity.this, EditReportActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_report);
        //View emptyView = findViewById(R.id.empty_view);
       //listView.setEmptyView(emptyView);
        reportsAdapter = new ReportsAdapter(this, null);
        listView.setAdapter(reportsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ReportsActivity.this, EditReportActivity.class);
                Uri currentProductionUri = ContentUris.withAppendedId(DocumentsEntry.CONTENT_URI_3, id);

                intent.setData(currentProductionUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(0, null,this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DocumentsEntry._ID,
                DocumentsEntry.CUSTOMER,
                DocumentsEntry.PRODUCT,
                DocumentsEntry.DEBIT,
                DocumentsEntry.REBATE,
                DocumentsEntry.AMOUNT,

        };
        return new android.content.CursorLoader(this,
                DocumentsEntry.CONTENT_URI_3,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        reportsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        reportsAdapter.swapCursor(null);

    }
}
