package com.lazy.testproject1_weather.di.component;

import com.lazy.testproject1_weather.base.BaseApplication;
import com.lazy.testproject1_weather.di.annotataion.ScopeApp;
import com.lazy.testproject1_weather.di.module.AppModule;

import dagger.Component;

/**
 * Created by liming on 2017/6/14.
 */
@ScopeApp
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(BaseApplication baseApplication);
}
