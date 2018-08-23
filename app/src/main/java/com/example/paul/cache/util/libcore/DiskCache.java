package com.example.paul.cache.util.libcore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskCache implements ImageCache {
    private static final String TAG = "DiskCache";

    static String mPath ;
    public DiskCache(Context context){
        init(context);
    }
    private void init(Context context){
        // 获取图片缓存路径
        mPath = getDiskCachePath(context,"bitmap");
        File cacheDir = new File(mPath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    @Override
    public Bitmap get(String key) {
        return BitmapFactory.decodeFile(mPath + key);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(mPath + key);
            if (file.exists()){
                Log.i(TAG,"File is exist on disk");
            }
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            closeQuietly(fileOutputStream);
        }
    }

    /**
     * 根据传入的dir获得路径
     * @param context
     * @param dir
     * @return
     */
    public String getDiskCachePath(Context context, String dir) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + dir + File.separator;
    }

    public static void closeQuietly(Closeable closeable){
        if (null!=closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
