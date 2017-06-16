package com.lazy.testproject1_weather.mvp.contract;

import com.lazy.testproject1_weather.entity.datasupport.City;
import com.lazy.testproject1_weather.entity.datasupport.County;
import com.lazy.testproject1_weather.entity.datasupport.Province;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

/**
 * Created by liming on 2017/6/15.
 */

public interface AreaContract {
    interface Model {

        /**
         * 查询全国所有的省份
         *
         * @return
         */
        List<Province> getProvinceList();

        /**
         * 根据省份信息查询所有的城市
         *
         * @param province
         * @return
         */
        List<City> getCityList(Province province);


        /**
         * 根据城市信息查询所有的县
         *
         * @param city
         * @return
         */
        List<County> getCountyList(City city);

        /**
         * 根据类型级别从服务器获取数据并存入到本地数据库中,所以第二个参数需要传递Retrofit对象
         *
         * @param retrofit
         * @return
         */
        Flowable<String> queryProvinceFromServer(Retrofit retrofit);

        /**
         * 根据省份编号查询所有的城市信息
         *
         * @param retrofit
         * @param provinceCode 省份编号
         * @return
         */
        Flowable<String> queryCityFromServer(Retrofit retrofit, int provinceCode);

        /**
         * 根据城市编号查询所有的县级信息
         *
         * @param retrofit
         * @param provinceCode 省份编号
         * @param cityCode     城市编号
         * @return
         */
        Flowable<String> queryCountyFromServer(Retrofit retrofit, int provinceCode, int cityCode);
    }

    interface View {

        void queryProvinces();

        void queryCities(Province province);

        void queryCounties(City city);

        void showProgressDialog();

        void dismissDialog();
    }

    interface Presenter {

        List<Province> getProvinceList();

        List<City> getCityList(Province province);

        List<County> getCountyList(City city);

        void queryFromServer(int typeLevel, Province province, City city, Integer... integers);
    }
}
