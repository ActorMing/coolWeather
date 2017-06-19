package com.lazy.testproject1_weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.lazy.testproject1_weather.base.BaseApplication;
import com.lazy.testproject1_weather.data.remote.ApiServer;
import com.lazy.testproject1_weather.di.annotataion.WeatherServer;
import com.lazy.testproject1_weather.di.component.DaggerAutoUpdateComponent;
import com.lazy.testproject1_weather.entity.bean.WeatherBean;
import com.lazy.testproject1_weather.util.ConstantUtils;
import com.lazy.testproject1_weather.util.RxJavaSchedulersUtil;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * 自动更新天气的后台服务
 * <p>
 * Created by liming on 2017/6/19.
 */

public class AutoUpdateService extends Service {

    @Inject
    @WeatherServer
    Retrofit retrofit;

    public AutoUpdateService() {
        initInject();
    }

    private void initInject() {
        DaggerAutoUpdateComponent.builder()
                .appComponent(BaseApplication.get(BaseApplication.getAppContext()).getAppComponent())
                .build()
                .inject(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int anMinute = 1 * 60 * 1000; //这是1分钟的毫秒数
        long taggerAtTime = SystemClock.elapsedRealtime() + anMinute;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, taggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr = preferences.getString("weather", null);
        if (!TextUtils.isEmpty(weatherStr)) {
            WeatherBean weatherBean = JSONObject.parseObject(weatherStr, WeatherBean.class);
            String weatherId = weatherBean.getHeWeather().get(0).getBasic().getId();
            Flowable<String> weather = retrofit.create(ApiServer.class).getWeather(weatherId, ConstantUtils.APP_KEY);
            weather.compose(RxJavaSchedulersUtil.io_main())
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            WeatherBean weatherBean1 = JSONObject.parseObject(String.valueOf(o), WeatherBean.class);
                            if (weatherBean1 != null && weatherBean1.getHeWeather() != null
                                    && weatherBean1.getHeWeather().get(0) != null
                                    && weatherBean1.getHeWeather().get(0).getStatus().equalsIgnoreCase("ok")) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                                editor.putString("weather", String.valueOf(o));
                                editor.apply();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
        }
    }

    /**
     * 更新必应图片
     */
    private void updateBingPic() {
        Flowable<String> bingPicture = retrofit.create(ApiServer.class).getBingPicture();
        bingPicture.compose(RxJavaSchedulersUtil.io_main())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("bing_pic", String.valueOf(o));
                        editor.apply();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }
}
