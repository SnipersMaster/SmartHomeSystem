package com.ev.SmartHouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import org.json.JSONArray;

public class app {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    //63a3ceac95e1307c1d2e96261d3103044307b7ef
    public static String QR="";
    public static String Mode="";
    public static String local="localhost",remote="104.236.253.6";
    public static String url="http://104.236.253.6:3000/";
    public static String user="";

    static boolean isGCMregister;
    public static void gcmregister(Context context,String token){
        isGCMregister=false;
        try {
            sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString("token", token);
            editor.putBoolean("isGCMregister",false);
            editor.apply();
        }catch (Exception ex){ex.printStackTrace();}

    }
    public static void setGCMisRegistered(Context context){
        isGCMregister=false;
        try {
            sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putBoolean("isGCMregister",true);
            editor.apply();
        }catch (Exception ex){ex.printStackTrace();}

    }
    public static boolean isgcmregistered(Context context){
            sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
            return sharedPreferences.getBoolean("isGCMregister",false);

    }

    public static void URL(String mode){
        if (mode.equals("1")){
            Mode ="1";
            url="http://"+local+":4000/";}
        else if (mode.equals("0"))
        {Mode="0";
            url="http://"+remote+":3000/";}
    }
    public static String getURL(){
        return Mode;
    }

    public static void Set(String QRget){QR=QRget;}

    public static void setUser(Context context,String userget){
        try {
            sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString("user", userget);
            editor.putInt("luncher",1);
            editor.apply();
        }catch (Exception ex){ex.printStackTrace();}
    }

    public static String getUser(Context context){
        sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
        return sharedPreferences.getString("user","1");
    }

    public static Integer getLuncher(Context context){
        sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("luncher", 0);
    }

    public static void setDevice(Context context,String d1,String d2, String d3, String d4){
        try {
            sharedPreferences = context.getSharedPreferences("smarthouse", Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString("d1", "\""+d1+"\"");
            editor.putString("d2", "\""+d2+"\"");
            editor.putString("d3", "\""+d3+"\"");
            editor.putString("d4", "\""+d4+"\"");
            editor.apply();
        }catch (Exception ex){ex.printStackTrace();}
    }

    public static String getDevice(Context context){
        sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
        String option1 = sharedPreferences.getString("d1","Device 1");
        String option2 = sharedPreferences.getString("d2","Device 2");
        String option3 = sharedPreferences.getString("d3","Device 3");
        String option4 = sharedPreferences.getString("d4","Device 4");
        String share = "[{d1:"+option1+"},{d2:"+option2+"},{d3:"+option3+"},{d4:"+option4+"}]";
        return share;
    }

    public static void deviceGet(Context context,View v1,View v2,View v3,View v4){
        try {
            JSONArray j=new JSONArray(app.getDevice(context));
            String obj1=j.getJSONObject(0).getString("d1");
            String obj2=j.getJSONObject(1).getString("d2");
            String obj3=j.getJSONObject(2).getString("d3");
            String obj4=j.getJSONObject(3).getString("d4");
            if(v1 instanceof EditText){
                EditText d1 = (EditText) v1;
                EditText d2 = (EditText) v2;
                EditText d3 = (EditText) v3;
                EditText d4 = (EditText) v4;
                d1.setHint(obj1);
                d2.setHint(obj2);
                d3.setHint(obj3);
                d4.setHint(obj4);
            }else if(v1 instanceof Switch){
                Switch d1 = (Switch) v1;
                Switch d2 = (Switch) v2;
                Switch d3 = (Switch) v3;
                Switch d4 = (Switch) v4;
                d1.setText(obj1);
                d2.setText(obj2);
                d3.setText(obj3);
                d4.setText(obj4);}

        } catch (Exception e) {e.printStackTrace();}
    }

}
