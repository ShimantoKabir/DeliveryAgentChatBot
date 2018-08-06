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
            "Authorization: Bearer a78734f0cad44eca9ebc7270f68ad23f",
            "Content-Type: application/json",
    })
    @POST("/v1/query?v=20150910")
    Call<Response> getResponse(@Body Request request);

}
