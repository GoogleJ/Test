package com.zxjk.duoduo.network;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private Retrofit mRetrofit;
    private static OkHttpClient okHttpClient;
    private static RetrofitManager mRetrofitManager;
    private static final int DEFAULT_TIME_OUT = 60;
    private static HttpLoggingInterceptor loggingInterceptor;

    static {
        initOkHttpClient();
    }

    private RetrofitManager() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://dm-51.data.aliyun.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitManager getInstance() {
        if (mRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager();
                }
            }
        }
        return mRetrofitManager;
    }

    /**
     * 获取单例OkHttpClient
     *
     * @return
     */
    @SuppressWarnings("deprecated")
    private static OkHttpClient initOkHttpClient() {
        loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", "retrofitBack = " + message);
            }
        });
        //日志等级
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            //打印拦截器日志
                            .addNetworkInterceptor(loggingInterceptor)
                            //设置连接超时时间
                            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                            //设置读取超时时间
                            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                            // 设置写入超时时间
                            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    public <T> T create(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }

}
