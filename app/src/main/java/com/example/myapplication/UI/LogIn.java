package com.example.myapplication.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.databinding.FragmentLogInBinding;
import com.example.myapplication.R;

public class LogIn extends Fragment {

    private FragmentLogInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvSignIn.setOnClickListener( v -> {
            Navigation.findNavController(v).navigate(R.id.action_logIn_to_signIn);
        });

        binding.btnLogin.setOnClickListener(v -> {

            // Llamada a la API para iniciar sesion

            Navigation.findNavController(v).navigate(R.id.action_logIn_to_profile);
        });
    }
}