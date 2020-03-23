package com.baimaisu.process;

import android.content.Context;

import com.baimaisu.process.convert.ConverterFactory;
import com.baimaisu.process.convert.impl.GsonConverter;

public class AppInit {
    public static void init(Context context) {
        ApplicationHolder.init(context.getApplicationContext());
        ConverterFactory.get().postAppendConverter(new GsonConverter());
    }
}
