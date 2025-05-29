package com.example.myapplication.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSignInBinding;
import com.example.myapplication.API.Usuario;

public class SignIn extends Fragment {

    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);

        binding.btnCrearCuenta.setOnClickListener(v -> crearCuenta());

        return binding.getRoot();
    }

    // Cambiar a llamada a la API
    private void crearCuenta() {
        String fullname = binding.etFullname.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        // Validaciones básicas
        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), getString(R.string.signin_fields_pop), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), getString(R.string.signin_pass_pop), Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto usuario (sin JWToken ni profilePicture de momento)
        Usuario nuevoUsuario = new Usuario(fullname, username, "", email, "", "cliente"); // ejemplo con tipo por defecto

        Toast.makeText(getContext(), getString(R.string.signin_pop_ok), Toast.LENGTH_SHORT).show();
    }
}
