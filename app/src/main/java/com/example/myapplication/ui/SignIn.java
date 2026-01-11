package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSignInBinding;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.viewmodel.SignInViewModel;

public class SignIn extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.btnCrearCuenta.setOnClickListener(this::crearCuenta);

        binding.btnBack.setOnClickListener(view ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signIn_to_logIn));
    }

    private void observeViewModel() {
        // Manejo de errores
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), mensaje -> {
            if (binding != null) {
                binding.tvErrorMessage.setText(mensaje);
                binding.tvErrorMessage.setVisibility(mensaje != null && !mensaje.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        // Usuario creado correctamente
        viewModel.getUsuarioLiveData().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario == null) return;

            // Guardar sesión
            Usuario.guardarSesion(requireContext(), usuario);

            // Cambiar Drawer
            NavController navController = NavHostFragment.findNavController(this);
            navController.setGraph(R.navigation.nav_graph);

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).actualizarDrawerMenu();
            }

        });
    }

    private void crearCuenta(View v) {
        if (binding == null || !isAdded()) return;

        // Ocultar error anterior
        binding.tvErrorMessage.setText("");
        binding.tvErrorMessage.setVisibility(View.GONE);

        String fullname = binding.etFullname.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        boolean aceptaTerminos = binding.cbAceptarTerminos.isChecked();

        viewModel.crearCuenta(requireContext(), fullname, username, email, password, confirmPassword, aceptaTerminos);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
