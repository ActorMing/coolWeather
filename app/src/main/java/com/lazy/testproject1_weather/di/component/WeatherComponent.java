package com.lazy.testproject1_weather.di.component;

import com.lazy.testproject1_weather.di.annotataion.ScopeActivity;
import com.lazy.testproject1_weather.di.module.WeatherModule;
import com.lazy.testproject1_weather.ui.activity.WeatherActivity;

import dagger.Component;

/**
 * Created by liming on 2017/6/16.
 */
@ScopeActivity
@Component(modules = WeatherModule.class, dependencies = AppComponent.class)
public interface WeatherComponent {

    void inject(WeatherActivity weatherActivity);
}
