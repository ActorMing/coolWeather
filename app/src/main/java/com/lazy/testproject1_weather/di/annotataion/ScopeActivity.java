package com.lazy.testproject1_weather.di.annotataion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Activity作用域
 * <p>
 * Created by liming on 2017/6/16.
 */

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ScopeActivity {
}
