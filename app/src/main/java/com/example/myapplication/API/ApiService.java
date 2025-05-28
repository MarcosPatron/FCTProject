package com.example.myapplication.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    // ASISTENTE
    @POST("send_message")
    Call<MessageResponse> sendMessage(@Body MessageRequest body);

    // USUARIO
    @GET("log_in")
    Call<Usuario> logIn(@Body Usuario body);

    @POST("sign_in")
    Call<Usuario> signIn(@Body Usuario body);

    @PUT("edit_user")
    Call<Usuario> EditUser(@Body Usuario body);

    @DELETE("delete_user")
    Call<Usuario> deleteUser(int id);

    @GET("validate-token") // Si la respuesta es 401 hago logout
    Call<Void> validateToken(@Header("Authorization") String token);

    // TICKET
    @POST("send_ticket")
    Call<Void> sendTicket(@Body Ticket body);
}
