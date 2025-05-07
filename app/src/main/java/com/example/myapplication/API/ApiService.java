package com.example.myapplication.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    @POST("/api/backend/sendMessage")
    Call<MessageResponse> sendMessage(@Body MessageRequest body);

    @GET("/api/backend/logIn")
    Call<Usuario> logIn(@Body Usuario body);

    @POST("/api/backend/signIn")
    Call<Usuario> signIn(@Body Usuario body);

    @PUT("/api/backend/editUser")
    Call<Usuario> EditUser(@Body Usuario body);

    @DELETE("/api/backend/deleteUser")
    Call<Usuario> deleteUser(int id);
}
