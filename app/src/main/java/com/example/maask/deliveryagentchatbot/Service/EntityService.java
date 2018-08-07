package com.example.maask.deliveryagentchatbot.Service;

import com.example.maask.deliveryagentchatbot.ResponseClass.EntityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by Maask on 8/7/2018.
 */

public interface EntityService {

    @Headers({
            "Authorization: Bearer 896aae1dbb8c4036bb5e8c2d42f5107e",
            "Content-Type: application/json",
    })
    @GET()
    Call<EntityResponse> getResponse(@Url String url);

}
