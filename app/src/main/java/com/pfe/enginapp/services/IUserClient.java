package com.pfe.enginapp.services;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Auth;
import com.pfe.enginapp.models.Intervention;
import com.pfe.enginapp.models.Team;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserClient {

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



    @GET("intervention/getInterventionByChef/{id_team}")
    Call<Intervention> getIntervention(@Path("id_team") String id_team);


    @POST("intervention/updateInterventionByChef/{id_intervention}")
    Call<Intervention> updateIntervention(@Path("id_intervention") String id_intervention,@Body Intervention intervention);




}
