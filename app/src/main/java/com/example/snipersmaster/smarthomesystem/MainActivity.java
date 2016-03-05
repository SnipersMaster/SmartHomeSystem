package com.example.snipersmaster.smarthomesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    Button btn;
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
        btn=(Button)findViewById(R.id.button);
        d1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("D1","1");}
                else if(!isChecked){Change("D1","0");}
            }
        });
        d2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("D2","1");}
                else if(!isChecked){Change("D2","0");}
            }
        });
        d3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("D3","1");}
                else if(!isChecked){Change("D3","0");}
            }
        });
        d4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("D4","1");}
                else if(!isChecked){Change("D4","0");}
            }
        });
        //Pushbots.sharedInstance().init(this);
        statistics();
        /*avgdaily();
        avgmonthly();
        avgseasonlly();
        current();*/
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
                    if(avgSeasonlly.getString("AVGM").equals("null")){
                        tm.setText("No Recordes ");
                    }else {
                        tm.setText(avgSeasonlly.getString("AVGM"));
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
    void Change(String name,String mode){
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

    }
}
