package com.example.paul.cache.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.paul.cache.util.libcore.DiskCache;
import com.example.paul.cache.util.libcore.ImageCache;
import com.example.paul.cache.util.libcore.MemoryCache;

/**
 * 双缓存操作类
 */
public class DoubleCache implements ImageCache {
    private static final String TAG = "DoubleCache";
    private MemoryCache mMemoryCache = null;
    private DiskCache mDiskCache = null;

    public DoubleCache(Context context){
        if (mMemoryCache != null && mDiskCache != null) {
            return;
        }
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
    }
    @Override
    public Bitmap get(String url) {
        String key = url2Key(url);
        Bitmap bitmap = mMemoryCache.get(key);
        if(bitmap == null){
            bitmap = mDiskCache.get(key);
        }else {
        }
        return bitmap;
     }

    @Override
    public void put(String url, Bitmap bitmap) {
        String key = url2Key(url);
        mMemoryCache.put(key,bitmap);
        mDiskCache.put(key,bitmap);
    }

    //url转key
    private String url2Key(String url){
        String key = MD5.hashKeyForDisk(url)+".jpg";
        return key;
    }
}
