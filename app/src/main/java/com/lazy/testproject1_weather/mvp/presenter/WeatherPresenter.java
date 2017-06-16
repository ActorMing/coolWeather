package com.lazy.testproject1_weather.mvp.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lazy.testproject1_weather.di.annotataion.WeatherServer;
import com.lazy.testproject1_weather.entity.bean.WeatherBean;
import com.lazy.testproject1_weather.mvp.contract.WeatherContract;
import com.lazy.testproject1_weather.util.RxJavaSchedulersUtil;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * Created by liming on 2017/6/16.
 */

public class WeatherPresenter implements WeatherContract.Presenter {

    private WeatherContract.View view;
    private WeatherContract.Model model;
    private Retrofit retrofit;

    public WeatherPresenter(WeatherContract.View view, WeatherContract.Model model, @WeatherServer Retrofit retrofit) {
        this.view = view;
        this.model = model;
        this.retrofit = retrofit;
    }

    @Override
    public void getWeatherById() {
        view.showProgressDialog();
        Flowable<String> weather = model.getWeather(view.getWeatherId(), retrofit);
        if (weather != null) {
            try {
                weather.compose(RxJavaSchedulersUtil.io_main())
                        .subscribe(new Consumer() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                WeatherBean weatherBean = JSONObject.parseObject(String.valueOf(o), WeatherBean.class);
                                WeatherBean.HeWeatherBean heWeatherBean = weatherBean.getHeWeather().get(0);
                                if (heWeatherBean != null && heWeatherBean.getStatus().equalsIgnoreCase("ok")) {
                                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(view.getActivity()).edit();
                                    edit.putString("weather", String.valueOf(o));
                                    edit.apply();
                                    view.showWeatherInfo(weatherBean);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.e("throwable", "" + throwable);
                            }
                        });
            } catch (Exception e) {
                Log.e("e", "" + e);
            }
        }

        view.dismissDialog();
    }

    /**
     * 获取必应的图片资源
     */
    @Override
    public void getBingPictureWithImageView(final ImageView iv) {
        Flowable<String> bingPic = model.getBingPic(retrofit);
        if (bingPic != null) {
            bingPic.compose(RxJavaSchedulersUtil.io_main())
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            String bingPicPath = String.valueOf(o);
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(view.getActivity()).edit();
                            edit.putString("bing_pic", bingPicPath);
                            edit.apply();
                            Glide.with(view.getActivity())
                                    .load(bingPicPath)
                                    .into(iv);
                        }
                    });
        }
    }
}
