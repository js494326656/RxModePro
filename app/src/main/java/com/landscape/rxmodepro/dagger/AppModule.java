package com.landscape.rxmodepro.dagger;

import android.content.Context;

import com.landscape.rxmodepro.BaseApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/30.
 */
@Module
public class AppModule {
    private BaseApp app;

    public AppModule(BaseApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public BaseApp getApp() {
        return app;
    }

    @Provides
    @Singleton
    public Context getApplication() {
        return app.getApplicationContext();
    }
}
