package com.lazy.testproject1_weather.di.module;

import com.lazy.testproject1_weather.service.AutoUpdateService;

import dagger.Module;

/**
 * 自动更新Module
 * <p>
 * Created by liming on 2017/6/19.
 */

@Module
public class AutoUpdateModule {

    private AutoUpdateService autoUpdateService;

    public AutoUpdateModule(AutoUpdateService autoUpdateService) {
        this.autoUpdateService = autoUpdateService;
    }

}
