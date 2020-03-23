package com.baimaisu.process.convert.impl;

import com.baimaisu.process.convert.Converter;
import com.google.gson.Gson;

@SuppressWarnings("SpellCheckingInspection")
public class GsonConverter implements Converter<Object> {

    private Gson gson = new Gson();
    @Override
    public String convert(Object value) {
        return gson.toJson(value);
    }

    @Override
    public Object convert(String value, Class clazz) {
        return gson.fromJson(value, clazz);
    }

    @Override
    public boolean isSupport(Object o) {
        return true;
    }

    @Override
    public boolean isSupport(Class clazz) {
        return true;
    }
}
