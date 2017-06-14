package com.lazy.testproject1_weather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 工具类
 * <p>
 * Created by liming on 2017/6/14.
 */

public class Utils {

    private Utils() {
    }

    /**
     * 网络是否连接
     *
     * @return
     */
    public static int networkIsConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return ConstantUtils.NETWORK_DISABLE;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return ConstantUtils.NETWORK_STATE_WIFI;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return ConstantUtils.NETWORK_STATE_MOBILE;
                }
            }
        }
        return ConstantUtils.NETWORK_DISABLE;
    }
}
