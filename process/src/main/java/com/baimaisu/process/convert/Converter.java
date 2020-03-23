package com.baimaisu.process.convert;

public interface Converter<F> {
    String convert(F value);

    F convert(String value, Class clazz);

    boolean isSupport(Object o);

    boolean isSupport(Class clazz);
}
