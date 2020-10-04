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

import com.example.sayed.inventory.data.InventoryContract.DocumentsEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditReportActivity extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor>{

    public Uri mCurrentReportUri;

    private EditText productReport;  //1
    private EditText customerReport; //2
    private EditText qtyReport;      //3
    private EditText debit;           //4
    private EditText credit;        //5
    private EditText rebate;          //6
    private EditText amountReport;    //7
    private EditText dateReport;     //8
    private EditText notesReport;    //9


    private int mYear;
    private int mMonth;
    private int mDay;




    private boolean mReportHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mReportHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);

        productReport = findViewById(R.id.name_product_report);
        customerReport = findViewById(R.id.name_customer_report);
        qtyReport = findViewById(R.id.quantity_report);
        amountReport = findViewById(R.id.amount_report);
        debit = findViewById(R.id.debit_report);
        credit = findViewById(R.id.credit_report);
        rebate = findViewById(R.id.rebate_report);
        dateReport = findViewById(R.id.date_clicked);
        notesReport = findViewById(R.id.notes_report);

        Intent intent = getIntent();
        mCurrentReportUri = intent.getData();
        if (mCurrentReportUri == null) {
            setTitle("Agregar a reporte");

            invalidateOptionsMenu();
        } else {
            setTitle("Editar reporte");
            getLoaderManager().initLoader(0, null, this);
        }
        dateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(EditReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        dateReport.setText(sdf.format(myCalendar.getTime()));
                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        productReport.setOnTouchListener(touchListener);
        customerReport.setOnTouchListener(touchListener);
        qtyReport.setOnTouchListener(touchListener);
        amountReport.setOnTouchListener(touchListener);
        debit.setOnTouchListener(touchListener);
        credit.setOnTouchListener(touchListener);
        rebate.setOnTouchListener(touchListener);
        dateReport.setOnTouchListener(touchListener);
        notesReport.setOnTouchListener(touchListener);

    }

    private void saveReport(){

        String proReport = productReport.getText().toString().trim();
        String custmReport = customerReport.getText().toString().trim();
        String quntReport = qtyReport.getText().toString().trim();
        String amtReport = amountReport.getText().toString().trim();
        String creditReport = credit.getText().toString().trim();
        String rebateReport = rebate.getText().toString().trim();
        String debitReport = debit.getText().toString().trim();
        String datReport = dateReport.getText().toString().trim();
        String notReport = notesReport.getText().toString().trim();


        if (null == mCurrentReportUri && TextUtils.isEmpty(proReport) && TextUtils.isEmpty(custmReport)
                && TextUtils.isEmpty(quntReport) && TextUtils.isEmpty(amtReport)
                && TextUtils.isEmpty(creditReport) && TextUtils.isEmpty(rebateReport)
                && TextUtils.isEmpty(debitReport) && TextUtils.isEmpty(datReport)
                && TextUtils.isEmpty(notReport)){
            Toast.makeText(this, "por favor llene los campos", Toast.LENGTH_LONG).show();
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
        values.put(DocumentsEntry.PRODUCT, proReport);
        values.put(DocumentsEntry.CUSTOMER, custmReport);
        values.put(DocumentsEntry.QUANTITY, intQuntity);
        values.put(DocumentsEntry.AMOUNT, intAmount);
        values.put(DocumentsEntry.DEBIT, intcredit);
        values.put(DocumentsEntry.DEBIT, intrebate);
        values.put(DocumentsEntry.DEBIT, intdebit);
        values.put(DocumentsEntry.DATE, datReport);
        values.put(DocumentsEntry.NOTES, notReport);

        if (mCurrentReportUri == null) {
            Uri newUri = getContentResolver().insert(DocumentsEntry.CONTENT_URI_3, values);
            if (newUri == null) {
                Toast.makeText(this, "Error al guardar",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "guardado", Toast.LENGTH_LONG).show();
            }
        }else {
            int rowsAffected = getContentResolver().update(mCurrentReportUri, values,
                    null, null);
            if (rowsAffected == 0){
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_report, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCurrentReportUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_report);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_report:
                saveReport();
                finish();
                return true;
            case R.id.delete_report:
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the report hasn't changed, continue with handling back button press
        if (!mReportHasChanged) {
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
                DocumentsEntry._ID,
                DocumentsEntry.PRODUCT,
                DocumentsEntry.CUSTOMER,
                DocumentsEntry.DEBIT,
                DocumentsEntry.REBATE,
                DocumentsEntry.CREDIT,
                DocumentsEntry.QUANTITY,
                DocumentsEntry.AMOUNT,
                DocumentsEntry.DATE,
                DocumentsEntry.NOTES
        };
        return new CursorLoader(this,
                mCurrentReportUri,
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

            int namePro = cursor.getColumnIndex(DocumentsEntry.PRODUCT);
            int nameCstom = cursor.getColumnIndex(DocumentsEntry.CUSTOMER);
            int quant = cursor.getColumnIndex(DocumentsEntry.QUANTITY);
            int amount = cursor.getColumnIndex(DocumentsEntry.AMOUNT);
            int debitt = cursor.getColumnIndex(DocumentsEntry.DEBIT);
            int rebitt = cursor.getColumnIndex(DocumentsEntry.REBATE);
            int creditt = cursor.getColumnIndex(DocumentsEntry.CREDIT);
            int date = cursor.getColumnIndex(DocumentsEntry.DATE);
            int notes = cursor.getColumnIndex(DocumentsEntry.NOTES);

            String nameProString = cursor.getString(namePro);
            String nameCstomString = cursor.getString(nameCstom);
            int quantString = cursor.getInt(quant);
            int amountString = cursor.getInt(amount);
            int debitStr = cursor.getInt(debitt);
            int rebitString = cursor.getInt(rebitt);
            int creditString = cursor.getInt(creditt);
            String dateString = cursor.getString(date);
            int notesString = cursor.getInt(notes);

            productReport.setText(nameProString);
            customerReport.setText(nameCstomString);
            qtyReport.setText(Integer.toString(quantString));
            amountReport.setText(Integer.toString(amountString));
            debit.setText(Integer.toString(debitStr));
            rebate.setText(Integer.toString(rebitString));
            credit.setText(Integer.toString(creditString));
            dateReport.setText(dateString);
            notesReport.setText(Integer.toString(notesString));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productReport.setText("");
        customerReport.setText("");
        qtyReport.setText("");
        amountReport.setText("");
        debit.setText("");
        rebate.setText("");
        credit.setText("");
        dateReport.setText("");
        notesReport.setText("");
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

        builder.setMessage("desea eleminar?");
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
        if (mCurrentReportUri != null) {
            int deleteRow = getContentResolver().delete(mCurrentReportUri, null, null);

            if (deleteRow == 0) {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
            }
        }
        // Close the activity
        finish();
    }
}
