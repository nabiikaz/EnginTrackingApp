package com.pfe.enginapp.services;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Auth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IUserClient {

    @POST("login")
    Call<Agent> login(@Body Auth auth);

    @POST("checkToken")
    Call<ResponseBody> checkToken(@Header("authorization") String authToken);




}
