package com.example.paul.cache.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.paul.cache.util.libcore.ImageCache;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    
    public ImageCache mCache ;
    public int mLoadingImageId ;
    public int mErrorImageId;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ImageLoader(Context context) {
        mCache = new DoubleCache(context);
    }
    
    public void displayImage(String url, ImageView imageView) {
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        submitLoadRequest(url, imageView);
    }

    private void submitLoadRequest(final String url, final ImageView imageView) {
        Log.i(TAG,"Download,url:"+url);
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = downloadImage(url);
                if (imageView.getTag().equals(url)) {

                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                    
                }
                mCache.put(url, bitmap);//每次都要更新
            }
        });
    }
    
    public Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            if (bitmap!=null){
                mCache.put(url, bitmap);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }


    public static class Builder{
        Context context;
        private ImageCache mCache ;
        private int mLoadingImageId ;
        private int mErrorImageId;

        public Builder(Context context){
            this.context = context;
        }
        public Builder setImageCache(ImageCache cache){
            mCache = cache;
            return this;
        }

        public Builder setErrorImageId(int resId){
            mErrorImageId = resId;
            return this;
        }
        public Builder setLoadingImageId(int resId){
            mLoadingImageId = resId;
            return this;
        }

        private void applyConfig(ImageLoader loader) {
            loader.mCache = mCache;
            loader.mErrorImageId = mErrorImageId;
            loader.mLoadingImageId = mLoadingImageId;
        }

        public ImageLoader create(){
            ImageLoader loader = new ImageLoader(context);
            applyConfig(loader);
            return loader;
        }
    }
}
