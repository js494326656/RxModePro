package com.landscape.rxmodepro;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.landscape.rxmodepro.dagger.AppComponent;
import com.landscape.rxmodepro.dagger.AppModule;
import com.landscape.rxmodepro.dagger.DaggerAppComponent;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BaseApp extends Application {
    private AppComponent mAppComponent;
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLog();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void initLog() {
        Logger.init(getPackageName())
                .methodCount(3)
                .methodOffset(2)
                .logTool(new AndroidLogTool());
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

