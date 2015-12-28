package com.example.paul.cache.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by paul on 15/12/28.
 */
public class DiskCache implements ImageCache {
    static String mPath ;
    public DiskCache(Context context){
        init(context);
    }
    private void init(Context context){
        // 获取图片缓存路径
        File cacheDir = getDiskCacheDir(context, "bitmap");
//        File cacheDir = new File(mPath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }
    @Override
    public Bitmap get(String key) {
        File file = new File(mPath+key);
        if (file.exists()){
            return BitmapFactory.decodeFile(mPath+key);
        }
        return null;
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(mPath+key);
            if (!file.exists()){
                Log.e("HPG","FILE not exist");
            }

            fileOutputStream = new FileOutputStream(mPath+key);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
             if (fileOutputStream!=null){
                 try {
                     fileOutputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
        }
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址。
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        mPath = cachePath + File.separator + uniqueName;
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取当前应用程序的版本号。
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
