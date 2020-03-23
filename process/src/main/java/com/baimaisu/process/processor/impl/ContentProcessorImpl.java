package com.baimaisu.process.processor.impl;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baimaisu.process.ApplicationHolder;
import com.baimaisu.process.UriUtils;
import com.baimaisu.process.processor.BaseContentProcessor;

public class ContentProcessorImpl extends BaseContentProcessor {
    private SQLiteDatabase db;

    class MySQLiteOpenHelper extends SQLiteOpenHelper {

        public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists " + TABLE_NAME + "(_id integer primary key autoincrement, " + COLUMN_LIVE_KEY + " TEXT ," + COLUMN_LIVE_DATA + " TEXT )");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public ContentProcessorImpl() {
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteOpenHelper(ApplicationHolder.getContext(), "liveData.db", null, 1, null);
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcher.match(uri) == MATCH_LIVEDATA) {
            return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        }

        throw new RuntimeException("fail to query" + uri);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) == MATCH_LIVEDATA) {
            long result = db.insert(TABLE_NAME, null, values);
            if (result > 0) { // insert success
                ApplicationHolder.getContext().getContentResolver().notifyChange(uri, null);
            }
            return ContentUris.withAppendedId(uri,result);
        }
        throw new RuntimeException("fail to insert" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == MATCH_LIVEDATA) {
            int result = db.delete(TABLE_NAME, selection, selectionArgs);
            if (result > 0) { // delete success
                ApplicationHolder.getContext().getContentResolver().notifyChange(UriUtils.withDelete(uri), null);
            }
            return result;
        }
        throw new RuntimeException("fail to delete" + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == MATCH_LIVEDATA) {
            int result = db.update(TABLE_NAME, values, selection, selectionArgs);
            if (result > 0) { // update success
                ApplicationHolder.getContext().getContentResolver().notifyChange(uri, null);
            }
            return result;
        }
        throw new RuntimeException("fail to update" + uri);
    }
}
