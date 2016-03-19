package com.example.snipersmaster.smarthomesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    int end=0;
    boolean bootup=false;
    Switch d1,d2,d3,d4;
    TextView tc,tm,ts,td;
    AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        td=(TextView)findViewById(R.id.txtDailyTemp);
        tc=(TextView)findViewById(R.id.txtCurrentTemp);
        tm=(TextView)findViewById(R.id.txtMonthlyTemp);
        ts=(TextView)findViewById(R.id.txtSeasonallyTemp);
        d1=(Switch)findViewById(R.id.device1);
        d2=(Switch)findViewById(R.id.device2);
        d3=(Switch)findViewById(R.id.device3);
        d4=(Switch)findViewById(R.id.device4);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        app.deviceGet(MainActivity.this, d1, d2, d3, d4);

        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d1.isChecked()) {
                    Change("D1", "1", d1);
                } else if (!d1.isChecked()) {
                    Change("D1", "0", d1);
                }
            }
        });
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d2.isChecked()) {
                    Change("D2", "1", d2);
                } else if (!d2.isChecked()) {
                    Change("D2", "0", d2);
                }
            }
        });
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d3.isChecked()) {
                    Change("D3", "1", d3);
                } else if (!d3.isChecked()) {
                    Change("D3", "0", d3);
                }
            }
        });
        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d4.isChecked()) {
                    Change("D4", "1", d4);
                } else if (!d4.isChecked()) {
                    Change("D4", "0", d4);
                }
            }
        });

        statistics();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scedule.class);
                startActivity(intent);
            }
        });


    }

    void statistics(){
        RequestParams params = new RequestParams();
        params.put("user",app.getUser(MainActivity.this));
        client = new AsyncHttpClient();
        client.post(app.url+"bootup", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject currentTemp = response.getJSONArray(0).getJSONObject(0);
                    tc.setText(currentTemp.getString("Temp"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    tc.setText("No Recordes ");
                }
                try {
                    JSONObject avgDaily = response.getJSONArray(1).getJSONObject(0);
                    if(avgDaily.getString("AVGD").equals("null")){
                        td.setText("No Recordes ");
                    }else {
                        td.setText(avgDaily.getString("AVGD"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
                try{
                    JSONObject avgMonthly = response.getJSONArray(2).getJSONObject(0);
                    tm.setText(avgMonthly.getString("AVGM"));
                    if(avgMonthly.getString("AVGM").equals("null")){
                        tm.setText("No Recordes ");
                    }else {
                        tm.setText(avgMonthly.getString("AVGD"));
                    }

            } catch (JSONException e) {
                e.printStackTrace();

            }
                try{
                    JSONObject avgSeasonlly = response.getJSONArray(3).getJSONObject(0);
                    ts.setText(avgSeasonlly.getString("AVGS"));
                    if(avgSeasonlly.getString("AVGS").equals("null")){
                        ts.setText("No Recordes ");
                    }else {
                        ts.setText(avgSeasonlly.getString("AVGS"));
                    }
            } catch (JSONException e) {
                e.printStackTrace();
                    ts.setText("No Recordes");
            }
                try{
                    JSONObject statusD1 = response.getJSONArray(4).getJSONObject(0);
                    if (statusD1.getString("dMode").equals("1")){d1.setChecked(true);}
                    else{d1.setChecked(false);}
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                try{

                    JSONObject statusD2 = response.getJSONArray(4).getJSONObject(1);
                    if (statusD2.getString("dMode").equals("1")){d2.setChecked(true);}
                    else{d2.setChecked(false);}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{

                    JSONObject statusD2 = response.getJSONArray(4).getJSONObject(2);
                    if (statusD2.getString("dMode").equals("1")){d3.setChecked(true);}
                    else{d3.setChecked(false);}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{

                    JSONObject statusD2 = response.getJSONArray(4).getJSONObject(3);
                    if (statusD2.getString("dMode").equals("1")){d4.setChecked(true);}
                    else{d4.setChecked(false);}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    bootup=true;

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
        {
            super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });


    }

    void Change(String name,String mode,final Switch s){
        if(bootup) {
            RequestParams params = new RequestParams();
            params.put("user", app.getUser(MainActivity.this));
            params.put("DN", name);
            params.put("DM", mode);
            client = new AsyncHttpClient();
            client.post(app.url+ "changeDeviceMode", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        JSONObject obj = response.getJSONObject(0);
                        if(obj.getString("status").equals("Device Is Not Connected")){
                            if (s.isChecked()) {s.setChecked(false);}
                            else{ s.setChecked(true);}
                        }
                        Toast.makeText(MainActivity.this, obj.getString("status"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        if (end>=1)
        {
            finish();
        }
        end++;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater edit =getMenuInflater();
        edit.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.helpOp:
                Toast.makeText(MainActivity.this, "5asfo", Toast.LENGTH_SHORT).show();
            break;
            case R.id.closeOp:
                MainActivity.this.finish();
                break;
            case R.id.editOp:
                showDialog();
                break;
            case R.id.setOp:
                Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        LayoutInflater in = this.getLayoutInflater();
        final View dialogView = in.inflate(R.layout.editdialog, null);
        final EditText ed1 =(EditText)dialogView.findViewById(R.id.editD1);
        final EditText ed2 =(EditText)dialogView.findViewById(R.id.editD2);
        final EditText ed3 =(EditText)dialogView.findViewById(R.id.editD3);
        final EditText ed4 =(EditText)dialogView.findViewById(R.id.editD4);
        editDialog.setView(dialogView)
                .setTitle("Rename Your Devices")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d1.setText(ed1.getText().toString());
                        d2.setText(ed2.getText().toString());
                        d3.setText(ed3.getText().toString());
                        d4.setText(ed4.getText().toString());
                        app.setDevice(MainActivity.this,ed1.getText().toString().trim(),ed2.getText().toString().trim(),ed3.getText().toString().trim(),ed4.getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = editDialog.create();
        alert.show();
        if (alert.isShowing()){
            app.deviceGet(MainActivity.this,ed1,ed2,ed3,ed4);
        }


    }
}
