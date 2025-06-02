package com.example.myapplication.UI;

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
import androidx.navigation.Navigation;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.API.ApiService;
import com.example.myapplication.API.Usuario;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentProfileBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends Fragment {
    private FragmentProfileBinding binding;
    private Usuario usuarioOriginal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarSesion();

        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.tvEditUsername.setOnClickListener(v -> mostrarDialogoEdicion("username", usuarioOriginal.getUsername()));
        binding.tvEditFullName.setOnClickListener(v -> mostrarDialogoEdicion("fullname", usuarioOriginal.getFullname()));
        binding.tvEditEmail.setOnClickListener(v -> mostrarDialogoEdicion("email", usuarioOriginal.getEmail()));
        binding.tvEditPassword.setOnClickListener(v -> mostrarDialogoEdicion("password", ""));
        binding.tvDeleteAccount.setOnClickListener(this::eliminarUsuario);
    }

    private void cargarSesion() {
        usuarioOriginal = Usuario.obtenerSesion(requireContext());

        if (usuarioOriginal == null) {
            Toast.makeText(getContext(), "No hay sesión activa", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.tvUsername.setText(usuarioOriginal.getUsername());
        binding.tvFullName.setText(usuarioOriginal.getFullname());
        binding.tvEmail.setText(usuarioOriginal.getEmail());
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
                .setPositiveButton((R.string.profile_save), (dialog, which) -> {
                    String nuevoValor = etNuevoValor.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    if (nuevoValor.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, getString(R.string.signin_fields_pop), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    actualizarCampoUsuario(campo, nuevoValor, password);
                })
                .setNegativeButton((R.string.profile_cancel), (dialog, which) -> dialog.dismiss())
                .create().show();
    }

    private void actualizarCampoUsuario(String campo, String nuevoValor, String passwordConfirmacion) {
        Usuario usuarioEditado = new Usuario(
                usuarioOriginal.getFullname(),
                usuarioOriginal.getUsername(),
                passwordConfirmacion,
                usuarioOriginal.getEmail()
        );
        usuarioEditado.setJWToken(usuarioOriginal.getJWToken());
        usuarioEditado.setProfilePicture(usuarioOriginal.getProfilePicture());

        switch (campo.toLowerCase()) {
            case "username":
                usuarioEditado.setUsername(nuevoValor);
                break;
            case "fullname":
                usuarioEditado.setFullname(nuevoValor);
                break;
            case "email":
                usuarioEditado.setEmail(nuevoValor);
                break;
            case "password":
                usuarioEditado.setPassword(nuevoValor);
                break;
        }

        editarUsuario(usuarioEditado);
    }

    private void editarUsuario(Usuario usuarioEditado) {
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<Usuario> call = apiService.EditUser(usuarioOriginal.getUsername(), usuarioEditado);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuarioActualizado = response.body();
                    usuarioActualizado.setPassword(usuarioEditado.getPassword());

                    Usuario.guardarSesion(requireContext(), usuarioActualizado);
                    usuarioOriginal = usuarioActualizado;

                    cargarSesion();
                    Toast.makeText(getContext(), (R.string.profile_pop_updated), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), getString(R.string.login_wrong_data), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), (R.string.profile_pop_error_update), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getString(R.string.login_conexion), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void eliminarUsuario(View v) {
        if (usuarioOriginal == null) {
            Toast.makeText(getContext(), getString(R.string.profile_new), Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<Void> call = apiService.deleteUser(usuarioOriginal.getUsername());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Usuario.cerrarSesion(requireContext());
                    Toast.makeText(getContext(), (R.string.profile_deleted), Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v).navigate(R.id.action_profile_to_logIn);
                } else {
                    Toast.makeText(getContext(), getString(R.string.login_error_login), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getString(R.string.login_conexion), Toast.LENGTH_LONG).show();
            }
        });
    }
}
