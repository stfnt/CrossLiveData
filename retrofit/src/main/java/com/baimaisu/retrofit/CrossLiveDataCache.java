package com.baimaisu.retrofit;

import android.text.TextUtils;

import com.baimaisu.process.CrossLiveData;

import java.util.HashMap;
import java.util.Map;

class CrossLiveDataCache {
    private static CrossLiveDataCache crossLiveDataCache = new CrossLiveDataCache();

    static CrossLiveDataCache get() {
        return crossLiveDataCache;
    }

    private HashMap<String, CrossLiveData> cache = new HashMap<>();

    void put(String key, CrossLiveData crossLiveData) {
        cache.put(key, crossLiveData);
    }

    private void remove(String key) {
        CrossLiveData crossLiveData = cache.get(key);
        cache.remove(key);
        if (crossLiveData != null && !crossLiveData.isRecycled()) {
            crossLiveData.recycle();
        }
    }

    boolean contains(String key) {
        return cache.containsKey(key);
    }

    CrossLiveData get(String key) {
        return cache.get(key);
    }

    void remove(CrossLiveData crossLiveData) {
        String key = "";
        for (Map.Entry<String, CrossLiveData> entry : cache.entrySet()) {
            if (entry.getValue() == crossLiveData) {
                key = entry.getKey();
            }
        }
        if (!TextUtils.isEmpty(key)) {
            remove(key);
        }
    }
}
