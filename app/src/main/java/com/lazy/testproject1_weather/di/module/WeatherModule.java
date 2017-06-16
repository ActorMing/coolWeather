package com.lazy.testproject1_weather.di.module;

import com.lazy.testproject1_weather.di.annotataion.ScopeActivity;
import com.lazy.testproject1_weather.di.annotataion.WeatherServer;
import com.lazy.testproject1_weather.mvp.contract.WeatherContract;
import com.lazy.testproject1_weather.mvp.model.WeatherModel;
import com.lazy.testproject1_weather.mvp.presenter.WeatherPresenter;
import com.lazy.testproject1_weather.ui.activity.WeatherActivity;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * 天气对象提供类
 * <p>
 * Created by liming on 2017/6/16.
 */

@Module
public class WeatherModule {

    private WeatherContract.View view;

    public WeatherModule(WeatherActivity view) {
        this.view = view;
    }

    @Provides
    @ScopeActivity
    WeatherContract.View providerWeatherView() {
        return view;
    }

    @Provides
    @ScopeActivity
    WeatherContract.Model providerWeatherModel() {
        return new WeatherModel();
    }

    @Provides
    @ScopeActivity
    WeatherContract.Presenter providerWeatherPresenter(WeatherContract.View view, WeatherContract.Model model, @WeatherServer Retrofit retrofit) {
        return new WeatherPresenter(view, model, retrofit);
    }
}
