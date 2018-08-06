package com.example.maask.deliveryagentchatbot.Service;

import com.example.maask.deliveryagentchatbot.RequestClass.Request;
import com.example.maask.deliveryagentchatbot.ResponseClass.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Maask on 8/5/2018.
 */

public interface ConversationService {

    @Headers({
            "Authorization: Bearer a1c3d31db11f4a1392c79dd6a531b4c0",
            "Content-Type: application/json",
    })
    @POST("/v1/query?v=20150910")
    Call<Response> getResponse(@Body Request request);

}
