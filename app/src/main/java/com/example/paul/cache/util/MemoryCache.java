package com.example.paul.cache.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by paul on 15/12/28.
 */
public class MemoryCache implements ImageCache {
    private LruCache<String,Bitmap> mMemoryCache;
    public MemoryCache(){
        init();
    }

    private void init(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/4;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
    }
    @Override
    public Bitmap get(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        mMemoryCache.put(key,bitmap);
    }
}
