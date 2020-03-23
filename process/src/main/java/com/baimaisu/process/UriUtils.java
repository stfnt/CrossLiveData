package com.baimaisu.process;

import android.net.Uri;
import android.text.TextUtils;

public class UriUtils {

    private static String FLAG_DELETE = getDeleteIdentifier();
    public static Uri withDelete(Uri uri){
        return uri.buildUpon().appendPath(FLAG_DELETE).build();
    }

    public static boolean isDeleteUri(Uri uri){
        return !TextUtils.isEmpty(uri.toString()) && uri.toString().endsWith(FLAG_DELETE);
    }


    private static String getDeleteIdentifier(){
        return "CrossLiveDataDeleted";
    }
}
