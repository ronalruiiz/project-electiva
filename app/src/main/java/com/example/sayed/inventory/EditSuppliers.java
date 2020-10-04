package com.example.sayed.inventory;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sayed.inventory.data.InventoryContract;
import com.example.sayed.inventory.data.InventoryContract.SuppliersEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class EditSuppliers extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor>{

    public Uri mCurrentSupplierUri;

    private EditText productRName;    //1
    private EditText supplierName;    //2
    private EditText numberUnits;     //3
    private EditText debit;           //4
    private EditText credit;          //5
    private EditText rebate;          //6
    private EditText total;          //7
    private EditText date;           //8
    private EditText notes;          //9


    private int mYear;
    private int mMonth;
    private int mDay;

    /**
     * Boolean flag that keeps track of whether the supplier has been edited (true) or not (false)
     */
    private boolean mSupplierHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mSupplierHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_suppliers);

        productRName = findViewById(R.id.product_sup);
        supplierName = findViewById(R.id.name_supplier);
        numberUnits = findViewById(R.id.num_units);
        debit = findViewById(R.id.debit_sup);
        credit = findViewById(R.id.credit_sup);
        rebate = findViewById(R.id.rebate_sup);
        total = findViewById(R.id.amount_sup);
        date = findViewById(R.id.date_sup);
        notes = findViewById(R.id.notes_sup);

        Intent intent = getIntent();
        mCurrentSupplierUri = intent.getData();
        if (mCurrentSupplierUri == null) {
            setTitle("Agregar a reporte");
            invalidateOptionsMenu();
        } else {
            setTitle("Editar reporte");
            getLoaderManager().initLoader(0, null, this);
        }
        //open calender to select date and add it edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(EditSuppliers.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                                String myFormat = "dd/MM/yyyy"; //Change as you need
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                                date.setText(sdf.format(myCalendar.getTime()));
                                mDay = selectedday;
                                mMonth = selectedmonth;
                                mYear = selectedyear;
                            }
                        }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        productRName.setOnTouchListener(touchListener);
        supplierName.setOnTouchListener(touchListener);
        numberUnits.setOnTouchListener(touchListener);
        debit.setOnTouchListener(touchListener);
        credit.setOnTouchListener(touchListener);
        rebate.setOnTouchListener(touchListener);
        total.setOnTouchListener(touchListener);
        date.setOnTouchListener(touchListener);
        notes.setOnTouchListener(touchListener);
    }

    private void saveReportSupplier(){

        String proReport = productRName.getText().toString().trim();
        String custmReport = supplierName.getText().toString().trim();
        String quntReport = numberUnits.getText().toString().trim();
        String amtReport = total.getText().toString().trim();
        String creditReport = credit.getText().toString().trim();
        String rebateReport = rebate.getText().toString().trim();
        String debitReport = debit.getText().toString().trim();
        String datReport = date.getText().toString().trim();
        String notReport = notes.getText().toString().trim();


        if (null == mCurrentSupplierUri && TextUtils.isEmpty(proReport) && TextUtils.isEmpty(custmReport)
                && TextUtils.isEmpty(quntReport) && TextUtils.isEmpty(amtReport)
                && TextUtils.isEmpty(creditReport) && TextUtils.isEmpty(rebateReport)
                && TextUtils.isEmpty(debitReport) && TextUtils.isEmpty(datReport)
                && TextUtils.isEmpty(notReport)){
            Toast.makeText(this, "Por favor rellene los campos", Toast.LENGTH_LONG).show();
            return;
        }
        int intQuntity = 0;
        if (!TextUtils.isEmpty(quntReport)) {
            intQuntity = Integer.parseInt(quntReport);
        }
        int intAmount = 0;
        if (!TextUtils.isEmpty(amtReport)) {
            intAmount = Integer.parseInt(amtReport);
        }
        int intcredit = 0;
        if (!TextUtils.isEmpty(creditReport)) {
            intcredit = Integer.parseInt(creditReport);
        }
        int intrebate = 0;
        if (!TextUtils.isEmpty(rebateReport)) {
            intrebate = Integer.parseInt(rebateReport);
        }
        int intdebit = 0;
        if (!TextUtils.isEmpty(debitReport)) {
            intdebit = Integer.parseInt(debitReport);
        }
        ContentValues values = new ContentValues();
        values.put(SuppliersEntry.SUPPLIER_NAME, custmReport);
        values.put(SuppliersEntry.PRODUCT_NAME, proReport);
        values.put(SuppliersEntry.NUMBER_UNITS, intQuntity);
        values.put(SuppliersEntry.TOTAL_BALANCE, intAmount);
        values.put(SuppliersEntry.DEBIT, intcredit);
        values.put(SuppliersEntry.DEBIT, intrebate);
        values.put(SuppliersEntry.DEBIT, intdebit);
        values.put(SuppliersEntry.DATE, datReport);
        values.put(SuppliersEntry.NOTES, notReport);

        if (mCurrentSupplierUri == null) {
            Uri newUri = getContentResolver().insert(SuppliersEntry.CONTENT_URI_4, values);
            if (newUri == null) {
                Toast.makeText(this, "Error al guardar proveedor",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "proveedor guardado", Toast.LENGTH_LONG).show();
            }
        }else {
            int rowsAffected = getContentResolver().update(mCurrentSupplierUri, values,
                    null, null);
            if (rowsAffected == 0){
                Toast.makeText(this, "Error al actualizar proveedor", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "proveedor actualizado", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_supplier, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If this is a new report, hide the "Delete" menu item.
        if (mCurrentSupplierUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_supplier);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_supplier:
                saveReportSupplier();
                finish();
                return true;
            case R.id.delete_supplier:
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (!mSupplierHasChanged) {
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
                SuppliersEntry._ID,
                SuppliersEntry.SUPPLIER_NAME,
                SuppliersEntry.PRODUCT_NAME,
                SuppliersEntry.DEBIT,
                SuppliersEntry.REBATE,
                SuppliersEntry.CREDIT,
                SuppliersEntry.NUMBER_UNITS,
                SuppliersEntry.TOTAL_BALANCE,
                SuppliersEntry.DATE,
                SuppliersEntry.NOTES
        };
        return new CursorLoader(this,
                mCurrentSupplierUri,
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

            int namePro = cursor.getColumnIndex(SuppliersEntry.PRODUCT_NAME);
            int nameCstom = cursor.getColumnIndex(SuppliersEntry.SUPPLIER_NAME);
            int quant = cursor.getColumnIndex(SuppliersEntry.NUMBER_UNITS);
            int amount = cursor.getColumnIndex(SuppliersEntry.TOTAL_BALANCE);
            int debitt = cursor.getColumnIndex(SuppliersEntry.DEBIT);
            int rebitt = cursor.getColumnIndex(SuppliersEntry.REBATE);
            int creditt = cursor.getColumnIndex(SuppliersEntry.CREDIT);
            int date = cursor.getColumnIndex(SuppliersEntry.DATE);
            int notess = cursor.getColumnIndex(SuppliersEntry.NOTES);

            String nameProString = cursor.getString(namePro);
            String nameCstomString = cursor.getString(nameCstom);
            int quantString = cursor.getInt(quant);
            int amountString = cursor.getInt(amount);
            int debitStr = cursor.getInt(debitt);
            int rebitString = cursor.getInt(rebitt);
            int creditString = cursor.getInt(creditt);
            String dateString = cursor.getString(date);
            int notesString = cursor.getInt(notess);

            productRName.setText(nameProString);
            supplierName.setText(nameCstomString);
            numberUnits.setText(Integer.toString(quantString));
            total.setText(Integer.toString(amountString));
            debit.setText(Integer.toString(debitStr));
            rebate.setText(Integer.toString(rebitString));
            credit.setText(Integer.toString(creditString));
            debit.setText(dateString);
            notes.setText(Integer.toString(notesString));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productRName.setText("");
        supplierName.setText("");
        numberUnits.setText("");
        total.setText("");
        debit.setText("");
        rebate.setText("");
        credit.setText("");
        date.setText("");
        notes.setText("");
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.message);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);

        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("desea borrar el proveedor?");
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
        if (mCurrentSupplierUri != null) {
            int deleteRow = getContentResolver().delete(mCurrentSupplierUri, null, null);

            if (deleteRow == 0) {
                Toast.makeText(this, "Error con borrar report", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "El reporte esta eliminado", Toast.LENGTH_LONG).show();
            }
        }
        // Close the activity
        finish();
    }

}
