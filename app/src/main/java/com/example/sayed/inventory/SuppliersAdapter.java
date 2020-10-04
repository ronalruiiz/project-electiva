package com.example.sayed.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sayed.inventory.data.InventoryContract;

public class SuppliersAdapter extends CursorAdapter {

    private TextView nameCustmView;
    private TextView nameProView;
    private TextView crebitView;
    private TextView rebateView;
    private TextView amountView;
    private TextView date;

    public SuppliersAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_suppliers, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        nameCustmView = view.findViewById(R.id.nameC_ss);
        nameProView = view.findViewById(R.id.nameP_ss);
        crebitView = view.findViewById(R.id.credit_ss);
        rebateView = view.findViewById(R.id.rebate_ss);
        amountView = view.findViewById(R.id.total_ss);
        date = view.findViewById(R.id.date_ss);


        int nameCstmr = cursor.getColumnIndex(InventoryContract.SuppliersEntry.SUPPLIER_NAME);
        int namePro = cursor.getColumnIndex(InventoryContract.SuppliersEntry.PRODUCT_NAME);
        int de = cursor.getColumnIndex(InventoryContract.SuppliersEntry.CREDIT);
        int re = cursor.getColumnIndex(InventoryContract.SuppliersEntry.REBATE);
        int am = cursor.getColumnIndex(InventoryContract.SuppliersEntry.TOTAL_BALANCE);
        int da = cursor.getColumnIndex(InventoryContract.SuppliersEntry.DATE);

        String nameCustomer = cursor.getString(nameCstmr);
        String nameProduct = cursor.getString(namePro);
        String dee = cursor.getString(de);
        String ree = cursor.getString(re);
        String amount = cursor.getString(am);
        String dateSup = cursor.getString(da);

        nameCustmView.setText(nameCustomer);
        nameProView.setText(nameProduct);
        crebitView.setText(dee);
        rebateView.setText(ree);
        amountView.setText(amount);
        date.setText(dateSup);


    }
}
