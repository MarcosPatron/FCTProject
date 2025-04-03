package com.example.myapplication.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/MiApi/sendMessage")
    Call<MessageResponse> sendMessage(@Body MessageRequest body);
}
