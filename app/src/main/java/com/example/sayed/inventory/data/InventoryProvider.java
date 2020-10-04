package com.example.sayed.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.sayed.inventory.data.InventoryContract.InventoryEntry;
import com.example.sayed.inventory.data.InventoryContract.CustomersEntry;
import com.example.sayed.inventory.data.InventoryContract.DocumentsEntry;
import com.example.sayed.inventory.data.InventoryContract.SuppliersEntry;
import com.example.sayed.inventory.data.InventoryContract.StocksEntry;


public class InventoryProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the products table */
    private static final int INVENTORY = 100;

    /** URI matcher code for the content URI for a single production in the products table */
    private static final int INVENTORY_ID = 101;


       /** URI matcher code for the content URI for the customers table */
    private static final int CUSTOMERS = 200;

    /** URI matcher code for the content URI for a single customers in the customers table */
    private static final int CUSTOMERS_ID = 201;

    /** URI matcher code for the content URI for the documents table */
    private static final int DOCUMENTS = 300;

    /** URI matcher code for the content URI for a single documents in the documents table */
    private static final int DOCUMENTS_ID = 301;

    /** URI matcher code for the content URI for the SUPPLIERS table */
    private static final int SUPPLIERS = 400;

    /** URI matcher code for the content URI for a single SUPPLIERS in the SUPPLIERS table */
    private static final int SUPPLIERS_ID = 401;

    /** URI matcher code for the content URI for the STOCKS table */
    private static final int STOCKS = 500;

    /** URI matcher code for the content URI for a single STOCKS in the STOCKS table */
    private static final int STOCKS_ID = 501;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {

        //content://com.android.example.inventory/inventory/100
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, INVENTORY);
        //content://com.android.example.inventory/inventory/101
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY
                +"/#",  INVENTORY_ID);

        //content://com.android.example.inventory/customers/200
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_Customers, CUSTOMERS);
        //content://com.android.example.inventory/customers/201
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_Customers
                +"/#",  CUSTOMERS_ID);

        //content://com.android.example.inventory/documents/300
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_DOCUMENTS, DOCUMENTS);
        //content://com.android.example.inventory/documents/301
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_DOCUMENTS
                +"/#",  DOCUMENTS_ID);

        //content://com.android.example.inventory/suppliers/400
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_SUPPLIERS, SUPPLIERS);
        //content://com.android.example.inventory/suppliers/401
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_SUPPLIERS
                +"/#",  SUPPLIERS_ID);

        //content://com.android.example.inventory/stocks/500
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_STOCKS, STOCKS);
        //content://com.android.example.inventory/stocks/501
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_STOCKS
                +"/#",  STOCKS_ID);

    }
    private InventoryHelper inventoryHelper;
    @Override
    public boolean onCreate() {
        inventoryHelper = new InventoryHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = inventoryHelper.getReadableDatabase();
        Cursor cursor;
        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                cursor = database.query(InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryEntry.TABLE_NAME, null, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CUSTOMERS:
                cursor = database.query(CustomersEntry.TABLE_NAME_2, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case CUSTOMERS_ID:
                selection = CustomersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(CustomersEntry.TABLE_NAME_2, null, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case DOCUMENTS:
                cursor = database.query(DocumentsEntry.TABLE_NAME_3, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case DOCUMENTS_ID:
                selection = DocumentsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DocumentsEntry.TABLE_NAME_3, null, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case SUPPLIERS:
                cursor = database.query(SuppliersEntry.TABLE_NAME_4, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case SUPPLIERS_ID:
                selection = SuppliersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(SuppliersEntry.TABLE_NAME_4, null, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case STOCKS:
                cursor = database.query(StocksEntry.TABLE_NAME_5, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case STOCKS_ID:
                selection = StocksEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StocksEntry.TABLE_NAME_5, null, selection,
                        selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;

            case CUSTOMERS:
                return CustomersEntry.CONTENT_LIST_TYPE;
            case CUSTOMERS_ID:
                return CustomersEntry.CONTENT_ITEM_TYPE;

            case DOCUMENTS:
                return DocumentsEntry.CONTENT_LIST_TYPE;
            case DOCUMENTS_ID:
                return DocumentsEntry.CONTENT_ITEM_TYPE;

            case SUPPLIERS:
                return SuppliersEntry.CONTENT_LIST_TYPE;
            case SUPPLIERS_ID:
                return SuppliersEntry.CONTENT_ITEM_TYPE;

            case STOCKS:
                return StocksEntry.CONTENT_LIST_TYPE;
            case STOCKS_ID:
                return StocksEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return insertProduction(uri, values);
            case CUSTOMERS:
                return insertCustomer(uri, values);
            case DOCUMENTS:
                return insertDocuments(uri, values);
            case SUPPLIERS:
                return insertSuppliers(uri, values);
            case STOCKS:
                return insertStocks(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertProduction(Uri uri, ContentValues values) {

        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        Long id = database.insert(InventoryEntry.TABLE_NAME, null, values);
        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert row for "+uri);
        }
        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    private Uri insertCustomer(Uri uri, ContentValues values) {

        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        Long id = database.insert(CustomersEntry.TABLE_NAME_2, null, values);
        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert row for "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    private Uri insertDocuments(Uri uri, ContentValues values) {

        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        Long id = database.insert(DocumentsEntry.TABLE_NAME_3, null, values);
        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert row for "+uri);
        }
        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    private Uri insertSuppliers(Uri uri, ContentValues values) {

        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        Long id = database.insert(SuppliersEntry.TABLE_NAME_4, null, values);
        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert row for "+uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    private Uri insertStocks(Uri uri, ContentValues values) {

        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        Long id = database.insert(StocksEntry.TABLE_NAME_5, null, values);
        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert row for "+uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CUSTOMERS:
                rowsDeleted = database.delete(CustomersEntry.TABLE_NAME_2,
                        selection, selectionArgs);
                break;
            case CUSTOMERS_ID:
                selection = InventoryContract.CustomersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(CustomersEntry.TABLE_NAME_2, selection, selectionArgs);
                break;
            case DOCUMENTS:
                rowsDeleted = database.delete(DocumentsEntry.TABLE_NAME_3,
                        selection, selectionArgs);
                break;
            case DOCUMENTS_ID:
                selection = DocumentsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(DocumentsEntry.TABLE_NAME_3, selection, selectionArgs);
                break;
            case SUPPLIERS:
                rowsDeleted = database.delete(SuppliersEntry.TABLE_NAME_4,
                        selection, selectionArgs);
                break;
            case SUPPLIERS_ID:
                selection = SuppliersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(SuppliersEntry.TABLE_NAME_4, selection, selectionArgs);
                break;
            case STOCKS:
                rowsDeleted = database.delete(StocksEntry.TABLE_NAME_5,
                        selection, selectionArgs);
                break;
            case STOCKS_ID:
                selection = StocksEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(StocksEntry.TABLE_NAME_5, selection, selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return updateInventory(uri, values, selection, selectionArgs);
            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateInventory(uri, values, selection, selectionArgs);


            case CUSTOMERS:
                return updateCustomers(uri, values, selection, selectionArgs);

            case CUSTOMERS_ID:
                selection = CustomersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateCustomers(uri, values, selection, selectionArgs);


            case DOCUMENTS:
                return updateDocuments(uri, values, selection, selectionArgs);

            case DOCUMENTS_ID:
                selection = DocumentsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateDocuments(uri, values, selection, selectionArgs);

            case SUPPLIERS:
                return updateSuppliers(uri, values, selection, selectionArgs);

            case SUPPLIERS_ID:
                selection = SuppliersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateSuppliers(uri, values, selection, selectionArgs);

            case STOCKS:
                return updateStock(uri, values, selection, selectionArgs);

            case STOCKS_ID:
                selection = StocksEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateStock(uri, values, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateInventory(Uri uri, ContentValues values, String selection,
                                String[] selectionArgs) {
        if (values.containsKey(InventoryEntry.PRODUCTION)){
            String production = values.getAsString(InventoryEntry.PRODUCTION);
            if (production == null){
                throw new IllegalArgumentException("production requires a name");
            }
        }

        if (values.containsKey(InventoryEntry.PRICE)){
            Integer price = values.getAsInteger(InventoryEntry.PRICE);
            if (price == null){
                throw new IllegalArgumentException("price require");
            }
        }

        if (values.containsKey(InventoryEntry.QUANTITY)){
            Integer weight = values.getAsInteger(InventoryEntry.QUANTITY);
            if (weight == null){
                throw new IllegalArgumentException("weight require");
            }
        }

        if (values.containsKey(InventoryEntry.WEIGHT)){
            Integer weight = values.getAsInteger(InventoryEntry.WEIGHT);
            if (weight == null){
                throw new IllegalArgumentException("weight require");
            }
        }

        if (values.containsKey(InventoryEntry.DEALER)){
            String dealer = values.getAsString(InventoryEntry.DEALER);
            if (dealer == null){
                throw new IllegalArgumentException("dealer requires a name");
            }
        }

        if (values.containsKey(InventoryEntry.DESCRIPTION)){
            String description = values.getAsString(InventoryEntry.DESCRIPTION);
            if (description == null){
                throw new IllegalArgumentException("description require");
            }
        }

        if (values.containsKey(InventoryEntry.PHOTO)){
            String image = values.getAsString(InventoryEntry.PHOTO);
            if (image == null){
                throw new IllegalArgumentException("image requires");
            }
        }

        if (values.size() == 0){
            return 0;
        }
        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        int rowUpdated = database.update(InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }


    private int updateCustomers(Uri uri, ContentValues values, String selection,
                                String[] selectionArgs) {

        if (values.containsKey(CustomersEntry.CUSTOMER_NAME)){
            String customer = values.getAsString(InventoryContract.CustomersEntry.CUSTOMER_NAME);
            if (customer == null){
                throw new IllegalArgumentException("CUSTOMER NAME requires a name");
            }
        }

        if (values.containsKey(CustomersEntry.CUSTOMER_ADDRESS)){
            String address = values.getAsString(CustomersEntry.CUSTOMER_ADDRESS);
            if (address == null){
                throw new IllegalArgumentException("CUSTOMER ADDRESS require");
            }
        }

        if (values.containsKey(CustomersEntry.CUSTOMER_PHONE)){
            String phone = values.getAsString(CustomersEntry.CUSTOMER_PHONE);
            if (phone == null){
                throw new IllegalArgumentException("CUSTOMER_PHONE require");
            }
        }
        //لو كان تم تمثيل عمود باسم CUSTOMER_EMAIL راجع علي قيمتة فارغة ولا لا
        if (values.containsKey(CustomersEntry.CUSTOMER_EMAIL)){
            String email = values.getAsString(CustomersEntry.CUSTOMER_EMAIL);
            if (email == null){
                throw new IllegalArgumentException("weight require");
            }
        }

        if (values.containsKey(CustomersEntry.CUSTOMER_NOTES)){
            String notes = values.getAsString(CustomersEntry.CUSTOMER_NOTES);
            if (notes == null){
                throw new IllegalArgumentException("dealer requires a name");
            }
        }
        if (values.size() == 0){
            return 0;
        }
        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        int rowUpdated = database.update(CustomersEntry.TABLE_NAME_2, values, selection, selectionArgs);

        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }


    private int updateDocuments(Uri uri, ContentValues values, String selection,
                                String[] selectionArgs) {

        if (values.containsKey(DocumentsEntry.CUSTOMER)){
            String customer_R = values.getAsString(DocumentsEntry.CUSTOMER);
            if (customer_R == null){
                throw new IllegalArgumentException(" NAME CUSTOMER requires a name");
            }
        }

        if (values.containsKey(DocumentsEntry.PRODUCT)){
            String prodect_R = values.getAsString(DocumentsEntry.PRODUCT);
            if (prodect_R == null){
                throw new IllegalArgumentException("NAME PRODUCT require");
            }
        }

        if (values.containsKey(DocumentsEntry.QUANTITY)){
            Integer quantity = values.getAsInteger(DocumentsEntry.QUANTITY);
            if (quantity == null){
                throw new IllegalArgumentException("QUANTITY require");
            }
        }

        if (values.containsKey(DocumentsEntry.DEBIT)){
            Integer debit = values.getAsInteger(DocumentsEntry.DEBIT);
            if (debit == null){
                throw new IllegalArgumentException("debit require");
            }
        }

        if (values.containsKey(DocumentsEntry.CREDIT)){
            Integer credit = values.getAsInteger(DocumentsEntry.CREDIT);
            if (credit == null){
                throw new IllegalArgumentException("credit require");
            }
        }

        if (values.containsKey(DocumentsEntry.REBATE)){
            Integer rebate = values.getAsInteger(DocumentsEntry.REBATE);
            if (rebate == null){
                throw new IllegalArgumentException("rebate require");
            }
        }

        if (values.containsKey(DocumentsEntry.AMOUNT)){
            Integer amount = values.getAsInteger(DocumentsEntry.AMOUNT);
            if (amount == null){
                throw new IllegalArgumentException("amount requires a name");
            }
        }

        if (values.containsKey(DocumentsEntry.DATE)){
            String date = values.getAsString(DocumentsEntry.DATE);
            if (date == null){
                throw new IllegalArgumentException("DATE requires a name");
            }
        }

        if (values.containsKey(DocumentsEntry.NOTES)){
            String notes_R = values.getAsString(DocumentsEntry.NOTES);
            if (notes_R == null){
                throw new IllegalArgumentException("NOTES requires a name");
            }
        }
        if (values.size() == 0){
            return 0;
        }
        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        int rowUpdated = database.update(DocumentsEntry.TABLE_NAME_3, values, selection, selectionArgs);

        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }

    private int updateSuppliers(Uri uri, ContentValues values, String selection,
                                String[] selectionArgs) {

        if (values.containsKey(SuppliersEntry.SUPPLIER_NAME)){
            String suppliers_R = values.getAsString(SuppliersEntry.PRODUCT_NAME);
            if (suppliers_R == null){
                throw new IllegalArgumentException(" NAME Suppliers requires a name");
            }
        }

        if (values.containsKey(SuppliersEntry.PRODUCT_NAME)){
            String prodect_R = values.getAsString(SuppliersEntry.PRODUCT_NAME);
            if (prodect_R == null){
                throw new IllegalArgumentException("NAME PRODUCT require");
            }
        }

        if (values.containsKey(SuppliersEntry.NUMBER_UNITS)){
            Integer numUnits= values.getAsInteger(SuppliersEntry.NUMBER_UNITS);
            if (numUnits == null){
                throw new IllegalArgumentException("numUnits require");
            }
        }

        if (values.containsKey(SuppliersEntry.DEBIT)){
            Integer debit = values.getAsInteger(SuppliersEntry.DEBIT);
            if (debit == null){
                throw new IllegalArgumentException("debit require");
            }
        }

        if (values.containsKey(SuppliersEntry.CREDIT)){
            Integer credit = values.getAsInteger(SuppliersEntry.CREDIT);
            if (credit == null){
                throw new IllegalArgumentException("credit require");
            }
        }

        if (values.containsKey(SuppliersEntry.REBATE)){
            Integer rebate = values.getAsInteger(SuppliersEntry.REBATE);
            if (rebate == null){
                throw new IllegalArgumentException("rebate require");
            }
        }

        if (values.containsKey(SuppliersEntry.TOTAL_BALANCE)){
            Integer total = values.getAsInteger(SuppliersEntry.TOTAL_BALANCE);
            if (total == null){
                throw new IllegalArgumentException("total requires a name");
            }
        }

        if (values.containsKey(SuppliersEntry.DATE)){
            String date = values.getAsString(SuppliersEntry.DATE);
            if (date == null){
                throw new IllegalArgumentException("DATE requires a name");
            }
        }

        if (values.containsKey(SuppliersEntry.NOTES)){
            String notes_R = values.getAsString(SuppliersEntry.NOTES);
            if (notes_R == null){
                throw new IllegalArgumentException("NOTES requires a name");
            }
        }
        if (values.size() == 0){
            return 0;
        }
        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        int rowUpdated = database.update(SuppliersEntry.TABLE_NAME_4, values, selection, selectionArgs);

        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }

    private int updateStock(Uri uri, ContentValues values, String selection,
                                String[] selectionArgs) {

        if (values.containsKey(StocksEntry.STOCK_NAME)){
            String stockName = values.getAsString(StocksEntry.STOCK_NAME);
            if (stockName == null){
                throw new IllegalArgumentException(" NAME Stock requires a name");
            }
        }

        if (values.containsKey(StocksEntry.PRODUCT_NAME)){
            String prodect_R = values.getAsString(StocksEntry.PRODUCT_NAME);
            if (prodect_R == null){
                throw new IllegalArgumentException("NAME PRODUCT require");
            }
        }

        if (values.containsKey(StocksEntry.QUANTITY)){
            Integer quantity = values.getAsInteger(StocksEntry.QUANTITY);
            if (quantity == null){
                throw new IllegalArgumentException("QUANTITY require");
            }
        }

        if (values.containsKey(StocksEntry.UNIT_PRICE)){
            Integer unitPrice = values.getAsInteger(StocksEntry.UNIT_PRICE);
            if (unitPrice == null){
                throw new IllegalArgumentException("unit Price require");
            }
        }

        if (values.containsKey(StocksEntry.SELLING_PRICE)){
            Integer sellingPrice = values.getAsInteger(StocksEntry.SELLING_PRICE);
            if (sellingPrice == null){
                throw new IllegalArgumentException("sellingPrice require");
            }
        }

        if (values.containsKey(StocksEntry.TOTAL)){
            Integer total = values.getAsInteger(StocksEntry.TOTAL);
            if (total == null){
                throw new IllegalArgumentException("total require");
            }
        }

        if (values.size() == 0){
            return 0;
        }
        SQLiteDatabase database = inventoryHelper.getWritableDatabase();
        int rowUpdated = database.update(StocksEntry.TABLE_NAME_5, values, selection, selectionArgs);

        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }
}
