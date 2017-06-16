package com.lazy.testproject1_weather.util;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava线程调度工具类
 * <p>
 * Created by liming on 2017/6/16.
 */

public class RxJavaSchedulersUtil {

    /**
     * Flowable线程切换
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer io_main() {
        return new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
