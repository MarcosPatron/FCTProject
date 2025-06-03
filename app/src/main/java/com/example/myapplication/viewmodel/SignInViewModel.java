package com.example.myapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInViewModel extends AndroidViewModel {

    private final MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public SignInViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Usuario> getUsuarioLiveData() {
        return usuarioLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void crearCuenta(Context context, String fullname, String username, String email, String password, String confirmPassword, boolean aceptaTerminos) {
        if (fullname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLiveData.setValue(context.getString(R.string.signin_fields_pop));
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLiveData.setValue(context.getString(R.string.signin_pass_pop));
            return;
        }

        if (!aceptaTerminos) {
            errorLiveData.setValue(context.getString(R.string.signin_pop_termsnconds));
            return;
        }

        Usuario nuevoUsuario = new Usuario(fullname, username, password, email);
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        apiService.signIn(nuevoUsuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();
                    Usuario.guardarSesion(context, usuario);

                    try {
                        SharedPreferences prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
                        prefs.edit().putString("username", usuario.getUsername()).apply();
                    } catch (Exception e) {
                        // Silencio
                    }

                    usuarioLiveData.setValue(usuario);
                } else {
                    errorLiveData.setValue(context.getString(R.string.help_error_login));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                errorLiveData.setValue(context.getString(R.string.login_conexion));
            }
        });
    }
}
