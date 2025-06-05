package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.LoginRequest;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInViewModel extends AndroidViewModel {

    private final MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<Usuario> getUsuarioLiveData() {
        return usuarioLiveData;
    }
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LogInViewModel(@NonNull Application application) {
        super(application);
    }

    // Hace login con llamada al backend
    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            errorLiveData.setValue(getApplication().getString(R.string.signin_fields_pop));
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        ApiService apiService = ApiClient.getClient(getApplication()).create(ApiService.class);
        apiService.logIn(request).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usuarioLiveData.setValue(response.body());
                } else if (response.code() == 404) {
                    errorLiveData.setValue(getApplication().getString(R.string.login_wrong_data));
                } else {
                    errorLiveData.setValue(getApplication().getString(R.string.login_error_login));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                errorLiveData.setValue(getApplication().getString(R.string.login_conexion));
            }
        });
    }
}
