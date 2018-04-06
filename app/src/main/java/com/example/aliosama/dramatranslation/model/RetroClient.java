package com.example.aliosama.dramatranslation.model;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aliosama on 8/15/2017.
 */

public class RetroClient {


    private static final String ROOT_URL = "Ottakea_JSON_URL_Root";


    private static Retrofit getRetrofitInstance() {

        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2,TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request newRequest;
                        newRequest = chain.request().newBuilder()
                                .addHeader("Content-Type","application/json")
                                .build();

                        return chain.proceed(newRequest);
                    }
                }).build();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
