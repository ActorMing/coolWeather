package com.lazy.testproject1_weather.data.remote;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 网络Api接口
 * <p>
 * Created by liming on 2017/6/14.
 */

public interface ApiServer {

    /**
     * 查询所有的全国的省份集合
     *
     * @return
     */
    @GET("china")
    Flowable<String> getProvinceList();

    /**
     * 根据省份编号查询所有的城市
     *
     * @param provinceCode 省份编号
     * @return
     */
    @GET("china/{provinceCode}")
    Flowable<String> getCityList(@Path("provinceCode") int provinceCode);

    /**
     * 根据省份编号和城市编号查询所有的县级信息
     *
     * @param provinceCode 省份编号
     * @param cityCode     城市编号
     * @return
     */
    @GET("china/{provinceCode}/{cityCode}")
    Flowable<String> getCountyList(@Path("provinceCode") int provinceCode, @Path("cityCode") int cityCode);
}
