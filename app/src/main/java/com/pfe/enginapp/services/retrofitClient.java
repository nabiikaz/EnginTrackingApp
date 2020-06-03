package com.pfe.enginapp.services;

import android.app.Activity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofitClient {
    String API_BASE_URL = "http://192.168.1.4:8000/api/";
    String authToken;
    private static Retrofit retrofit;
    private static retrofitClient instance;






    public Retrofit getRetrofit() {


        return retrofit;
    }


    public retrofitClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        retrofit = builder.build();


    }

    public retrofitClient(final String authToken) {


        OkHttpClient.Builder  httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("authorization", authToken)
                        .build();
                return chain.proceed(build);
            }
        });





        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


                .client(httpClient.build()) // add this line
                .build();



    }

    public retrofitClient(Boolean isGoogleApiCall,String baseUrl) {


        OkHttpClient.Builder  httpClient = new OkHttpClient.Builder();






        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)

                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


                .client(httpClient.build()) // add this line
                .build();



    }

    public Retrofit getClient() {

        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}

