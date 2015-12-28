package com.example.paul.cache.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by paul on 15/12/28.
 */
public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();
    private static ImageLoader sInstance;
    private DoubleCache mDoubleCache = null;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ImageLoader(Context context) {
        mDoubleCache = new DoubleCache(context);
    }

    public static ImageLoader getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                sInstance = new ImageLoader(context);
            }
        }
        return sInstance;
    }

    public void displayImage(String url, ImageView imageView) {
        Bitmap bitmap = mDoubleCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            mDoubleCache.put(url,bitmap);
            return;
        }
        submitLoadRequest(url, imageView);
    }

    private void submitLoadRequest(final String url, final ImageView imageView) {
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
                mDoubleCache.put(url, bitmap);
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            mDoubleCache.put(url,bitmap);
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
}
