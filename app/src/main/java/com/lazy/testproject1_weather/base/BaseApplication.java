package com.lazy.testproject1_weather.base;

import android.app.Application;
import android.content.Context;

import com.lazy.testproject1_weather.di.component.AppComponent;
import com.lazy.testproject1_weather.di.component.DaggerAppComponent;
import com.lazy.testproject1_weather.di.module.AppModule;

import org.litepal.LitePal;

/**
 * BaseApplication
 * <p>
 * Created by liming on 2017/6/14.
 */

public class BaseApplication extends Application {


    private static Context mContext;

    /**
     * App桥梁接口
     */
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);

        if (mContext == null) {
            mContext = BaseApplication.this;
        }

        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }
}
