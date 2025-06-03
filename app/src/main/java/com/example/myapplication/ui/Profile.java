package com.example.myapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.viewmodel.ProfileViewModel;
import com.example.myapplication.databinding.FragmentProfileBinding;

public class Profile extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Observadores
        viewModel.getUsuarioLiveData().observe(getViewLifecycleOwner(), usuario -> {
            binding.tvUsername.setText(usuario.getUsername());
            binding.tvFullName.setText(usuario.getFullname());
            binding.tvEmail.setText(usuario.getEmail());
        });

        viewModel.getMensajeLiveData().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getUsuarioEliminado().observe(getViewLifecycleOwner(), eliminado -> {
            if (eliminado) {
                Navigation.findNavController(view).navigate(R.id.action_profile_to_logIn);
                Toast.makeText(getContext(), R.string.profile_deleted, Toast.LENGTH_SHORT).show();
            }
        });

        // Botones
        binding.tvEditUsername.setOnClickListener(v -> mostrarDialogoEdicion("username", binding.tvUsername.getText().toString()));
        binding.tvEditFullName.setOnClickListener(v -> mostrarDialogoEdicion("fullname", binding.tvFullName.getText().toString()));
        binding.tvEditEmail.setOnClickListener(v -> mostrarDialogoEdicion("email", binding.tvEmail.getText().toString()));
        binding.tvEditPassword.setOnClickListener(v -> mostrarDialogoEdicion("password", ""));
        binding.tvDeleteAccount.setOnClickListener(v -> viewModel.eliminarUsuario(requireContext()));

        viewModel.cargarUsuario(requireContext());
    }

    private void mostrarDialogoEdicion(String campo, String valorActual) {
        Context context = requireContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edition_user, null);

        TextView tvCampo = dialogView.findViewById(R.id.tvCampoEditar);
        EditText etNuevoValor = dialogView.findViewById(R.id.etNuevoValor);
        EditText etPassword = dialogView.findViewById(R.id.etPasswordConfirm);

        tvCampo.setText(getString(R.string.profile_new) + campo);
        etNuevoValor.setText(valorActual);

        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.profile_edit) + campo)
                .setView(dialogView)
                .setPositiveButton(R.string.profile_save, (dialog, which) -> {
                    String nuevoValor = etNuevoValor.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    if (nuevoValor.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, getString(R.string.signin_fields_pop), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viewModel.editarUsuario(context, campo, nuevoValor, password);
                })
                .setNegativeButton(R.string.profile_cancel, (dialog, which) -> dialog.dismiss())
                .create().show();
    }
}
