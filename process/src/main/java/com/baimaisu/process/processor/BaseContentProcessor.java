package com.baimaisu.process.processor;

import android.content.ContentValues;
import android.content.UriMatcher;

import com.baimaisu.process.ApplicationHolder;

public abstract class BaseContentProcessor implements ContentProcessor {
    protected static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static String AUTHORITY = "";
    private static String Scheme = "content://";

    private static final String CROSS_LIVE_DATA = "crossLiveData";

    private static final int MATCH_CALLER = 1000;
    protected static final int MATCH_LIVEDATA = MATCH_CALLER + 1;
    protected static final String TABLE_NAME = "liveData";
    public static final String COLUMN_LIVE_KEY = "liveKey";
    public static final String COLUMN_LIVE_DATA = "liveData";

//    protected static void initUriMatcher() {
//        AUTHORITY = "content://" + ApplicationHolder.getContext().getPackageName() + ".myProvider";
//        uriMatcher.addURI(AUTHORITY, CROSS_LIVE_DATA, MATCH_LIVEDATA);
//    }

    static {
        AUTHORITY = ApplicationHolder.getContext().getPackageName() + ".myProvider";
        uriMatcher.addURI(AUTHORITY, CROSS_LIVE_DATA + "/#", MATCH_LIVEDATA);
    }

    public static String getBaseUriPath() {
        return Scheme + AUTHORITY + "/" + CROSS_LIVE_DATA;
    }

    public static ContentValues createContentValues(String liveKey, String liveData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LIVE_KEY, liveKey);
        contentValues.put(COLUMN_LIVE_DATA, liveData);
        return contentValues;
    }

}
