package com.example.paul.cache.util.libcore;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * 内存缓存使用lru算法处理
 *
 */

public class MemoryCache implements ImageCache {
    private static final String TAG = "MemoryCache";
    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCache() {
        init();
    }

    private void init() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.i(TAG, "init maxMemory: " + maxMemory);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap!=null){
            Log.i(TAG,"File is exist in memory");
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        if (get(url) == null) {
            mMemoryCache.put(url, bitmap);
        }
    }
}
