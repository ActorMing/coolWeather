package com.lazy.testproject1_weather.di.component;

import com.lazy.testproject1_weather.di.annotataion.ScopeActivity;
import com.lazy.testproject1_weather.di.module.AutoUpdateModule;
import com.lazy.testproject1_weather.service.AutoUpdateService;

import dagger.Component;

/**
 * Created by liming on 2017/6/19.
 */

@ScopeActivity
@Component(modules = AutoUpdateModule.class, dependencies = AppComponent.class)
public interface AutoUpdateComponent {

    void inject(AutoUpdateService autoUpdateService);
}
