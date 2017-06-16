package com.lazy.testproject1_weather.mvp.contract;

import android.app.Activity;
import android.widget.ImageView;

import com.lazy.testproject1_weather.entity.bean.WeatherBean;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

/**
 * Created by liming on 2017/6/16.
 */

public interface WeatherContract {
    interface Model {

        /**
         * 根据WeatherId查询天气情况
         *
         * @param weatherId
         * @return
         */
        Flowable<String> getWeather(String weatherId, Retrofit retrofit);

        /**
         * 获取必应图片
         *
         * @return
         */
        Flowable<String> getBingPic(Retrofit retrofit);
    }

    interface View {

        void showProgressDialog();

        void dismissDialog();

        /**
         * 显示天气信息
         *
         * @param weatherBean
         */
        void showWeatherInfo(WeatherBean weatherBean);

        /**
         * 获取天气编号
         *
         * @return
         */
        String getWeatherId();

        Activity getActivity();
    }

    interface Presenter {

        /**
         * 根据编号查询天气详情
         */
        void getWeatherById();

        void getBingPictureWithImageView(ImageView iv);
    }
}
