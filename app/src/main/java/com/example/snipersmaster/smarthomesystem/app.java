package com.example.snipersmaster.smarthomesystem;

import android.content.Context;
import android.content.SharedPreferences;

public class app {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static String QR="";
    public static String url="http://104.197.212.107:3000/";
    public static String user="";
    public static void Set(String QRget){
        QR=QRget;
    }
    public static void setUser(Context context,String userget){

        try {
            sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString("user", userget);
            editor.putInt("luncher",1);
            editor.apply();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static String getUser(Context context){
        sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
        return sharedPreferences.getString("user","1");
    }
    public static Integer getLuncher(Context context){
        sharedPreferences = context.getSharedPreferences("smarthouse",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("luncher", 0);
    }



}

