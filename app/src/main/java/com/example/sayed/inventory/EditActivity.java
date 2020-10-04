package com.example.sayed.inventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sayed.inventory.data.InventoryContract.InventoryEntry;
import java.io.FileNotFoundException;
import java.io.InputStream;
public class EditActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the pet data loader */
    private static final int EXISTING_PRODUCTION_LOADER = 0;
    private static int RESULT_LOAD_IMAGE = 1;
    TextView textView;
    private EditText mProduction;
    private EditText mQuantity;
    private EditText mPrice;
    private EditText mWeight;
    private EditText mDealer;
    private EditText mDescription;
    private ImageButton image_view;
    public String path;

    /**
     * Content URI for the existing Production (null if it's a new Production)
     */
    public Uri mCurrentProductionUri;

    /**
     * Boolean flag that keeps track of whether the pet has been edited (true) or not (false)
     */
    private boolean mProductionHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mProductionHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        textView = findViewById(R.id.info);
        mProduction = findViewById(R.id.production);
        mQuantity = findViewById(R.id.quantity);
        mPrice = findViewById(R.id.price);
        mWeight = findViewById(R.id.weight);
        mDealer = findViewById(R.id.dealer);
        mDescription = findViewById(R.id.description);
        image_view = findViewById(R.id.image_view);

        Intent intent = getIntent();
        mCurrentProductionUri = intent.getData();
        if (mCurrentProductionUri == null) {
            setTitle("Agregar un producto");
            textView.setText("Agregar Información Producto.");

            invalidateOptionsMenu();
        } else {
            setTitle("Editar producto");
            textView.setText("Edite Información sobre producto.");
            getLoaderManager().initLoader(EXISTING_PRODUCTION_LOADER, null, this);
        }
        mProduction.setOnTouchListener(touchListener);
        mQuantity.setOnTouchListener(touchListener);
        mPrice.setOnTouchListener(touchListener);
        mWeight.setOnTouchListener(touchListener);
        mDealer.setOnTouchListener(touchListener);
        mDescription.setOnTouchListener(touchListener);
        image_view.setOnTouchListener(touchListener);

    }
    public void imgGallery(View v){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);//100 هو ان نتيجة الطلب صورة وليس صوت او فديو
        startActivityForResult(intent, 100);// نوع البيانات المستقبل ممكن يكون صورة او صوت او فديو
    }
    //تستقبل نتيجة الانتدنت عند تحديد صورة من معرض الصور
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100){
            //ناخد البيانات علي شكل رابط
            assert data != null;
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream);
                image_view.setImageBitmap(decodeStream);
                image_view.setTag(selectedImageUri);
                path = image_view.getTag().toString();
            } catch (FileNotFoundException e) {
                Log.e("e", e.getMessage() );
            }
        }
    }
    private void saveProduction() {
        String production = mProduction.getText().toString().trim();
        String quantity = mQuantity.getText().toString().trim();
        String price = mPrice.getText().toString().trim();
        String weight = mWeight.getText().toString().trim();
        String dealer = mDealer.getText().toString().trim();
        String description = mDescription.getText().toString().trim();
        //في حال كانت الحقول كلها فارغة فلسنا بحاجة لانشاء منتج جديد ودا معناه ان المستخدم غير رايه
        if (null == mCurrentProductionUri && TextUtils.isEmpty(production) && TextUtils.isEmpty(quantity) &&
                  TextUtils.isEmpty(price) && TextUtils.isEmpty(weight)
                && TextUtils.isEmpty(dealer) && TextUtils.isEmpty(description) &&  TextUtils.isEmpty(path)) {

            Toast.makeText(this, "please fill the fields", Toast.LENGTH_LONG).show();
            return;
        }
        int intQuantity = 0;
        if (!TextUtils.isEmpty(quantity)) {
            intQuantity = Integer.parseInt(quantity);
        }
        int intPrice = 0;
        if (!TextUtils.isEmpty(price)) {
            intPrice = Integer.parseInt(price);
        }
        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int intWeight = 0;
        if (!TextUtils.isEmpty(weight)) {
            intWeight = Integer.parseInt(weight);
        }
      //String stringPath = "android.resource://com.example.sayed.inventory/" + R.drawable.preview;

        if (path == null){
        Uri imgPath = Uri.parse("android.resource://com.example.sayed.inventory/" + R.drawable.preview);
        path = imgPath.toString();

         }

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.PRODUCTION, production);
        values.put(InventoryEntry.QUANTITY, intQuantity);
        values.put(InventoryEntry.PRICE, intPrice);
        values.put(InventoryEntry.WEIGHT, intWeight);
        values.put(InventoryEntry.DEALER, dealer);
        values.put(InventoryEntry.DESCRIPTION, description);
        values.put(InventoryEntry.PHOTO, path);



        // Determine if this is a new or existing production by checking
        // if mCurrentProductionUri is null or not
        if (mCurrentProductionUri == null) {
            // This is a NEW production, so insert a new production into the provider,
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
            // If the new content URI is null, then there was an error with insertion.
            if (newUri == null) {
                Toast.makeText(this, "Error with save production",
                        Toast.LENGTH_LONG).show();
                // Otherwise, the insertion was successful and we can display a toast.
            } else {
                Toast.makeText(this, "saved production", Toast.LENGTH_LONG).show();
            }
        } else {
            //هنا معتاه ان المنتج موجود بس هيتم تحديثة اي ان ال uri موجود
            //لذلك سيتم تحدبث ال uri الموجود والقيم التي يحويها
            // وبالتالي سنحتاج الي تمرير mCurrentProductionUri
            // الذي مباشرة يحتوي علي القيم في حقولها ليتم التعديل عليها
            //لاحظ هذا الكود لمعرفة كيف تمرر نصوص الي حقول لتحريرها
            int rowsAffected = getContentResolver().update(mCurrentProductionUri, values,
                    null, null);
            //في حال فشل التحديث اي
            if (rowsAffected == 0) {
                Toast.makeText(this, "Error with update production", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "production updated successful", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ediror, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If this is a new Production, hide the "Delete" menu item.
        if (mCurrentProductionUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveProduction();
                finish();
                return true;
            case R.id.delete:
                //فى حال حذف منتج اظهار مربع حوار دبالوج
                // هل ترد حذف هذا الحيوان او لا وناخذ تحديد القيمة منه ونبني عليها كود جديد
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                // If the Production hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                //فى حال لم يحدث اي تغير فى الحقول وضغط علي زر الرجوع عادى مفيش حاجة
                //فى هذه الحالة ال boolean كما هو false
                if (!mProductionHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                //عدا ذلك اي هناك تغييرات لم ينم حفظها وضغط علي زر الرجوع
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //زر الرجوع ف النظام
    @Override
    public void onBackPressed() {
        // If the production hasn't changed, continue with handling back button press
        if (!mProductionHasChanged) {
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
                InventoryEntry._ID,
                InventoryEntry.PRODUCTION,
                InventoryEntry.QUANTITY,
                InventoryEntry.PRICE,
                InventoryEntry.WEIGHT,
                InventoryEntry.DEALER,
                InventoryEntry.DESCRIPTION,
                InventoryEntry.PHOTO
        };
        return new CursorLoader(this,
                mCurrentProductionUri,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //لترك المؤشر مبكرا ف حال كان فارغ او اقل من صف 1
        //اي في حال لا يوجد جداول اصلا او انتهاء عدد الصفوف ف قاعدة البيانات
        if (cursor == null || cursor.getCount() < 1){
            return;
        }
        if (cursor.moveToNext()){
            //نقل المؤشر الي الاعمدة للحصول علي قيمها ومن ثم تمريرها ف حقول الادخال اي سناخذ القيم لكل
            // حقل ليقوم المستخدم بالتعديل عليها وطل ذلك يحدث عند النقر علي عتصر فى القائمة
            int prdu = cursor.getColumnIndex(InventoryEntry.PRODUCTION);
            int quantity = cursor.getColumnIndex(InventoryEntry.QUANTITY);
            int price = cursor.getColumnIndex(InventoryEntry.PRICE);
            int weight = cursor.getColumnIndex(InventoryEntry.WEIGHT);
            int dealer = cursor.getColumnIndex(InventoryEntry.DEALER);
            int des = cursor.getColumnIndex(InventoryEntry.DESCRIPTION);
            int image = cursor.getColumnIndex(InventoryEntry.PHOTO);
            // Extract out the value from the Cursor for the given column index
            String pr = cursor.getString(prdu);
            int qunt = cursor.getInt(quantity);
            int pri = cursor.getInt(price);
            int wei = cursor.getInt(weight);
            String dea = cursor.getString(dealer);
            String desc = cursor.getString(des);
            String img = cursor.getString(image);

            Uri pathcur = Uri.parse(img);
            // Update the views on the screen with the values from the database
            mProduction.setText(pr);
            mQuantity.setText(Integer.toString(qunt));
            mPrice.setText(Integer.toString(pri));
            mWeight.setText(Integer.toString(wei));
            mDealer.setText(dea);
            mDescription.setText(desc);
            image_view.setImageURI(pathcur);

            image_view.setTag(pathcur);
            path = image_view.getTag().toString();
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        //بعد تهيئة المحمل احذف كل البيانات من حقول الادخال
        mProduction.setText("");
        mQuantity.setText("");
        mPrice.setText("");
        mWeight.setText("");
        mDealer.setText("");
        mDescription.setText("");
        image_view.setImageURI(null);
    }
    //فى حال قام المستخدم بتعديل احد الحقول وضغط علي زر الرجوع بدون قصد او قصد
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.message);
        //للخروج من النشاط وتجاهل التعديلات
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);

        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                //لاغلاق الديالوج والرجوع الي التعديلات ف النشاط
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
        // show Delete this production?
        builder.setMessage(R.string.delete_this_production);
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
        //بما انه يتم حذف منتج موجود معناه ان ال uri موجود
        if (mCurrentProductionUri != null) {
            int deleteRow = getContentResolver().delete(mCurrentProductionUri, null, null);

            if (deleteRow == 0) {
                Toast.makeText(this, "Error with delete production", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "production deleted", Toast.LENGTH_LONG).show();
            }
        }
        // Close the activity
        finish();
    }

}