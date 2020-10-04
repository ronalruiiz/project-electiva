package com.example.sayed.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sayed.inventory.data.InventoryContract.InventoryEntry;
import com.example.sayed.inventory.data.InventoryContract.DocumentsEntry;
import com.example.sayed.inventory.data.InventoryContract.CustomersEntry;
import com.example.sayed.inventory.data.InventoryContract.SuppliersEntry;
import com.example.sayed.inventory.data.InventoryContract.StocksEntry;

public class InventoryHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryHelper.class.getSimpleName();

    public final static String DATABASE_NAME = "stockroom.db";

    public final static int DATABASE_VERSION = 1;

    public InventoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE <table_name> (<column_name_1> <data_type_1>,
        //<column_name_2> <data_type_2>,.....)
        String SQL_create_INVENTORY_TABLE =  " CREATE TABLE " + InventoryEntry.TABLE_NAME + " ( "
                +InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +InventoryEntry.PRODUCTION + " TEXT NOT NULL, "
                +InventoryEntry.QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                +InventoryEntry.WEIGHT + " INTEGER NOT NULL DEFAULT 0, "
                +InventoryEntry.PRICE + " INTEGER NOT NULL DEFAULT 0, "
                +InventoryEntry.DEALER + " TEXT NOT NULL, "
                +InventoryEntry.DESCRIPTION + " TEXT NOT NULL, "   //, "
                +InventoryEntry.PHOTO + " TEXT );";
        db.execSQL(SQL_create_INVENTORY_TABLE);

        String SQL_create_CUSTOMER_TABLE_2 =  " CREATE TABLE " + CustomersEntry.TABLE_NAME_2 + " ( "
                +CustomersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +CustomersEntry.CUSTOMER_NAME + " TEXT NOT NULL, "
                +CustomersEntry.CUSTOMER_ADDRESS + " TEXT NOT NULL, "
                +CustomersEntry.CUSTOMER_PHONE + " TEXT NOT NULL, "
                +CustomersEntry.CUSTOMER_EMAIL + "  TEXT NOT NULL, "
                +CustomersEntry.CUSTOMER_NOTES + " TEXT );";
        db.execSQL(SQL_create_CUSTOMER_TABLE_2);

        String SQL_create_REPORTS_TABLE_3 =  " CREATE TABLE " + InventoryContract.DocumentsEntry.TABLE_NAME_3 + " ( "
                +DocumentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DocumentsEntry.CUSTOMER + " TEXT NOT NULL, "
                +DocumentsEntry.PRODUCT + " TEXT NOT NULL, "
                +DocumentsEntry.QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                +DocumentsEntry.DEBIT + " INTEGER NOT NULL DEFAULT 0, "
                +DocumentsEntry.CREDIT + " INTEGER NOT NULL DEFAULT 0, "
                +DocumentsEntry.REBATE + " INTEGER NOT NULL DEFAULT 0, "
                +DocumentsEntry.AMOUNT + "  INTEGER NOT NULL DEFAULT 0, "
                +DocumentsEntry.DATE + "  TEXT, "
                +DocumentsEntry.NOTES + " TEXT );";
        db.execSQL(SQL_create_REPORTS_TABLE_3);

        String SQL_create_SUPPLIER_TABLE_4 =  " CREATE TABLE " + SuppliersEntry.TABLE_NAME_4 + " ( "
                +DocumentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +SuppliersEntry.SUPPLIER_NAME + " TEXT NOT NULL, "
                +SuppliersEntry.PRODUCT_NAME + " TEXT NOT NULL, "
                +SuppliersEntry.NUMBER_UNITS + " INTEGER NOT NULL DEFAULT 0, "
                +SuppliersEntry.DEBIT + " INTEGER NOT NULL DEFAULT 0, "
                +SuppliersEntry.CREDIT + " INTEGER NOT NULL DEFAULT 0, "
                +SuppliersEntry.REBATE + " INTEGER NOT NULL DEFAULT 0, "
                +SuppliersEntry.TOTAL_BALANCE + "  INTEGER NOT NULL DEFAULT 0, "
                +SuppliersEntry.DATE + "  TEXT, "
                +SuppliersEntry.NOTES + " TEXT );";
        db.execSQL(SQL_create_SUPPLIER_TABLE_4);

        String SQL_create_STOCKS_TABLE_5 =  " CREATE TABLE " + StocksEntry.TABLE_NAME_5 + " ( "
                +StocksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +StocksEntry.STOCK_NAME + " TEXT NOT NULL, "
                +StocksEntry.PRODUCT_NAME + " TEXT NOT NULL, "
                +StocksEntry.QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                +StocksEntry.UNIT_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                +StocksEntry.SELLING_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                +StocksEntry.TOTAL + "  INTEGER NOT NULL DEFAULT 0 );";
        db.execSQL(SQL_create_STOCKS_TABLE_5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
