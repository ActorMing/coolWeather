package com.lazy.testproject1_weather.base;

import android.app.Application;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * BaseApplication
 * <p>
 * Created by liming on 2017/6/14.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);
    }
}
