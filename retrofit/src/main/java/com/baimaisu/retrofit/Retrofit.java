package com.baimaisu.retrofit;

import com.baimaisu.process.CrossLiveData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

public class Retrofit {
    static class MyInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            if (RetrofitUtils.isDefaultMethod(method)) {
                throw new UnsupportedOperationException();
            }
            String key = RetrofitUtils.createKey(method);
            if (CrossLiveDataCache.get().contains(key)) {
                return CrossLiveDataCache.get().get(key);
            }

            Class clazz = RetrofitUtils.checkMethodReturnType(method, CrossLiveData.class);
            CrossLiveData instance = (CrossLiveData) clazz.newInstance();
            instance.setIdentifier(key);
            CrossLiveDataCache.get().put(key, instance);

            instance.setValueClass(((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]);
            instance.addRecycleCallBack(new CrossLiveData.RecycleCallBack() {
                @Override
                public void onRecycle(CrossLiveData crossLiveData) {
                    CrossLiveDataCache.get().remove(crossLiveData);
                }
            });
            return instance;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T createApi(Class<T> clazz) {
        RetrofitUtils.checkInterface(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MyInvocationHandler());
    }
}
