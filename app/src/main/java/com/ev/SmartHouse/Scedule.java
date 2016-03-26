package com.ev.SmartHouse;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class Scedule extends AppCompatActivity {
    Switch sD1, sD2, sD3, sD4;
    ToggleButton tglSAT,tglSUN,tglMON,tglTUE,tglWED,tglTHU,tglFRI;
    Button btnDate, btnTime, btnSave;
    int Thoure, Tminute, Dyear, Dmonth, Dday;

    EditText ed;
    String Job="",St = "", D1 = "5 off", D2 = "6 off", D3 = "13 off", D4 = "26 off",Week="",SAT="",SUN="",MON="",TUE="",WED="",THU="",FRI="";
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
        tglSAT=(ToggleButton)findViewById(R.id.tglSAT);
        tglSUN=(ToggleButton)findViewById(R.id.tglSUN);
        tglMON=(ToggleButton)findViewById(R.id.tglMON);
        tglTUE=(ToggleButton)findViewById(R.id.tglTUE);
        tglWED=(ToggleButton)findViewById(R.id.tglWED);
        tglTHU=(ToggleButton)findViewById(R.id.tglTHU);
        tglFRI=(ToggleButton)findViewById(R.id.tglFRI);
        ed = (EditText) findViewById(R.id.edComment);

        app.deviceGet(Scedule.this,sD1,sD2,sD3,sD4);

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
                        Toast.makeText(Scedule.this, Dyear+"/"+Dmonth+"/"+Dday, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), Thoure + " : " + Tminute, Toast.LENGTH_SHORT).show();
                    }
                }, Calendar.HOUR, Calendar.MINUTE, true);
                tp.show();
            }
        });

        tglSUN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUN=(tglSUN.isChecked())?"0":"";
                setScedual();
            }
        });
        tglMON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MON=(tglMON.isChecked())?"1":"";
                setScedual();

            }
        });
        tglTUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TUE=(tglTUE.isChecked())?"2":"";
                setScedual();

            }
        });
        tglWED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WED=(tglWED.isChecked())?"3":"";
                setScedual();

            }
        });
        tglTHU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                THU=(tglTHU.isChecked())?"4":"";
                setScedual();

            }
        });
        tglFRI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRI=(tglFRI.isChecked())?"5":"";
                setScedual();

            }
        });
        tglSAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SAT=(tglSAT.isChecked())?"6":"";
                setScedual();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScedual();
                if(Job.contains("0 0 0 0 ")){
                    Toast.makeText(Scedule.this, "Set Time and Date Please!!!", Toast.LENGTH_SHORT).show();

                }
                else if(ed.getText().toString().isEmpty()){
                    Toast.makeText(Scedule.this, "Give a name for the job, please", Toast.LENGTH_SHORT).show();
                }
                else{
                    St = D1 + " " + D2 + " " + D3 + " " + D4;
                    RequestParams params = new RequestParams();
                    params.put("user", app.getUser(Scedule.this));
                    params.put("job", Job);
                    params.put("deviceStatus", St);
                    params.put("comment",ed.getText().toString());
                    client = new AsyncHttpClient();
                    client.post(app.url + "createJob", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                Toast.makeText(Scedule.this, "DONE", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Scedule.this, "Error "+response, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Toast.makeText(Scedule.this,responseString, Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                        }


                    });
                }
            }
        });
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

    void setScedual(){

        switch (SUN)
        {
            case "0":
                Week=SUN;
                switch (MON){
                    case "1":
                        Week+=","+MON;
                        switch (TUE){
                            case "2":
                                Week+=","+TUE;
                                switch (WED){
                                    case "3":
                                        Week+=","+WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "":
                                Week+=TUE;
                                switch (WED){
                                    case "3":
                                        Week+=","+WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                    case "":
                        Week+=MON;
                        switch (TUE){
                            case "2":
                                Week+=","+TUE;
                                switch (WED){
                                    case "3":
                                        Week+=","+WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "":
                                Week+=TUE;
                                switch (WED){
                                    case "3":
                                        Week+=","+WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
                break;
            case "":
                Week=SUN;
                switch (MON){
                    case "1":
                        Week+=MON;
                        switch (TUE){
                            case "2":
                                Week+=","+TUE;
                                switch (WED){
                                    case "3":
                                        Week+=","+WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;switch (SAT){
                                                        case "6":
                                                            Week+=","+SAT;
                                                            break;
                                                        case "":
                                                            Week+=SAT;
                                                            break;
                                                    }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "":
                                Week+=TUE;
                                switch (WED){
                                    case "3":
                                        Week+=","+WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                    case "":
                        Week+=MON;
                        switch (TUE){
                            case "2":
                                Week+=TUE;
                                switch (WED){
                                    case "3":
                                        Week+=","+WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "":
                                Week+=TUE;
                                switch (WED){
                                    case "3":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=","+THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                    case "":
                                        Week+=WED;
                                        switch (THU){
                                            case "4":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=","+FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                            case "":
                                                Week+=THU;
                                                switch (FRI){
                                                    case "5":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=","+SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                    case "":
                                                        Week+=FRI;
                                                        switch (SAT){
                                                            case "6":
                                                                Week+=SAT;
                                                                break;
                                                            case "":
                                                                Week+=SAT;
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
                break;
        }

        Job = Tminute + " " + Thoure + " " + Dday + " " + Dmonth + " " + Week;
        if(Job.equals("")||Job.equals("0 0 0 0 ")){btnSave.setEnabled(false);}
        else{btnSave.setEnabled(true);}
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
                Toast.makeText(Scedule.this, "5asfo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.closeOp:
                Scedule.this.finish();
                break;
            case R.id.editOp:
                showDialog();
                break;
            case R.id.setOp:
                Toast.makeText(Scedule.this, "Setting", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showDialog(){
        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        LayoutInflater in = this.getLayoutInflater();
        final View dialogView = in.inflate(R.layout.editdialogg, null);
        final EditText ed1 =(EditText)dialogView.findViewById(R.id.editD1);
        final EditText ed2 =(EditText)dialogView.findViewById(R.id.editD2);
        final EditText ed3 =(EditText)dialogView.findViewById(R.id.editD3);
        final EditText ed4 =(EditText)dialogView.findViewById(R.id.editD4);
        editDialog.setView(dialogView)
                .setTitle("Rename Your Devices")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sD1.setText(ed1.getText().toString());
                        sD2.setText(ed2.getText().toString());
                        sD3.setText(ed3.getText().toString());
                        sD4.setText(ed4.getText().toString());
                        app.setDevice(Scedule.this,ed1.getText().toString().trim(),ed2.getText().toString().trim(),ed3.getText().toString().trim(),ed4.getText().toString().trim());
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
            app.deviceGet(Scedule.this, ed1, ed2, ed3, ed4);
        }


    }
}


