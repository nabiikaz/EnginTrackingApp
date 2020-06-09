package com.pfe.enginapp.services;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Auth;
import com.pfe.enginapp.models.DistanceMatrixResult;
import com.pfe.enginapp.models.SnappedPoints;
import com.pfe.enginapp.models.Team;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IGoogleMapsApiClient {

    @POST("login")
    Call<Agent> login(@Body Auth auth);

    @POST("checkToken")
    Call<ResponseBody> checkToken(@Header("authorization") String authToken);

    @POST("getAgent/{id}")
    Call<Agent> getAgent(@Path("id") String idAgent);

    /**
     * getTeam()
     * @return the team members of the authenticated Agent
     */
    @GET("team")
    Call<Team> getTeam();


    /**
     * getTeam(teamId)
     * @param teamId
     * @return the team members of passed teamId
     */
    @GET("team/{id}")
    Call<Team> getTeam(@Path("id") String teamId);


    @GET("snapToRoads")
    Call<SnappedPoints> snapToRoads(@Query("path") String path, @Query("key") String key);


    @POST("team/setAdresseTeam")
    Call<ResponseBody> setAdresseTeam(@Body SnappedPoints.SnappedPoint.Location location);

    @GET("json")
    Call<DistanceMatrixResult> getDistanceMatrixResult(@Query("origins") String origins, @Query("destinations") String destinations, @Query("key") String key);






}
