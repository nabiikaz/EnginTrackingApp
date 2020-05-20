package com.pfe.enginapp.services;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofitClient {
    String API_BASE_URL = "http://192.168.1.2:8000/api/";

    private static retrofitClient instance;
    private static Retrofit retrofit;

    public static retrofitClient getInstance(){
        if(instance == null){
            instance = new retrofitClient();
        }
        return  instance;
    }

    public static Retrofit getRetrofit(){

        return  retrofit;
    }


    private retrofitClient(){


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        retrofit = builder.client(httpClient.build()).build();
    }
}
