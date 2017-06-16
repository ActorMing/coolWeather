package com.lazy.testproject1_weather.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lazy.testproject1_weather.entity.datasupport.City;
import com.lazy.testproject1_weather.entity.datasupport.County;
import com.lazy.testproject1_weather.entity.datasupport.Province;


/**
 * 用于解析省市县的工具类
 * <p>
 * Created by liming on 2017/6/15.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的升级数据
     *
     * @param response 返回内容
     * @return
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = (JSONArray) JSONArray.parse(response);
                for (int i = 0; i < allProvinces.size(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInteger("id"));
                    province.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = (JSONArray) JSONArray.parse(response);
                for (int i = 0; i < allCities.size(); i++) {
                    JSONObject jsonObject = (JSONObject) allCities.get(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInteger("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountryResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = (JSONArray) JSONArray.parse(response);
                for (int i = 0; i < allCounties.size(); i++) {
                    JSONObject jsonObject = (JSONObject) allCounties.get(i);
                    County county = new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
