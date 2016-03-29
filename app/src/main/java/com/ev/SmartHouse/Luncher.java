package com.ev.SmartHouse;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class Luncher extends AppCompatActivity {
    AsyncHttpClient client;
    Button btnQR,btnin;
    private static final String TAG = "Tag";


    EditText txuser,txpass,txQR;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if(app.getLuncher(Luncher.this)==0){

        }else if(app.getLuncher(Luncher.this)==1){
            Intent intent=new Intent(Luncher.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        btnQR=(Button)findViewById(R.id.Scan);
        btnin=(Button)findViewById(R.id.btnSingIn);
        txuser=(EditText)findViewById(R.id.Userin);
        txpass=(EditText)findViewById(R.id.Passin);
        txQR=(EditText)findViewById(R.id.QR_Codein);
        //read QR
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Luncher.this, Scanner.class);
                startActivity(in);
            }
        });
        registerReceiver();
        //log-in
        btnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user pass qr
                RequestParams params = new RequestParams();
                params.put("user", txuser.getText().toString());
                params.put("pass", txpass.getText().toString());
                params.put("qr", txQR.getText().toString());
                client = new AsyncHttpClient();
                client.post(app.url + "auth", params, new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            // Registering BroadcastReceiver

                            Toast.makeText(Luncher.this, response.getJSONObject(0).getString("ID"), Toast.LENGTH_SHORT).show();

                          app.setUser(Luncher.this, response.getJSONObject(0).getString("ID"));
                          RegistrationIntentService.id=response.getJSONObject(0).getString("ID");


                            if (checkPlayServices()) {
                                // Start IntentService to register this application with GCM.
                                Intent intent = new Intent(Luncher.this, RegistrationIntentService.class);
                                startService(intent);
                            }
                            Intent main=new Intent(Luncher.this,MainActivity.class);
                            startActivity(main);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Luncher.this, "Username or Password is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //   }
                    // });
                });
            }});
    }
    @Override
    protected void onResume() {
        super.onResume();

        txQR.setText(app.QR);
        //registerReceiver();
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;

        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
