package com.lazy.testproject1_weather.di.module;

import android.content.Context;

import com.lazy.testproject1_weather.di.annotataion.ScopeApp;

import dagger.Module;
import dagger.Provides;

/**
 * ApplicationModule
 * <p>
 * Created by liming on 2017/6/14.
 */

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    @ScopeApp
    Context providerContext() {
        return mContext;
    }
}
