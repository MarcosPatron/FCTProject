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
import com.example.myapplication.databinding.FragmentLogInBinding;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.viewmodel.LogInViewModel;

public class LogIn extends Fragment {

    private FragmentLogInBinding binding;
    private LogInViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        observeViewModel();
        setupListeners();
    }

    private void observeViewModel() {
        viewModel.getUsuarioLiveData().observe(
                getViewLifecycleOwner(),
                usuario -> {
                    if (usuario == null) return;

                    // Guardar sesión
                    Usuario.guardarSesion(requireContext(), usuario);

                    // Cambiar Drawer
                    NavController navController = NavHostFragment.findNavController(this);

                    navController.setGraph(R.navigation.nav_graph);

                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).actualizarDrawerMenu();
                    }
                }
        );

        viewModel.getErrorLiveData().observe(
                getViewLifecycleOwner(),
                errorMsg -> {
                    if (errorMsg == null || errorMsg.isEmpty()) return;

                    binding.tvError.setVisibility(View.VISIBLE);
                    binding.tvError.setText(errorMsg);
                }
        );
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            viewModel.login(username, password);
        });

        binding.tvSignIn.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_logIn_to_signIn)
        );
        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_logIn_to_nav_graph)
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
