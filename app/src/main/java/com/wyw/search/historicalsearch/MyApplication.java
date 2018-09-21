package com.wyw.search.historicalsearch;

import android.app.Application;

/**
 * wyw
 * Created by Administrator on 2018/9/20.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        if (instance == null)
            instance = new MyApplication();
        return instance;
    }
}
