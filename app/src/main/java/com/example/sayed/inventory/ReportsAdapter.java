package com.example.sayed.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sayed.inventory.data.InventoryContract;

public class ReportsAdapter extends CustomersAdapter {

    private TextView nameCustmView;
    private TextView nameProView;
    private TextView debitView;
    private TextView rebateView;
    private TextView amountView;

    public ReportsAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.edit, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        nameCustmView = view.findViewById(R.id.nameC_rp);
        nameProView = view.findViewById(R.id.nameP_rp);
        debitView = view.findViewById(R.id.debit_rp);
        rebateView = view.findViewById(R.id.rebate_rp);
        amountView = view.findViewById(R.id.Amnt_rp);


        int nameCstmr = cursor.getColumnIndex(InventoryContract.DocumentsEntry.CUSTOMER);
        int namePro = cursor.getColumnIndex(InventoryContract.DocumentsEntry.PRODUCT);
        int de = cursor.getColumnIndex(InventoryContract.DocumentsEntry.DEBIT);
        int re = cursor.getColumnIndex(InventoryContract.DocumentsEntry.REBATE);
        int am = cursor.getColumnIndex(InventoryContract.DocumentsEntry.AMOUNT);

        String nameCustomer = cursor.getString(nameCstmr);
        String nameProduct = cursor.getString(namePro);
        String dee = cursor.getString(de);
        String ree = cursor.getString(re);
        String amount = cursor.getString(am);

        nameCustmView.setText(nameCustomer);
        nameProView.setText(nameProduct);
        debitView.setText(dee);
        rebateView.setText(ree);
        amountView.setText(amount);


    }
}
