package com.archi.tithetracker;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ListView;
import android.widget.Toast;

public class BackgroundTask extends AsyncTask<String,Product,String> {

    Context ctx;
    ProductAdaptor productAdaptor;
    Activity activity;
    ListView listView;
    BackgroundTask(Context ctx) {
        this.ctx =ctx;
        activity = (Activity)ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... params) {

        String method = params[0];
        DbOperations dbOperations = new DbOperations(ctx);
        if(method.equals("add_info")) {

            String input = params[1];
            String date = params[2];
            SQLiteDatabase db = dbOperations.getWritableDatabase();
            dbOperations.addInformation(db, input, date);
        } if(method.equals("get_info") || method.equals("add_info")) {
            listView = (ListView) activity.findViewById(R.id.display_listview);
            SQLiteDatabase db = dbOperations.getReadableDatabase();
            Cursor cursor = dbOperations.getInfromation(db);
            productAdaptor = new ProductAdaptor(ctx, R.layout.display_product_row);
            String input, date;
            while(cursor.moveToNext()) {
                input = cursor.getString(cursor.getColumnIndex(PC.PE.INPUT));
                date = cursor.getString(cursor.getColumnIndex(PC.PE.DATE));
                Product product = new Product(input, date);
                publishProgress(product);
            }
            return "get_info";
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onProgressUpdate(Product... values) {
        productAdaptor.add(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("get_info"))
            listView.setAdapter(productAdaptor);
        else
            Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
    }
}
