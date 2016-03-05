package com.example.snipersmaster.smarthomesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class Luncher extends AppCompatActivity {
    AsyncHttpClient client;
    Button btnQR,btnin;
    EditText txuser,txpass,txQR;
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
                client.post(app.url+"auth", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONObject user = response.getJSONObject(0);
                            app.setUser(Luncher.this,user.getString("ID"));
                            //    Toast.makeText(Luncher.this, user.getString("ID"), Toast.LENGTH_SHORT).show();
                            Intent app = new Intent(Luncher.this,MainActivity.class);
                            app.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(app);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Luncher.this, "Username or Password is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        txQR.setText(app.QR);
    }

}
