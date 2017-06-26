package com.blessedtactics.programs.backpacker;

import android.app.Application;

import io.realm.Realm;


public class App extends Application {

    public final static String LOG_TAG = "Backpacker";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
