package com.lazy.testproject1_weather.di.component;

import com.lazy.testproject1_weather.di.annotataion.ScopeActivity;
import com.lazy.testproject1_weather.di.module.AreaFragmentModule;
import com.lazy.testproject1_weather.ui.Fragment.AreaFragment;

import dagger.Component;

/**
 * Created by liming on 2017/6/16.
 */

@ScopeActivity
@Component(modules = AreaFragmentModule.class, dependencies = AppComponent.class)
public interface AreaFragmentComponent {

    void inject(AreaFragment areaFragment);
}
