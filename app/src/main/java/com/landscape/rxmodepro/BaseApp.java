package com.landscape.rxmodepro;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BaseApp extends Application {

    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLog();
    }

    private void initLog() {
//        Logger.init(getPackageName())
//                .methodCount(3)
//                .methodOffset(2)
//                .logTool(new AndroidLogTool());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

