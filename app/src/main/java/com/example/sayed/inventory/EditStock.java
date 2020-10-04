package com.example.sayed.inventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sayed.inventory.data.InventoryContract.StocksEntry;

public class EditStock extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor>{

    public Uri mCurrentStockUri;

    private EditText stockN;    //1
    private EditText productN;    //2
    private EditText quantS;     //3
    private EditText parchP;           //4
    private EditText sellP;          //5
    private EditText total;          //6

    /**
     * Boolean flag that keeps track of whether the Stock has been edited (true) or not (false)
     */
    private boolean mStockHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mStockHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock);

        stockN = findViewById(R.id.name_Stock);
        productN = findViewById(R.id.p_stock);
        quantS = findViewById(R.id.q_stock);
        parchP = findViewById(R.id.Par_stock);
        sellP = findViewById(R.id.s_stock);
        total = findViewById(R.id.t_stock);

        Intent intent = getIntent();
        mCurrentStockUri = intent.getData();
        if (mCurrentStockUri == null) {
            setTitle("Add a Stock");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Stock");
            getLoaderManager().initLoader(0, null, this);
        }

        stockN.setOnTouchListener(touchListener);
        productN.setOnTouchListener(touchListener);
        quantS.setOnTouchListener(touchListener);
        parchP.setOnTouchListener(touchListener);
        sellP.setOnTouchListener(touchListener);
        total.setOnTouchListener(touchListener);

    }
    private void saveReportStock() {

        String stockReport = stockN.getText().toString().trim();
        String proReport = productN.getText().toString().trim();
        String quntReport = quantS.getText().toString().trim();
        String parReport = parchP.getText().toString().trim();
        String sellReport = sellP.getText().toString().trim();
        String totalReport = total.getText().toString().trim();


        if (null == mCurrentStockUri && TextUtils.isEmpty(stockReport)
                && TextUtils.isEmpty(proReport)
                && TextUtils.isEmpty(quntReport) && TextUtils.isEmpty(totalReport)
                && TextUtils.isEmpty(parReport) && TextUtils.isEmpty(sellReport)) {
            Toast.makeText(this, "please fill the fields", Toast.LENGTH_LONG).show();
            return;
        }
        int intQuntity = 0;
        if (!TextUtils.isEmpty(quntReport)) {
            intQuntity = Integer.parseInt(quntReport);
        }
        int intPar = 0;
        if (!TextUtils.isEmpty(parReport)) {
            intPar = Integer.parseInt(parReport);
        }
        int intSell = 0;
        if (!TextUtils.isEmpty(sellReport)) {
            intSell = Integer.parseInt(sellReport);
        }
        int intTotal = 0;
        if (!TextUtils.isEmpty(totalReport)) {
            intTotal = Integer.parseInt(totalReport);
        }
        ContentValues values = new ContentValues();
        values.put(StocksEntry.STOCK_NAME, stockReport);
        values.put(StocksEntry.PRODUCT_NAME, proReport);
        values.put(StocksEntry.QUANTITY, intQuntity);
        values.put(StocksEntry.UNIT_PRICE, intPar);
        values.put(StocksEntry.SELLING_PRICE, intSell);
        values.put(StocksEntry.TOTAL, intTotal);

        if (mCurrentStockUri == null) {
            Uri newUri = getContentResolver().insert(StocksEntry.CONTENT_URI_5, values);
            if (newUri == null) {
                Toast.makeText(this, "Error with save report stocks",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "saved report stocks", Toast.LENGTH_LONG).show();
            }
        }else {
            int rowsAffected = getContentResolver().update(mCurrentStockUri, values,
                    null, null);
            if (rowsAffected == 0){
                Toast.makeText(this, "Error with update report stocks", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "report stocks updated successful", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_stock, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCurrentStockUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_stock);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_stock:
                saveReportStock();
                finish();
                return true;
            case R.id.delete_stock:
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (!mStockHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
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
        return new CursorLoader(this,
                mCurrentStockUri,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToNext()){

            int namePro = cursor.getColumnIndex(StocksEntry.STOCK_NAME);
            int nameCstom = cursor.getColumnIndex(StocksEntry.PRODUCT_NAME);
            int quant = cursor.getColumnIndex(StocksEntry.QUANTITY);
            int parch = cursor.getColumnIndex(StocksEntry.UNIT_PRICE);
            int sell = cursor.getColumnIndex(StocksEntry.SELLING_PRICE);
            int tot = cursor.getColumnIndex(StocksEntry.TOTAL);

            String nameStock = cursor.getString(nameCstom);
            String nameProString = cursor.getString(namePro);
            int quantString = cursor.getInt(quant);
            int parchString = cursor.getInt(parch);
            int sellStr = cursor.getInt(sell);
            int totString = cursor.getInt(tot);



            stockN.setText(nameStock);
            productN.setText(nameProString);
            quantS.setText(Integer.toString(quantString));
            parchP.setText(Integer.toString(parchString));
            sellP.setText(Integer.toString(sellStr));
            total.setText(Integer.toString(totString));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stockN.setText("");
        productN.setText("");
        quantS.setText("");
        parchP.setText("");
        sellP.setText("");
        total.setText("");
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.message);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);

        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // show Delete this stock?
        builder.setMessage("delete this the report stock?");
        // if selected delete
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduction();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteProduction() {
        if (mCurrentStockUri != null) {
            int deleteRow = getContentResolver().delete(mCurrentStockUri, null, null);

            if (deleteRow == 0) {
                Toast.makeText(this, "Error with delete stock", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "The stock is deleted", Toast.LENGTH_LONG).show();
            }
        }
        // Close the activity
        finish();
    }

}
