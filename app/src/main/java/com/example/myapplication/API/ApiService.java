package com.example.myapplication.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // ASISTENTE
    @POST("send_message")
    Call<MessageResponse> sendMessage(@Body MessageRequest body);

    // USUARIO

    // Tomar el user cada vez que se inicia la aplicación con el JWT en SharedPreferences
    @GET("/api/backend/get_user")
    Call<Usuario> getPerfil();

    @POST("/api/backend/log_in")
    Call<Usuario> logIn(@Body Usuario body);

    @POST("/api/backend/sign_in")
    Call<Usuario> signIn(@Body Usuario body);

    @PUT("/api/backend/users/{user_id}")
    Call<Usuario> EditUser(@Path("user_id") int userId, @Body Usuario body);

    @DELETE("/api/backend/delete_user/{user_id}")
    Call<Void> deleteUser(@Path("user_id") int userId);

    @GET("validate-token") // Si la respuesta es 401 hago logout
    Call<Void> validateToken(@Header("Authorization") String token);

    // TICKET
    @POST("/api/backend/send_ticket")
    Call<Void> sendTicket(@Body Ticket body);
}
