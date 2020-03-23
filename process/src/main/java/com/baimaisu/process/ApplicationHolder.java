package com.baimaisu.process;

import android.annotation.SuppressLint;
import android.content.Context;

public class ApplicationHolder {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }
}
