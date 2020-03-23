package com.baimaisu.atest;

import android.app.Application;
import android.content.Context;

import com.baimaisu.process.AppInit;
import com.didichuxing.doraemonkit.DoraemonKit;

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.init(this);
        DoraemonKit.install(this);
    }
}
