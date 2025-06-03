package com.example.myapplication.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.ViewModel.SignInViewModel;
import com.example.myapplication.databinding.FragmentSignInBinding;

public class SignIn extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        binding.btnCrearCuenta.setOnClickListener(this::crearCuenta);

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), this::mostrarError);
        viewModel.getUsuarioLiveData().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_signIn_to_profile);
            }
        });

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
        boolean aceptaTerminos = binding.cbAceptarTerminos.isChecked();

        viewModel.crearCuenta(requireContext(), fullname, username, email, password, confirmPassword, aceptaTerminos);
    }
}
