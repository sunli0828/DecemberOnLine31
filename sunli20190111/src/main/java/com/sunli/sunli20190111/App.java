package com.sunli.sunli20190111;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @Author sunli
 * @Data 2019/1/11
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
