package com.example.queu_e.api;

import com.example.queu_e.model.OrganizationResponse;
import com.example.queu_e.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QueueApi {

    @GET("/?resource=token&function=GetCurrent")
    Call<TokenResponse> getCurrent(@Query("id") String id, @Query("name") String name);

    @GET("/?resource=token&function=RequestToken")
    Call<TokenResponse> requestToken(@Query("id") String id, @Query("name") String name);

    @GET("/?resource=org&function=GetAll")
    Call<OrganizationResponse> getAll();
}
