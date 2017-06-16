package com.lazy.testproject1_weather.mvp.presenter;

import android.util.Log;

import com.lazy.testproject1_weather.di.annotataion.WeatherServer;
import com.lazy.testproject1_weather.entity.datasupport.City;
import com.lazy.testproject1_weather.entity.datasupport.County;
import com.lazy.testproject1_weather.entity.datasupport.Province;
import com.lazy.testproject1_weather.mvp.contract.AreaContract;
import com.lazy.testproject1_weather.util.ConstantUtils;
import com.lazy.testproject1_weather.util.RxJavaSchedulersUtil;
import com.lazy.testproject1_weather.util.Utility;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * Created by liming on 2017/6/15.
 */

public class AreaPresenter implements AreaContract.Presenter {

    private AreaContract.View mView;
    private AreaContract.Model mModel;
    private Retrofit retrofit;

    public AreaPresenter(AreaContract.View mView, AreaContract.Model mModel, @WeatherServer Retrofit retrofit) {
        this.mView = mView;
        this.mModel = mModel;
        this.retrofit = retrofit;
    }

    @Override
    public List<Province> getProvinceList() {
        return mModel.getProvinceList();
    }

    @Override
    public List<City> getCityList(Province province) {
        return mModel.getCityList(province);
    }

    @Override
    public List<County> getCountyList(City city) {
        return mModel.getCountyList(city);
    }

    @Override
    public void queryFromServer(final int typeLevel, final Province province, final City city, Integer... integers) {
        mView.showProgressDialog();
        switch (typeLevel) {
            case ConstantUtils.LEVEL_PROVINCE:
                Flowable<String> stringFlowable = mModel.queryProvinceFromServer(retrofit);
                if (stringFlowable != null) {
                    stringFlowable.compose(RxJavaSchedulersUtil.io_main())
                            .subscribe(new Consumer() {
                                @Override
                                public void accept(@NonNull Object o) throws Exception {
                                    if (Utility.handleProvinceResponse(String.valueOf(o))) {
                                        mView.queryProvinces();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    Log.e("throwable", String.valueOf(throwable));
                                }
                            });
                } else {
                    Log.e("throwable", "result is null");
                }
                mView.dismissDialog();
                break;
            case ConstantUtils.LEVEL_CITY:
                Flowable<String> stringFlowable1 = mModel.queryCityFromServer(retrofit, integers[0]);
                if (stringFlowable1 != null) {
                    stringFlowable1.compose(RxJavaSchedulersUtil.io_main())
                            .subscribe(new Consumer() {
                                @Override
                                public void accept(@NonNull Object o) throws Exception {
                                    if (Utility.handleCityResponse(String.valueOf(o), province.getId())) {
                                        mView.queryCities(province);
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    Log.e("throwable", String.valueOf(throwable));
                                }
                            });
                }
                mView.dismissDialog();
                break;
            case ConstantUtils.LEVEL_COUNTY:
                Flowable<String> stringFlowable2 = mModel.queryCountyFromServer(retrofit, integers[0], integers[1]);
                stringFlowable2.compose(RxJavaSchedulersUtil.io_main())
                        .subscribe(new Consumer() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                if (Utility.handleCountryResponse(String.valueOf(o), city.getId())) {
                                    mView.queryCounties(city);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.e("throwable", String.valueOf(throwable));
                            }
                        });
                mView.dismissDialog();
                break;
        }
    }


}
