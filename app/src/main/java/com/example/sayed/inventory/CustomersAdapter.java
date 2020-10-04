package com.example.sayed.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sayed.inventory.data.InventoryContract.CustomersEntry;

public class CustomersAdapter extends CursorAdapter {

    TextView nameView;
    TextView phoneView;
    public CustomersAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_customers, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        nameView = view.findViewById(R.id.name_customer);
        phoneView = view.findViewById(R.id.phone_customer);

        int nameColumnIndex = cursor.getColumnIndex(CustomersEntry.CUSTOMER_NAME);
        int phoneColumnIndex = cursor.getColumnIndex(CustomersEntry.CUSTOMER_PHONE);

        String name = cursor.getString(nameColumnIndex);
        String phone = cursor.getString(phoneColumnIndex);

        nameView.setText(name);
        phoneView.setText(phone);





    }
}
