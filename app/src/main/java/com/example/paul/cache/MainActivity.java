package com.example.paul.cache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.example.paul.cache.util.DoubleCache;
import com.example.paul.cache.util.ImageLoader;

public class MainActivity extends AppCompatActivity {
    ImageLoader loader;
    private ImageView imageView;
    private ImageView imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        imageView2 = findViewById(R.id.image2);
        ImageLoader.Builder builder = new ImageLoader.Builder(MainActivity.this);
        builder.setImageCache(new DoubleCache(MainActivity.this));
        loader = builder.create();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.displayImage("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg", imageView);
            }
        });
        loader.displayImage("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg", imageView);
    }
}
