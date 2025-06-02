package com.example.myapplication.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.API.ApiService;
import com.example.myapplication.API.Usuario;
import com.example.myapplication.API.LoginRequest;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLogInBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends Fragment {

    private FragmentLogInBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvSignIn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(com.example.myapplication.R.id.action_logIn_to_signIn);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                binding.tvError.setText(R.string.signin_fields_pop);
                return;
            }

            LoginRequest request = new LoginRequest(username, password);
            ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
            Call<Usuario> call = apiService.logIn(request);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Usuario usuario = response.body();

                        Usuario.guardarSesion(requireContext(), usuario); // Guardar user en SharedPreferences

                        Navigation.findNavController(v).navigate(R.id.action_logIn_to_profile);
                    } else if (response.code() == 404) {
                        binding.tvError.setVisibility(View.VISIBLE);
                        binding.tvError.setText(R.string.login_wrong_data);
                    } else {
                        binding.tvError.setVisibility(View.VISIBLE);
                        binding.tvError.setText(R.string.login_error_login);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                    binding.tvError.setText(R.string.login_conexion);
                }
            });
        });
    }
}
