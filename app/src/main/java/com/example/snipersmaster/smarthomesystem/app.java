package com.example.snipersmaster.smarthomesystem;

import android.app.Application;

import com.pushbots.push.Pushbots;

/**
 * Created by SnipersMaster on 3/5/16.
 */
public class app extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
       // Pushbots.sharedInstance().init(this);

    }
}
