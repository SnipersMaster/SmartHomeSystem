package com.example.snipersmaster.smarthomesystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
TextView tc,tm,ts,td;
    AsyncHttpClient client;
    String url = "http://192.168.0.91:3000/avgdailytemp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        td=(TextView)findViewById(R.id.txtDailyTemp);
        tc=(TextView)findViewById(R.id.txtCurrentTemp);
        tm=(TextView)findViewById(R.id.txtMonthlyTemp);
        ts=(TextView)findViewById(R.id.txtSeasonallyTemp);
        avgdaily();
    }
    void avgdaily(){
        RequestParams params = new RequestParams();
        params.put("user","1");
        client = new AsyncHttpClient();
        client.post(url, params,new JsonHttpResponseHandler() {
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
        });

    }
    void avgmonthly(){

    }
    void avgseasonlly(){

    }
    void current(){

    }
}
