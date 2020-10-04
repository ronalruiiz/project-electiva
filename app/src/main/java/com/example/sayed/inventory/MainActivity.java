package com.example.sayed.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sayed.inventory.data.ExportDatabaseCSVTask;
import com.example.sayed.inventory.data.ExportDatabaseCSVTask2;
import com.example.sayed.inventory.data.ExportDatabaseCSVTask3;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private LinearLayout addProduct;
    private LinearLayout editProduct;
    private LinearLayout listProducts;
    private LinearLayout customer;
    private LinearLayout excel;
    private LinearLayout reports;
    private LinearLayout callView;
    private LinearLayout smsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addProduct = findViewById(R.id.add_products);
        editProduct = findViewById(R.id.edit_product);
        listProducts = findViewById(R.id.list_goods2);
        excel = findViewById(R.id.excel);
        reports = findViewById(R.id.reports_re);
        //callView = findViewById(R.id.call);
        //smsView = findViewById(R.id.send_sms);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto:"));
                String [] to={""};
                i.putExtra(Intent.EXTRA_EMAIL,to);
                i.putExtra(Intent.EXTRA_SUBJECT,"This is subject");
                i.putExtra(Intent.EXTRA_TEXT,"This is body");
                i.setType("plain/text");
                startActivity(i);
            }
        });

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }


    public void addProduct(View view){
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }
    public void editProduct(View view){
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }
    public void listInventory(View view){
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }
    public void excel(View view){

        String[] colors = {"Clientes ", "Proveedores ", "Stocks"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lista de Reportes");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //first option clicked, do this...
                    ExportDatabaseCSVTask cs = new ExportDatabaseCSVTask(getApplicationContext());
                    cs.exportDataBaseIntoCSV();

                }else if(which == 1){
                    //second option clicked, do this...

                    ExportDatabaseCSVTask2 cs2 = new ExportDatabaseCSVTask2(getApplicationContext());
                    cs2.exportDataBaseIntoCSV();

                }else if(which == 2){

                    ExportDatabaseCSVTask3 cs3 = new ExportDatabaseCSVTask3(getApplicationContext());
                    cs3.exportDataBaseIntoCSV();
                }
            }
        });
        builder.show();

    }
    public void customersDebit(View view){
        Intent intent = new Intent(this, ReportsActivity.class);
        startActivity(intent);
    }
    public void suppliers(View view){
        Intent intent = new Intent(this, Suppliers.class);
        startActivity(intent);
    }
    public void stocks(View view){
        Intent intent = new Intent(this, Stocks.class);
        startActivity(intent);
    }
    public void Help(View view){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
    public void About(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Main_Menu) {

        } else if (id == R.id.list_goods) {
            Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.add_production) {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);

        } else if (id == R.id.edit_production) {
            Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.add_customer) {
            Intent intent = new Intent(MainActivity.this, EditCustomersActivity.class);
            startActivity(intent);

        } else if (id == R.id.add_report) {
            Intent intent = new Intent(MainActivity.this, EditReportActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_call) {

            //Intent intent = new Intent(MainActivity.this, CustomersActivity.class);
            //startActivity(intent);
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:"));
            startActivity(intent);
        } else if (id == R.id.nav_send_sms) {

            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            startActivity(sendIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
