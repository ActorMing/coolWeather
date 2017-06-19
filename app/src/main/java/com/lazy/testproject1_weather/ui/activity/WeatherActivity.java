package com.lazy.testproject1_weather.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lazy.testproject1_weather.R;
import com.lazy.testproject1_weather.base.BaseActivity;
import com.lazy.testproject1_weather.base.BaseApplication;
import com.lazy.testproject1_weather.di.component.DaggerWeatherComponent;
import com.lazy.testproject1_weather.di.module.WeatherModule;
import com.lazy.testproject1_weather.entity.bean.WeatherBean;
import com.lazy.testproject1_weather.mvp.contract.WeatherContract;

import java.util.List;

import javax.inject.Inject;

public class WeatherActivity extends BaseActivity implements WeatherContract.View {

    private Context mContext;

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipe_layout_weather;

    private ScrollView scrollViewWeather;

    // 城市、更新时间、度数、天气信息、空气指数、pm2.5、舒适度、洗车、运动指数
    private TextView tvCity, tvUpdateTime, tvDegree, tvWeatherInfo, tvAqi, tvPm25, tvComfort, tvWashCar, tvSport;

    private LinearLayout linearLayoutForecast; // 预测天气布局

    private Button btnNavigation;

    private ImageView ivBingPic;

    private String weatherId = null;

    private ProgressDialog progressDialog;

    @Inject
    WeatherContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        initInject();

        initViews();
    }

    private void initInject() {
        DaggerWeatherComponent.builder()
                .appComponent(BaseApplication.get(this).getAppComponent())
                .weatherModule(new WeatherModule(this))
                .build()
                .inject(this);
    }

    private void initViews() {
        mContext = this;
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_weather);
        swipe_layout_weather = (SwipeRefreshLayout) findViewById(R.id.swipe_layout_weather);
        scrollViewWeather = (ScrollView) findViewById(R.id.sv_weather);
        tvCity = (TextView) findViewById(R.id.tv_title_city);
        tvUpdateTime = (TextView) findViewById(R.id.tv_update_time);
        tvDegree = (TextView) findViewById(R.id.tv_degree);
        tvWeatherInfo = (TextView) findViewById(R.id.tv_weather_info);
        tvAqi = (TextView) findViewById(R.id.tv_aqi);
        tvPm25 = (TextView) findViewById(R.id.tv_pm25);
        tvComfort = (TextView) findViewById(R.id.tv_comfort);
        tvWashCar = (TextView) findViewById(R.id.tv_wash_car);
        tvSport = (TextView) findViewById(R.id.tv_wash_car);
        btnNavigation = (Button) findViewById(R.id.btn_nav);
        linearLayoutForecast = (LinearLayout) findViewById(R.id.linear_layout_forecast);
        ivBingPic = (ImageView) findViewById(R.id.iv_bing_pic);


        initListener();
    }

    private void initListener() {
        swipe_layout_weather.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bing_pic = defaultSharedPreferences.getString("bing_pic", null);
        if (!TextUtils.isEmpty(bing_pic)) {
            Glide.with(this)
                    .load(bing_pic)
                    .into(ivBingPic);
        } else {
            presenter.getBingPictureWithImageView(ivBingPic);
        }
        String weatherStr = defaultSharedPreferences.getString("weather", null);
        if (!TextUtils.isEmpty(weatherStr)) {  // 有数据缓存直接从缓存中获取json数据
            WeatherBean weatherBean = JSONObject.parseObject(weatherStr, WeatherBean.class);
            weatherId = weatherBean.getHeWeather().get(0).getBasic().getId();
            showWeatherInfo(weatherBean);
        } else { // 无缓存去网络请求数据
            weatherId = getIntent().getStringExtra("weather_id");
            scrollViewWeather.setVisibility(View.INVISIBLE);
            presenter.getWeatherById();
        }

        // 下拉刷新
        swipe_layout_weather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(null);
            }
        });

        if (scrollViewWeather != null) {
            scrollViewWeather.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (swipe_layout_weather != null) {
                        swipe_layout_weather.setEnabled(scrollViewWeather.getScrollY() == 0);
                    }
                }
            });
        }

        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    public void refreshData(String weatherId) {
        if (!TextUtils.isEmpty(weatherId)) {
            this.weatherId = weatherId;
        }
        presenter.getWeatherById();
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showWeatherInfo(WeatherBean weatherBean) {
        WeatherBean.HeWeatherBean heWeatherBean = weatherBean.getHeWeather().get(0);
        if (heWeatherBean == null) {
            dismissDialog();
            Toast.makeText(mContext, "获取天气信息失败", Toast.LENGTH_SHORT).show();
            return;
        }
        String cityName = heWeatherBean.getBasic().getCity();
        String updateTime = heWeatherBean.getBasic().getUpdate().getLoc().split(" ")[1];
        String degree = heWeatherBean.getNow().getTmp() + "°C";
        String weatherInfo = heWeatherBean.getNow().getCond().getTxt();
        tvCity.setText(cityName);
        tvUpdateTime.setText(updateTime);
        tvDegree.setText(degree);
        tvWeatherInfo.setText(weatherInfo);

        linearLayoutForecast.removeAllViews();
        List<WeatherBean.HeWeatherBean.DailyForecastBean> daily_forecast = heWeatherBean.getDaily_forecast();
        for (WeatherBean.HeWeatherBean.DailyForecastBean dailyForecastBean : daily_forecast) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.forecast_item, linearLayoutForecast, false);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
            TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
            TextView tvMaxTemp = (TextView) view.findViewById(R.id.tv_max_temperature);
            TextView tvMinTemp = (TextView) view.findViewById(R.id.tv_min_temperature);
            tvDate.setText(dailyForecastBean.getDate());
            tvInfo.setText(dailyForecastBean.getCond().getTxt_d());
            tvMaxTemp.setText(dailyForecastBean.getTmp().getMax());
            tvMinTemp.setText(dailyForecastBean.getTmp().getMin());
            linearLayoutForecast.addView(view);
        }

        if (heWeatherBean.getAqi() != null) {
            tvAqi.setText(heWeatherBean.getAqi().getCity().getAqi());
            tvPm25.setText(heWeatherBean.getAqi().getCity().getPm25());
        }
        String comfort = "舒适度: " + heWeatherBean.getSuggestion().getComf().getTxt();
        String carWash = "洗车指数: " + heWeatherBean.getSuggestion().getCw().getTxt();
        String sport = "运动建议: " + heWeatherBean.getSuggestion().getSport().getTxt();
        tvComfort.setText(comfort);
        tvWashCar.setText(carWash);
        tvSport.setText(sport);
        scrollViewWeather.setVisibility(View.VISIBLE);
    }

    @Override
    public String getWeatherId() {
        return weatherId;
    }

    @Override
    public Activity getActivity() {
        return WeatherActivity.this;
    }

    @Override
    public void stopSwipeRefresh() {
        swipe_layout_weather.setRefreshing(false);
    }
}
