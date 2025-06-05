package com.example.myapplication.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {

    private final MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> usuarioEliminado = new MutableLiveData<>();

    public LiveData<Usuario> getUsuarioLiveData() {
        return usuarioLiveData;
    }

    public LiveData<String> getMensajeLiveData() {
        return mensajeLiveData;
    }

    public LiveData<Boolean> getUsuarioEliminado() {
        return usuarioEliminado;
    }

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    // Obtiene la sesion
    public void cargarUsuario(Context context) {
        Usuario u = Usuario.obtenerSesion(context);
        if (u == null) {
            mensajeLiveData.setValue("No hay sesión activa");
        } else {
            usuarioLiveData.setValue(u);
        }
    }

    // Edita el usuario y se actualiza
    public void editarUsuario(Context context, String campo, String nuevoValor, String passwordConfirmacion) {
        Usuario original = Usuario.obtenerSesion(context);

        if (original == null) {
            mensajeLiveData.setValue("Sesión no válida.");
            return;
        }

        Usuario modificado = new Usuario(
                original.getFullname(),
                original.getUsername(),
                passwordConfirmacion,
                original.getEmail()
        );
        modificado.setJWToken(original.getJWToken());
        modificado.setProfilePicture(original.getProfilePicture());

        switch (campo.toLowerCase()) {
            case "username":
                modificado.setUsername(nuevoValor);
                break;
            case "fullname":
                modificado.setFullname(nuevoValor);
                break;
            case "email":
                modificado.setEmail(nuevoValor);
                break;
            case "password":
                modificado.setPassword(nuevoValor);
                break;
        }

        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.EditUser(original.getUsername(), modificado).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario actualizado = response.body();
                    actualizado.setPassword(modificado.getPassword());
                    Usuario.guardarSesion(context, actualizado);
                    usuarioLiveData.setValue(actualizado);
                    mensajeLiveData.setValue(context.getString(com.example.myapplication.R.string.profile_pop_updated));
                } else if (response.code() == 401) {
                    mensajeLiveData.setValue(context.getString(com.example.myapplication.R.string.login_wrong_data));
                } else {
                    mensajeLiveData.setValue(context.getString(com.example.myapplication.R.string.profile_pop_error_update));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                mensajeLiveData.setValue(context.getString(com.example.myapplication.R.string.login_conexion));
            }
        });
    }

    // Elimina el usuario y cierra sesion
    public void eliminarUsuario(Context context) {
        Usuario usuario = Usuario.obtenerSesion(context);
        if (usuario == null) {
            mensajeLiveData.setValue("No hay usuario activo");
            return;
        }

        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.deleteUser(usuario.getUsername()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Usuario.cerrarSesion(context);
                    usuarioEliminado.setValue(true);
                } else {
                    mensajeLiveData.setValue(context.getString(com.example.myapplication.R.string.login_error_login));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                mensajeLiveData.setValue(context.getString(com.example.myapplication.R.string.login_conexion));
            }
        });
    }
}
