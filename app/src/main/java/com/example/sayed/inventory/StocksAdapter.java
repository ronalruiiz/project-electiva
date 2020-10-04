package com.example.sayed.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sayed.inventory.data.InventoryContract.StocksEntry;

public class StocksAdapter extends CursorAdapter {

    private TextView Stock;
    private TextView ProdStock;
    private TextView quantITY;
    private TextView parching;
    private TextView selling;
    private TextView total;

    public StocksAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_stocks, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Stock= view.findViewById(R.id.nameC_stock);
        ProdStock= view.findViewById(R.id.nameP_stock);
        quantITY = view.findViewById(R.id.qunqun_stock);
        parching = view.findViewById(R.id.parch_stock);
        selling = view.findViewById(R.id.sell_stock);
        total = view.findViewById(R.id.total_stock);


        int nameCstmr = cursor.getColumnIndex(StocksEntry.STOCK_NAME);
        int namePro = cursor.getColumnIndex(StocksEntry.PRODUCT_NAME);
        int de = cursor.getColumnIndex(StocksEntry.QUANTITY);
        int re = cursor.getColumnIndex(StocksEntry.UNIT_PRICE);
        int am = cursor.getColumnIndex(StocksEntry.SELLING_PRICE);
        int da = cursor.getColumnIndex(StocksEntry.TOTAL);

        String nameCustomer = cursor.getString(nameCstmr);
        String nameProduct = cursor.getString(namePro);
        String dee = cursor.getString(de);
        String ree = cursor.getString(re);
        String amount = cursor.getString(am);
        String dateSup = cursor.getString(da);

        Stock.setText(nameCustomer);
        ProdStock.setText(nameProduct);
        quantITY.setText(dee);
        parching.setText(ree);
        selling.setText(amount);
        total.setText(dateSup);

    }
}
