package com.example.snipersmaster.smarthomesystem;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


public class Scedule extends AppCompatActivity {
    Switch sD1, sD2, sD3, sD4;
    Button btnDate, btnTime, btnSave;
    int Thoure, Tminute, Dyear, Dmonth, Dday;
    TextView tv;
    String St = "", D1 = "5 off", D2 = "6 off", D3 = "13 off", D4 = "26 off";
    AsyncHttpClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scedule);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnTime = (Button) findViewById(R.id.btnTime);
        btnSave = (Button) findViewById(R.id.btnSaveSceduale);
        sD1 = (Switch) findViewById(R.id.sD1);
        sD2 = (Switch) findViewById(R.id.sD2);
        sD3 = (Switch) findViewById(R.id.sD3);
        sD4 = (Switch) findViewById(R.id.sD4);
        tv = (TextView) findViewById(R.id.textView4);
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = df.format(c.getTime());
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(Scedule.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        Dyear = year;
                        Dmonth = month;
                        Dday = day;
                        Toast.makeText(Scedule.this, formattedDate, Toast.LENGTH_SHORT).show();
                    }
                }).textConfirm("CONFIRM") //text of confirm button
                        .textCancel("CANCEL") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                        .minYear(2016) //min year in loop
                        .dateChose(formattedDate) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(Scedule.this);
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(Scedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Thoure = hourOfDay;
                        Tminute = minute;
                        Toast.makeText(getApplicationContext(), String.valueOf(Calendar.HOUR) + " : " + String.valueOf(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
                        tv.setText(Thoure + " : " + Tminute);
                    }
                }, Calendar.HOUR, Calendar.MINUTE, true);
                tp.setMessage("Select a Date:");
                tp.show();


            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.set(Calendar.YEAR, Dyear);
                c.set(Calendar.MONTH, Dmonth);
                c.set(Calendar.DAY_OF_MONTH, Dday);
                int week = (c.get(Calendar.DAY_OF_WEEK) >= 7) ? c.get(Calendar.DAY_OF_WEEK) - 4 : c.get(Calendar.DAY_OF_WEEK) + 3;
                String Job = Tminute + " " + Thoure + " " + Dday + " " + Dmonth + " " + week;
                St = D1 + " " + D2 + " " + D3 + " " + D4;
                Toast.makeText(Scedule.this, D1 + " " + D2 + " " + D3 + " " + D4, Toast.LENGTH_SHORT).show();
                RequestParams params = new RequestParams();
                params.put("user", app.getUser(Scedule.this));
                params.put("job", Job);
                params.put("deviceStatus", D1 + " " + D2 + " " + D3 + " " + D4);
                client = new AsyncHttpClient();
                client.post(app.url + "createJob", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            Toast.makeText(Scedule.this, "DONE", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(Scedule.this, responseString, Toast.LENGTH_SHORT).show();
                        throwable.printStackTrace();
                    }


                }); }});
                sD1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                               {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (isChecked) {
                                                           D1 = "5 on";
                                                       } else {
                                                           D1 = "5 off";
                                                       }
                                                   }
                                               }

                );

                sD2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                               {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (isChecked) {
                                                           D2 = "6 on";
                                                       } else {
                                                           D2 = "6 off";
                                                       }
                                                   }
                                               }

                );

                sD3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                               {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (isChecked) {
                                                           D3 = "13 on";
                                                       } else {
                                                           D3 = "13 off";
                                                       }
                                                   }
                                               }

                );

                sD4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (isChecked) {
                                                           D4 = "26 on";
                                                       } else {
                                                           D4 = "26 off";
                                                       }
                                                   }
                                               });
            }

    }


