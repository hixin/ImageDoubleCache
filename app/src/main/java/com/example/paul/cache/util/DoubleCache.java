package com.example.paul.cache.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by paul on 15/12/28.
 */
public class DoubleCache implements ImageCache {

    private MemoryCache mMemoryCache = null;
    private DiskCache mDiskCache = null;

    public DoubleCache(Context context){
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
    }
    @Override
    public Bitmap get(String url) {
        String key = MD5.hashKeyForDisk(url)+".jpg";
        Bitmap bitmap = mMemoryCache.get(key);
        if(bitmap==null){
            Log.e("HPG","disk");
            bitmap = mDiskCache.get(key);
        }else {
            Log.e("HPG","memory");
        }
        return bitmap;
     }

    @Override
    public void put(String url, Bitmap bitmap) {
        String key = MD5.hashKeyForDisk(url)+".jpg";
        mMemoryCache.put(key,bitmap);
        mDiskCache.put(key,bitmap);
    }
}
