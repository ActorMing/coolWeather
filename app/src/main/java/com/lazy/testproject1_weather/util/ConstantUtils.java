package com.lazy.testproject1_weather.util;

/**
 * 常量工具类
 * <p>
 * Created by liming on 2017/6/14.
 */

public class ConstantUtils {

    private ConstantUtils() {
    }

    /**
     * BaseUrl
     */
    public static final String BASE_URL = "http://guolin.tech/api/";


    /**
     * OkHttpCache缓存文件
     */
    public static final String HTTP_CACHE_FILE_NAME = "okHttpCacheFile";

    /**
     * CacheFileSize
     */
    public static final long CACHE_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 连接超时时间
     */
    public static final long CONNECTION_TIME_OUT = 10;

    /**
     * 读取超时时间
     */
    public static final long READ_TIME_OUT = 10;

    /**
     * 写入超时时间
     */
    public static final long WRITE_TIME_OUT = 10;

    /**
     * 网络连接的三种状态
     */
    public static final int NETWORK_DISABLE = -1; // 网络不可用
    public static final int NETWORK_STATE_WIFI = 0; // wifi
    public static final int NETWORK_STATE_MOBILE = 1; // mobile

    /**
     * 查询省份、城市、县
     */
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
}
