package com.example.sayed.inventory;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sayed.inventory.data.InventoryContract.CustomersEntry;

public class EditCustomersActivity extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor>{

    public Uri mCurrentCustomerUri;

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText noteEditText;
    private Button call;
    static public final int CONTACT = 0;


    /**
     * Boolean flag that keeps track of whether the Customer has been edited (true) or not (false)
     */
    private boolean mCustomerHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mCustomerHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customers);

        nameEditText = findViewById(R.id.name_client);
        addressEditText = findViewById(R.id.address_client);
        phoneEditText = findViewById(R.id.phone_client);
        emailEditText = findViewById(R.id.email_client);
        noteEditText = findViewById(R.id.notes);
        call = findViewById(R.id.btn_call);

        Intent intent = getIntent();
        mCurrentCustomerUri = intent.getData();
        if (mCurrentCustomerUri == null) {
            setTitle("Agregar Cliente");
            invalidateOptionsMenu();
        } else {
            setTitle("Editar Cliente");
            getLoaderManager().initLoader(0, null, this);
            invalidateOptionsMenu();
        }
        nameEditText.setOnTouchListener(touchListener);
        addressEditText.setOnTouchListener(touchListener);
        phoneEditText.setOnTouchListener(touchListener);
        emailEditText.setOnTouchListener(touchListener);
        noteEditText.setOnTouchListener(touchListener);

    }
    public void callCustomer(View view){

        String call = phoneEditText.getText().toString().trim();
        if (call == null){
            return;
        }else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
            startActivity(intent);
        }
    }
    private void saveCustomer(){

        String nameCustmr = nameEditText.getText().toString().trim();
        String addressCustmr = addressEditText.getText().toString().trim();
        String phoneCustmr = phoneEditText.getText().toString().trim();
        String emailCustmr = emailEditText.getText().toString().trim();
        String notesCustmr = noteEditText.getText().toString().trim();

        if (null == mCurrentCustomerUri && TextUtils.isEmpty(nameCustmr) && TextUtils.isEmpty(addressCustmr)
                && TextUtils.isEmpty(phoneCustmr) && TextUtils.isEmpty(emailCustmr)
                && TextUtils.isEmpty(notesCustmr)){
            Toast.makeText(this, "please fill the fields", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(CustomersEntry.CUSTOMER_NAME, nameCustmr);
        values.put(CustomersEntry.CUSTOMER_ADDRESS, addressCustmr);
        values.put(CustomersEntry.CUSTOMER_PHONE, phoneCustmr);
        values.put(CustomersEntry.CUSTOMER_EMAIL, emailCustmr);
        values.put(CustomersEntry.CUSTOMER_NOTES, notesCustmr);

        if (mCurrentCustomerUri == null) {
            Uri newUri = getContentResolver().insert(CustomersEntry.CONTENT_URI_2, values);
            if (newUri == null) {
                Toast.makeText(this, "Error with save customer",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "saved customer", Toast.LENGTH_LONG).show();
            }
        }else {
            int rowsAffected = getContentResolver().update(mCurrentCustomerUri, values,
                    null, null);
            if (rowsAffected == 0){
                Toast.makeText(this, "Error with update customer", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "customer updated successful", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ediror_customer, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If this is a new customer, hide the "Delete" menu item.
        if (mCurrentCustomerUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_customer);
            menuItem.setVisible(false);
        } if (!(mCurrentCustomerUri == null)) {
            MenuItem menuItem = menu.findItem(R.id.import_from_device);
            menuItem.setVisible(false);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_from_device:
                importFromDevice();
                return true;
            case R.id.save_customer:
                saveCustomer();
                finish();
                return true;
            case R.id.delete_customer:
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void importFromDevice(){

        //Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/people"));
        //startActivityForResult(intent, CONTACT);

        //Intent intent = new Intent(Intent.ACTION_PICK);
        //intent.setData(ContactsContract.Contacts.CONTENT_URI);
        //startActivityForResult(intent, CONTACT);

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        // filter the contacts with phone number only
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, CONTACT);
        //
        //Intent intent = new Intent(Intent.ACTION_PICK, Contacts.People.CONTENT_URI);
        //startActivityForResult(intent, CONTACT);
    }

    public void onActivityResult (int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case (CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = intent.getData();
                    //String[] projection = {//ContactsContract.CommonDataKinds.Phone.NUMBER,
                            //ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
                    //Cursor c =  managedQuery(contactData, null, null, null, null);
                    assert contactData != null;
                    Cursor c = getContentResolver().query(contactData, null,null,null, null);
                    startManagingCursor(c);
                    assert c != null;
                    if (c.moveToNext()) {
                        //String name = c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME));
                        //String number = c.getString(c.getColumnIndexOrThrow(Contacts.People.NUMBER));
                        //String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        //String name = c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                        //String number = c.getString(c.getColumnIndex(ContactsContract.Data.N));
                        //String number = c.getString(c.getColumnIndex(Contacts.Phones.NUMBER));
                        //String number = c.getString(c.getColumnIndexOrThrow(Contacts.People.NUMBER));
                        String name=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        nameEditText.setText(name);
                        phoneEditText.setText(number);
                    }
                }
                break;
        }

        /*
        if (resultCode != Activity.RESULT_OK || requestCode != CONTACT) return;
        Uri uri = intent.getData();
       Cursor c =  getContentResolver().query(uri, null, null, null, null);
       // Cursor c = managedQuery(intent.getData(), null, null, null, null);
        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndexOrThrow(Contacts.Phones.DISPLAY_NAME));
            //String number = c.getString(c.getColumnIndexOrThrow(Contacts.Phones.NUMBER));
            String number =c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            nameEditText.setText(name);
            phoneEditText.setText(number);
            c.close();
        }*/
    }
    /*
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (0) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                     Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds
                    .Phone.CONTENT_URI, null,null,null, null);
                    assert contactData != null;
                    //Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    assert c != null;
                    if (c.moveToNext()) {
                        //String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        //String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String name =c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number =c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        nameEditText.setText(name);
                       phoneEditText.setText(number);

                    }
                    c.close();
                }
                break;
        }
    }*/

    @Override
    public void onBackPressed() {
        // If the customer hasn't changed, continue with handling back button press
        if (!mCustomerHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
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
                CustomersEntry._ID,
                CustomersEntry.CUSTOMER_NAME,
                CustomersEntry.CUSTOMER_ADDRESS,
                CustomersEntry.CUSTOMER_PHONE,
                CustomersEntry.CUSTOMER_EMAIL,
                CustomersEntry.CUSTOMER_NOTES
        };
        return new CursorLoader(this,
                mCurrentCustomerUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1){
            return;
        }
        if (cursor.moveToNext()){
            int name = cursor.getColumnIndex(CustomersEntry.CUSTOMER_NAME);
            int address = cursor.getColumnIndex(CustomersEntry.CUSTOMER_ADDRESS);
            int phone = cursor.getColumnIndex(CustomersEntry.CUSTOMER_PHONE);
            int email = cursor.getColumnIndex(CustomersEntry.CUSTOMER_EMAIL);
            int notes = cursor.getColumnIndex(CustomersEntry.CUSTOMER_NOTES);
            // Extract out the value from the Cursor for the given column index
            String nameStr = cursor.getString(name);
            String addressStr = cursor.getString(address);
            String phoneStr = cursor.getString(phone);
            String emailStr = cursor.getString(email);
            String notesStr = cursor.getString(notes);

            nameEditText.setText(nameStr);
            addressEditText.setText(addressStr);
            phoneEditText.setText(phoneStr);
            emailEditText.setText(emailStr);
            noteEditText.setText(notesStr);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEditText.setText("");
        addressEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
        noteEditText.setText("");

    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_customer);
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteProduction() {
        if (mCurrentCustomerUri != null) {
            int deleteRow = getContentResolver().delete(mCurrentCustomerUri, null, null);

            if (deleteRow == 0) {
                Toast.makeText(this, "Error with delete customer", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "customer deleted", Toast.LENGTH_LONG).show();
            }
        }
        // Close the activity
        finish();
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
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

}
