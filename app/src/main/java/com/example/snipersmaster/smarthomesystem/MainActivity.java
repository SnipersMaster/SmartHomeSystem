package com.example.snipersmaster.smarthomesystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Switch d1,d2,d3,d4;
    TextView tc,tm,ts,td;
    AsyncHttpClient client;
    String url = "http://192.168.0.91:3000/";
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
        d1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("1","D1","1");}
                else if(!isChecked){Change("1","D1","0");}
            }
        });
        d2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("1","D2","1");}
                else if(!isChecked){Change("1","D2","0");}
            }
        });
        d3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("1","D3","1");}
                else if(!isChecked){Change("1","D3","0");}
            }
        });
        d4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){Change("1","D4","1");}
                else if(!isChecked){Change("1","D4","0");}
            }
        });


        avgdaily();
        avgmonthly();
        avgseasonlly();
        current();
    }
    void avgdaily(){
        RequestParams params = new RequestParams();
        params.put("user","1");
        client = new AsyncHttpClient();
        client.post(url+"avgDailytemp", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject obj = response.getJSONObject(0);
                    td.setText(obj.getString("AVG"));
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
    void avgmonthly(){
        RequestParams params = new RequestParams();
        params.put("user","1");
        client = new AsyncHttpClient();
        client.post(url+"avgMonthlyTemp", params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject obj = response.getJSONObject(0);
                    tm.setText(obj.getString("AVG"));
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
    void avgseasonlly(){
        RequestParams params = new RequestParams();
        params.put("user","1");
        client = new AsyncHttpClient();
        client.post(url+"avgSeason", params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject obj = response.getJSONObject(0);
                    ts.setText(obj.getString("AVG"));
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
    void current(){
        RequestParams params = new RequestParams();
        params.put("user","1");
        client = new AsyncHttpClient();
        client.post(url+"currentTemp", params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject obj = response.getJSONObject(0);
                    tc.setText(obj.getString("Temp"));
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
    void Change(String user,String name,String mode){
        RequestParams params = new RequestParams();
        params.put("user",user);
        params.put("DN",name);
        params.put("DM",mode);
        client = new AsyncHttpClient();
        client.post(url+"changeDeviceMode", params, new JsonHttpResponseHandler() {
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
