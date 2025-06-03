package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.model.Usuario;
import com.example.myapplication.R;
import com.example.myapplication.viewmodel.LogInViewModel;
import com.example.myapplication.databinding.FragmentLogInBinding;

public class LogIn extends Fragment {

    private FragmentLogInBinding binding;
    private LogInViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        // Observers
        viewModel.getUsuarioLiveData().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                Usuario.guardarSesion(requireContext(), usuario);
                Navigation.findNavController(view).navigate(R.id.action_logIn_to_profile);
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMsg -> {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.tvError.setText(errorMsg);
        });

        binding.tvSignIn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_logIn_to_signIn);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            viewModel.login(username, password);
        });
    }
}
