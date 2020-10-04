package com.example.sayed.inventory.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;

public class ExportDatabaseCSVTask3 {

    Context context;
    public ExportDatabaseCSVTask3(Context context) {
        this.context=context;
    }
    public void exportDataBaseIntoCSV(){
        //here CredentialDb is my database. you can create your db object.
        InventoryHelper inventoryHelper = new InventoryHelper(context);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");

        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "stocks_reports.csv");

        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            //here create a method ,and return SQLiteDatabaseObject.getReadableDatabase();
            SQLiteDatabase sql_db = inventoryHelper.getReadableDatabase();
            Cursor curCSV = sql_db.rawQuery("SELECT * FROM "
                    +InventoryContract.StocksEntry.TABLE_NAME_5,null);
            csvWrite.writeNext(curCSV.getColumnNames());

            while(curCSV.moveToNext())
            {
                //Which column you want to export you can add over here...
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),curCSV.getString(4)
                        ,curCSV.getString(5),curCSV.getString(6)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Error:", sqlEx.getMessage(), sqlEx);
        }

    }
}
