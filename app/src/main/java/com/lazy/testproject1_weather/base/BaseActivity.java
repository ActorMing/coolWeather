package com.lazy.testproject1_weather.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseActivity
 * <p>
 * Created by liming on 2017/6/14.
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * ActivityList
     */
    private static List<Activity> activityList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }

    /**
     * 当App退出的时候移除所有的Activity
     */
    public static void removeAllActivity() {
        if (!activityList.isEmpty()) {
            for (Activity activity : activityList) {
                if (activity != null && !activity.isFinishing()) {
                    activityList.remove(activity);
                    activity.finish();
                }
            }
        }
    }
}
