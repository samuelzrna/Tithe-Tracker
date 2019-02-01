package com.archi.tithetracker;

import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    public static DecimalFormat df2 = new DecimalFormat("#.##");
    EditText etInput;
    Button btnClear, btnTithe, btnDelete, btnDeleteRow, btnToggleView;
    String input, date;
    int toggle = 1;
    DbOperations myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DbOperations(this);
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute("get_info");
        getTenPercent();

        etInput = (EditText) findViewById(R.id.d_input);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnDeleteRow = (Button) findViewById(R.id.btn_delete_row);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnToggleView = (Button) findViewById(R.id.btn_toggle_view);
        //pay tithe button:
        btnDelete.setOnClickListener(new View.OnClickListener() {

            int i = 0;
            @Override
            public void onClick(View v) {
                if(toggle % 2 == 0) {
                    toggle += 1;
                    btnToggleView = (Button) findViewById(R.id.btn_toggle_view);
                    btnToggleView.setText("Toggle Tithe:");
                    update();
                }

                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    btnDelete.setText("Pay Tithes");
                    handler.postDelayed(r, 1750);
                } else if (i == 2) {
                    myDb.deleteAllData();
                    update();
                    i = 0;
                }
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                }, 1750);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void inputButton(View view) throws InterruptedException {
        input = etInput.getText().toString();
        date = new SimpleDateFormat("MM/dd/yy").format(new Date());

        if(!etInput.getText().toString().equals(".")){
            if(!etInput.getText().toString().equals("")){
                BackgroundTask backgroundTask =  new BackgroundTask(this);
                backgroundTask.execute("add_info", input, date);
                etInput.setText("");
            }else {
                Toast.makeText(this,"Please Insert An Income First...",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"Invalid Income Insertion...",Toast.LENGTH_SHORT).show();
        }
        Thread.sleep(50);
        update();
    }

    public void clearButton(View view) {
        if(!etInput.getText().toString().equals(""))
            etInput.setText("");
        else
            Toast.makeText(this,"No Input To Delete",Toast.LENGTH_SHORT).show();
    }

    public void titheButton(View view) throws InterruptedException {
        btnDelete.setText("Pay Tithes");
    }

    public void getTenPercent() {
        Cursor res = myDb.getAllData();
        double tithe = 0.00;
        while(res.moveToNext()){
            if(!res.getString(1).equals(""))
                tithe += Double.parseDouble(res.getString(1)) / 10;
        }
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setText("$" + String.valueOf(df2.format(tithe)));
        res.close();
    }
    public void getTotal() {
        Cursor res = myDb.getAllData();
        double total = 0.00;
        while(res.moveToNext()){
            if(!res.getString(1).equals(""))
                total += Double.parseDouble(res.getString(1));
        }
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setText("$" + String.valueOf(df2.format(total)));
        res.close();
    }

    public void toggleViewButton(View view) {
        toggle += 1;
        if(toggle % 2 == 0) {
            btnToggleView = (Button) findViewById(R.id.btn_toggle_view);
            btnToggleView.setText("Toggle Total:");
            getTotal();
        }else {
            btnToggleView = (Button) findViewById(R.id.btn_toggle_view);
            btnToggleView.setText("Toggle Tithe:");
            getTenPercent();
        }
    }
    public void update() {
        if(toggle % 2 == 0)
            getTotal();
        else
            getTenPercent();
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute("get_info");
    }

    public void deleteRowButton(View view) throws InterruptedException {
        Thread.sleep(500);
        update();
    }
}
