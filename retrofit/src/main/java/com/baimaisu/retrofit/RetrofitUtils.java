package com.baimaisu.retrofit;

import android.os.Build;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class RetrofitUtils {
    static void checkInterface(Class<?> clazz) {
        if (clazz == null) {
            fail("the clazz is null");
        }
        if (!clazz.isInterface()) {
            fail("the clazz must be interface");
        }

    }

    private static void fail(String message) {
        throw new RuntimeException(message);
    }

    static String createKey(Method method) {
        Projection projection = method.getAnnotation(Projection.class);
        if (projection != null) {
            return String.valueOf(projection.name());
        }

        throw new RuntimeException("method must has Projection,and name is a int");
    }

    @SuppressWarnings("SameParameterValue")
    static Class checkMethodReturnType(Method method, Class<?> superClazz, Class... interfaces) {
        Class clazz = method.getReturnType();
        if (!clazz.isAssignableFrom(superClazz)) {
            fail("clazz must be extend for " + superClazz.getName());
        }

        List<Class> list = new ArrayList<>();
        collectInterfaces(clazz, list);
        for (Class iInterface : interfaces) {
            if (!list.contains(iInterface)) {
                fail("clazz must be impl for " + iInterface.getName());
            }
        }

        return clazz;
    }

    private static void collectInterfaces(Class<?> clazz, List<Class> list) {
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class iInterface : interfaces) {
            list.add(iInterface);
            collectInterfaces(iInterface, list);
        }
    }

    static boolean isDefaultMethod(Method method) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return method.isDefault();
        }
        return false;
    }
}
