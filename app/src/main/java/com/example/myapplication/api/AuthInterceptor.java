package com.example.myapplication.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

// Clase para que el API Client sepa cuando usar el JWT
public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String path = originalRequest.url().encodedPath();

        // Endpoints que necesitan autenticacion con JWT
        boolean needsAuth = path.contains("/get_user") ||
                path.contains("/edit_user") ||
                path.contains("/validate-token") ||
                path.contains("/send_ticket") ||
                path.contains("/delete_user");

        if (needsAuth) {
            SharedPreferences prefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE);
            String token = prefs.getString("jwt_token", null);

            if (token != null) {
                Request authorisedRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(authorisedRequest);
            }
        }
        return chain.proceed(originalRequest);
    }
}
