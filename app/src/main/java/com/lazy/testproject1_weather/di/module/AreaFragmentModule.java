package com.lazy.testproject1_weather.di.module;

import com.lazy.testproject1_weather.di.annotataion.ScopeActivity;
import com.lazy.testproject1_weather.di.annotataion.WeatherServer;
import com.lazy.testproject1_weather.mvp.contract.AreaContract;
import com.lazy.testproject1_weather.mvp.model.AreaModel;
import com.lazy.testproject1_weather.mvp.presenter.AreaPresenter;
import com.lazy.testproject1_weather.ui.Fragment.AreaFragment;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * 地区碎片提供类
 * <p>
 * Created by liming on 2017/6/16.
 */


@Module
public class AreaFragmentModule {

    private AreaContract.View view;

    public AreaFragmentModule(AreaFragment areaFragment) {
        this.view = areaFragment;
    }

    @ScopeActivity
    @Provides
    AreaContract.View providerAreaView() {
        return view;
    }

    @ScopeActivity
    @Provides
    AreaContract.Model providerAreaModel() {
        return new AreaModel();
    }

    @ScopeActivity
    @Provides
    AreaContract.Presenter providerAreaPresenter(AreaContract.View view, AreaContract.Model model, @WeatherServer Retrofit retrofit) {
        return new AreaPresenter(view, model, retrofit);
    }
}
