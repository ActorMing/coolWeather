package com.lazy.testproject1_weather.mvp.model;

import com.lazy.testproject1_weather.data.remote.ApiServer;
import com.lazy.testproject1_weather.mvp.contract.WeatherContract;
import com.lazy.testproject1_weather.util.ConstantUtils;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

/**
 * Created by liming on 2017/6/16.
 */

public class WeatherModel implements WeatherContract.Model {

    @Override
    public Flowable<String> getWeather(String weatherId, Retrofit retrofit) {
        try {
            return retrofit.create(ApiServer.class).getWeather(weatherId, ConstantUtils.APP_KEY);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Flowable<String> getBingPic(Retrofit retrofit) {
        try {
            return retrofit.create(ApiServer.class).getBingPicture();
        } catch (Exception e) {
            return null;
        }
    }
}
