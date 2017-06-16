package com.lazy.testproject1_weather.mvp.model;

import android.util.Log;

import com.lazy.testproject1_weather.data.remote.ApiServer;
import com.lazy.testproject1_weather.entity.datasupport.City;
import com.lazy.testproject1_weather.entity.datasupport.County;
import com.lazy.testproject1_weather.entity.datasupport.Province;
import com.lazy.testproject1_weather.mvp.contract.AreaContract;

import org.litepal.crud.DataSupport;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

/**
 * Created by liming on 2017/6/15.
 */

public class AreaModel implements AreaContract.Model {


    @Override
    public List<Province> getProvinceList() {
        return DataSupport.findAll(Province.class);
    }

    @Override
    public List<City> getCityList(Province province) {
        // 根据省份编号查询城市信息
        return DataSupport.where("provinceId = ?", String.valueOf(province.getId())).find(City.class);
    }

    @Override
    public List<County> getCountyList(City city) {
        // 根据城市信息查询所有的县信息
        return DataSupport.where("cityId = ?", String.valueOf(city.getId())).find(County.class);
    }

    @Override
    public Flowable<String> queryProvinceFromServer(Retrofit retrofit) {
        try {
            return retrofit.create(ApiServer.class).getProvinceList();
        } catch (Exception e) {
            Log.e("error:", "e:" + e);
            return null;
        }
    }

    @Override
    public Flowable<String> queryCityFromServer(Retrofit retrofit, int provinceCode) {
        try {
            return retrofit.create(ApiServer.class).getCityList(provinceCode);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Flowable<String> queryCountyFromServer(Retrofit retrofit, int provinceCode, int cityCode) {
        try {
            return retrofit.create(ApiServer.class).getCountyList(provinceCode, cityCode);
        } catch (Exception e) {
            return null;
        }
    }

}
