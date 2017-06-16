package com.lazy.testproject1_weather.di.component;

import com.lazy.testproject1_weather.base.BaseApplication;
import com.lazy.testproject1_weather.di.annotataion.ScopeApp;
import com.lazy.testproject1_weather.di.annotataion.WeatherServer;
import com.lazy.testproject1_weather.di.module.ApiServerModule;
import com.lazy.testproject1_weather.di.module.AppModule;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Application桥梁接口
 * <p>
 * Created by liming on 2017/6/14.
 */
@ScopeApp
@Component(modules = {AppModule.class, ApiServerModule.class})
public interface AppComponent {

    void inject(BaseApplication baseApplication);

    @WeatherServer
    Retrofit getRetrofit();
}
