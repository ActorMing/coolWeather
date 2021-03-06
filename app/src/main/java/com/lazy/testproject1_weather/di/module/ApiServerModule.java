package com.lazy.testproject1_weather.di.module;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lazy.testproject1_weather.di.annotataion.NetWork;
import com.lazy.testproject1_weather.di.annotataion.NoNetWork;
import com.lazy.testproject1_weather.di.annotataion.ScopeApp;
import com.lazy.testproject1_weather.di.annotataion.WeatherServer;
import com.lazy.testproject1_weather.util.ConstantUtils;
import com.lazy.testproject1_weather.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * ApiServerModule
 * <p>
 * Created by liming on 2017/6/14.
 */

@Module
public class ApiServerModule {

    /**
     * 1. File cacheFile
     * 2. Cache cache
     * 3. Interceptor interceptor
     * 4. Interceptor noNetWorkInterceptor
     * 5. OkHttpClient okHttpClient
     * 6. Retrofit retrofit
     */

    /**
     * 为下面的缓存对象提供缓存文件
     *
     * @param context AppModule提供过来的全局的Context
     * @return 缓存文件
     */
    @Provides
    @ScopeApp
    File providerCacheFile(Context context) {
        return new File(context.getCacheDir(), ConstantUtils.HTTP_CACHE_FILE_NAME);
    }

    /**
     * 为下面的OkHttpClient提供缓存对象
     *
     * @param cacheFile AppModule提供过来的全局的Context
     * @return 缓存对象
     */
    @Provides
    @ScopeApp
    Cache providerCache(File cacheFile) {
        return new Cache(cacheFile, ConstantUtils.CACHE_FILE_SIZE);
    }

    /**
     * 在有网络的情况下，先去读缓存，设置的缓存时间到了，在去网络获取
     *
     * @return 网络拦截器
     */
    @NetWork
    @Provides
    @ScopeApp
    Interceptor providerNetWorkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                /**
                 *  1. 获取 Request 对象
                 *  2. 获取 Chain 对象  Chain继续发送请求
                 *  3. 判断网络是否可用
                 *      3.1 网络可用设置get方式的缓存
                 */
                Request request = chain.request();

                Response response = chain.proceed(request);

                // 网络状态大于Disable说明可以使用,需要进入缓存
                if (Utils.networkIsConnected() > ConstantUtils.NETWORK_DISABLE) {
                    int maxAge = 60; // 缓存失效时间,单位为秒
                    return response.newBuilder()
                            .removeHeader("Pragma")// 清除头信息(Pragma)，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                }
                return response;
            }
        };
    }

    /**
     * 在没有网络的情况下，取读缓存数据
     *
     * @return 无网络的拦截器
     */
    @NoNetWork
    @Provides
    @ScopeApp
    Interceptor providerNoNetWorkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                /**
                 *  1. 获取Request对象
                 *  2. 获取Response对象
                 *  3. 没有网络直接去缓存中获取数据
                 */
                Request request = chain.request();


                Response response = chain.proceed(request);

                // 没有网络取缓存数据
                if (Utils.networkIsConnected() <= ConstantUtils.NETWORK_DISABLE) {
                    int maxStale = 60; // // 缓存失效时间,单位为秒
                    return response.newBuilder()
                            .removeHeader("Pragma")// 清除头信息(Pragma)，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .addHeader("Cache-Control", "public, only-if-cache, max-stale=" + maxStale)
                            .build();
                }
                return response;
            }
        };
    }

    /**
     * 为下面的Retrofit提供OkHttpClient对象
     *
     * @param cache                 上面提供的缓存对象
     * @param networkInterceptor    上面提供的网络拦截器
     * @param notNetWorkInterceptor 上面提供的无网络拦截器
     * @return OkHttpClient
     */
    @Provides
    @ScopeApp
    OkHttpClient providerOkHttpClient(Cache cache, @NetWork Interceptor networkInterceptor, @NoNetWork Interceptor notNetWorkInterceptor) {

        /**
         * 1. 设置连接超时时间
         * 2. 设置读取超时时间
         * 3. 设置写入超时时间
         * 4. 设置网络缓存拦截器
         * 5. 设置无网络的拦截器
         * 6. 设置缓存文件
         * 7. 设置失败后是否从新连接
         */

        return new OkHttpClient.Builder()
                .connectTimeout(ConstantUtils.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(ConstantUtils.READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(ConstantUtils.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(networkInterceptor)
                .addInterceptor(notNetWorkInterceptor)
                .cache(cache)
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * 提供一个全局唯一的Retrofit对象,方便外部进行网络数据访问
     *
     * @param okHttpClient 上面提供的OkHttpClient对象
     * @return Retrofit
     */
    @WeatherServer
    @Provides
    @ScopeApp
    Retrofit providerRetrofit(OkHttpClient okHttpClient) {

        /**
         *  1. 设置BaseUrl
         *  2. 设置OkHttpClient
         *  3. 设置回调适配工厂对象RxJavaAdapterFactory
         *  4. 设置数据转换工厂对象GsonConverterFactory 或者使用 FastJsonConverterFactory
         *  5. 设置字符串转换工厂对象
         */

        return new Retrofit.Builder()
                .baseUrl(ConstantUtils.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }
}
