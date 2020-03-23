package com.baimaisu.process.convert;

import java.util.ArrayList;
import java.util.List;

public class ConverterFactory {
    private static ConverterFactory factory = new ConverterFactory();

    public static ConverterFactory get() {
        return factory;
    }

    private List<Converter<Object>> converters = new ArrayList<>();

    public void postAppendConverter(Converter<Object> converter) {
        this.converters.add(converter);
    }


    public synchronized String toJson(Object data) {
        for (Converter<Object> converter : converters) {
            if (converter.isSupport(data)) {
                String str = converter.convert(data);
                if (str != null) {
                    return str;
                }
            }
        }
        return null;
    }


    public synchronized <T> T fromJson(String json, Class<T> clazz) {
        for (Converter<Object> converter : converters) {
            if (converter.isSupport(clazz)) {
                @SuppressWarnings("unchecked") T data = (T) converter.convert(json, clazz);
                if (data != null) {
                    return data;
                }
            }
        }
        return null;
    }

}
