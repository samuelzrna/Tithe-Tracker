package com.archi.tithetracker;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



@RequiresApi(api = Build.VERSION_CODES.N)
public class ProductAdaptor extends ArrayAdapter {

    public static DecimalFormat df2 = new DecimalFormat("#.###");

    List list = new ArrayList();
    ArrayList<Integer> idHolder = new ArrayList();

    public ProductAdaptor(Context context, int resource) {
        super(context, resource);
    }

    DbOperations myDb;

    public void add(Product object) {
        list.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final ProductHolder productHolder;

        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_product_row, parent, false);
            productHolder = new ProductHolder();
            productHolder.tx_input = (TextView) row.findViewById(R.id.t_input);
            productHolder.tx_date = (TextView) row.findViewById(R.id.t_date);
            productHolder.deleteRowButton = (Button) row.findViewById(R.id.btn_delete_row);
            row.setTag(productHolder);
        }
        else {
            productHolder = (ProductHolder) row.getTag();
        }
        Product product = (Product)getItem(position);
        productHolder.tx_date.setText(product.getDate());
        productHolder.tx_input.setText("$" + df2.format(Integer.parseInt(product.getInput())));
        productHolder.deleteRowButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                myDb = new DbOperations(getContext());
                Cursor res = myDb.getAllData();
                list.remove(position);
                while(res.moveToNext()) {
                    idHolder.add(Integer.parseInt(res.getString(0)));
                }
                myDb.deleteData(Integer.toString(idHolder.get(position)));
                res.close();
                BackgroundTask backgroundTask = new BackgroundTask(getContext());
                backgroundTask.execute("get_info");
            }
        });
        return row;

    }

    static class ProductHolder {
        TextView tx_input, tx_date;
        Button deleteRowButton;
    }
}