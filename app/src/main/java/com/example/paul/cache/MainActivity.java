package com.example.paul.cache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.paul.cache.util.ImageLoader;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageLoader.getInstance(MainActivity.this).displayImage("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg", imageView);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageLoader.getInstance(MainActivity.this).displayImage("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg", imageView2);
            }
        });
    }
}
