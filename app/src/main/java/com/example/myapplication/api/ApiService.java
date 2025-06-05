package com.example.myapplication.api;

import com.example.myapplication.model.Ticket;
import com.example.myapplication.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // ASISTENTE
    @POST("send_message")
    Call<MessageResponse> sendMessage(@Body MessageRequest body);

    // USUARIO
    @GET("/api/backend/get_user")
    Call<Usuario> getPerfil();

    @POST("/api/backend/log_in")
    Call<Usuario> logIn(@Body LoginRequest body);

    @POST("/api/backend/sign_in")
    Call<Usuario> signIn(@Body Usuario body);

    @PUT("/api/backend/edit_user/{username}")
    Call<Usuario> EditUser(@Path("username") String username, @Body Usuario body);

    @DELETE("/api/backend/delete_user/{username}")
    Call<Void> deleteUser(@Path("username") String username);

    // TICKET
    @POST("/api/backend/send_ticket")
    Call<Void> sendTicket(@Body Ticket body);
}
