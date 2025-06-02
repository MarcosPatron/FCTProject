package com.example.myapplication.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.API.ApiService;
import com.example.myapplication.API.Usuario;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSignInBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends Fragment {

    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        binding.btnCrearCuenta.setOnClickListener(this::crearCuenta);
        return binding.getRoot();
    }

    private void mostrarError(String mensaje) {
        binding.tvErrorMessage.setText(mensaje);
        binding.tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void ocultarError() {
        binding.tvErrorMessage.setText("");
        binding.tvErrorMessage.setVisibility(View.GONE);
    }

    private void crearCuenta(View v) {
        ocultarError();

        String fullname = binding.etFullname.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            mostrarError(getString(R.string.signin_fields_pop));
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarError(getString(R.string.signin_pass_pop));
            return;
        }

        if (!binding.cbAceptarTerminos.isChecked()) {
            mostrarError(getString(R.string.signin_pop_termsnconds));
            return;
        }

        Usuario nuevoUsuario = new Usuario(fullname, username, password, email);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<Usuario> call = apiService.signIn(nuevoUsuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();
                    Usuario.guardarSesion(requireContext(), usuario);

                    try {
                        SharedPreferences prefs = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
                        prefs.edit().putString("username", usuario.getUsername()).apply();
                    } catch (Exception e) {
                        // Ignorar errores de guardado local
                    }

                    Navigation.findNavController(v).navigate(R.id.action_signIn_to_profile);
                } else {
                    mostrarError(getString(R.string.help_error_login));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                mostrarError(getString(R.string.login_conexion));
            }
        });
    }
}
