package com.sunli.sunli20190111;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {

    private SimpleDraweeView icon_banner;
    private TextView text_news;
    private RecyclerView recyclerview_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        icon_banner = findViewById(R.id.activity_main_icon_banner);
        text_news = findViewById(R.id.activity_main_text_news);
        recyclerview_news = findViewById(R.id.activity_main_recyclerview_news);
    }
}
