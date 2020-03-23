package com.baimaisu.process;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.baimaisu.process.convert.ConverterFactory;
import com.baimaisu.process.processor.BaseContentProcessor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CrossLiveData<T> extends MutableLiveData<T> {
    //    private static final Uri URI = Uri.parse("content://com.oppo.usercenter.provider.open/DBAccountStatusEntity");
    private Uri getUri() {
        return Uri.withAppendedPath(Uri.parse(BaseContentProcessor.getBaseUriPath()), getIdentifier());
    }


    private String identifier;
    private boolean isRecycled;
    private List<RecycleCallBack> recycleCallBacks = new ArrayList<>();
    private Class<?> valueClass;


    private MyContentObserver myContentObserver = null;

    public CrossLiveData<T> toSubProcess() {
        if(isSubProcess){
            throw new RuntimeException("toSubProcess only is allowed call once");
        }
        isSubProcess = true;
        myContentObserver = new MyContentObserver(new Handler(Looper.getMainLooper()));
        ApplicationHolder.getContext().getContentResolver().registerContentObserver(getUri(), true, myContentObserver);
        return this;
    }


    class MyContentObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d("MyContentObserver", "onChange: " + uri);
            if(UriUtils.isDeleteUri(uri)){
                Log.d("MyContentObserver", "delete: " + uri+ ",recycle");
                recycle();
                return;
            }
            T data = restoreData();
            setValue(data);
        }
    }

    private boolean isSubProcess = false;

    @Override
    public void setValue(T value) {
        super.setValue(value);
        if(isRecycled){
            throw new RuntimeException("cannot setValue after recycled");
        }
        notifyRemoteProcessContentChange();
    }


    private void notifyRemoteProcessContentChange() {
        if (!isSubProcess) {
            saveData();
        }
    }

    private void saveData() {
        T value = getValue();
        String json = ConverterFactory.get().toJson(value);
        insertOrUpdateProvider(json);
    }

    private void insertOrUpdateProvider(String json) {
        ContentValues contentValues = BaseContentProcessor.createContentValues(getIdentifier(), json);
        ContentResolver contentResolver = ApplicationHolder.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(getUri(), new String[]{BaseContentProcessor.COLUMN_LIVE_KEY, BaseContentProcessor.COLUMN_LIVE_DATA}, BaseContentProcessor.COLUMN_LIVE_KEY + " =?", new String[]{getIdentifier()}, null);
        if (cursor != null && cursor.getCount() > 0) {
            contentResolver.update(getUri(), contentValues, BaseContentProcessor.COLUMN_LIVE_KEY + " =?", new String[]{getIdentifier()});
        } else {
            contentResolver.insert(getUri(), contentValues);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private T restoreData() {
        T data = null;
        Cursor cursor = ApplicationHolder.getContext().getContentResolver().query(getUri(), new String[]{BaseContentProcessor.COLUMN_LIVE_KEY, BaseContentProcessor.COLUMN_LIVE_DATA}, BaseContentProcessor.COLUMN_LIVE_KEY + " =?", new String[]{getIdentifier()}, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String str = cursor.getString(cursor.getColumnIndex(BaseContentProcessor.COLUMN_LIVE_DATA));
            //noinspection unchecked
            data = (T) ConverterFactory.get().fromJson(str, valueClass);
        }
        if (cursor != null) {
            cursor.close();
        }
        return data;
    }


    public synchronized void recycle() {
        if (!isRecycled) {
            if (isSubProcess) {
                recycleContentObserver();
            } else {
                recycleSelf();
            }
            isRecycled = true;
            for(RecycleCallBack recycleCallBack : recycleCallBacks){
                recycleCallBack.onRecycle(this);
            }
        }
    }

    private void recycleSelf(){
        ApplicationHolder.getContext().getContentResolver().delete(getUri(), BaseContentProcessor.COLUMN_LIVE_KEY + " =?", new String[]{getIdentifier()});
    }



    private void recycleContentObserver() {
        if (myContentObserver != null) {
            ApplicationHolder.getContext().getContentResolver().unregisterContentObserver(myContentObserver);
            myContentObserver = null;
        }
    }

    public boolean isRecycled() {
        return isRecycled;
    }

    private String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public interface RecycleCallBack {
        void onRecycle(CrossLiveData crossLiveData);
    }

    public void addRecycleCallBack(RecycleCallBack recycleCallBack) {
        this.recycleCallBacks.add(recycleCallBack);
    }

    public void setValueClass(Type clazz) {
        this.valueClass = (Class<?>) clazz;
    }
}
