package com.baimaisu.process.processor;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface ContentProcessor {
    Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder);

    String getType(@NonNull Uri uri);

    Uri insert(@NonNull Uri uri, @Nullable ContentValues values);

    int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs);

    int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs);
}
