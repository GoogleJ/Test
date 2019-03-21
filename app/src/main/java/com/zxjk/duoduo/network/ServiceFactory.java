package com.zxjk.duoduo.network;

import com.zxjk.duoduo.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public  class ServiceFactory {
    private Retrofit retrofit;

    private static ServiceFactory instance;

    private ServiceFactory() {
        //Log拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //构造client对象
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("id", Constant.userId)
                                .header("token", Constant.token)
                                .header("Accept-Language", Constant.language)
                                .header("phoneUuid", Constant.phoneUuid);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        //buildApi
        retrofit = new Retrofit.Builder().baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(BasicConvertFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public <T> T getBaseService(final Class<T> from) {
        return retrofit.create(from);
    }
}