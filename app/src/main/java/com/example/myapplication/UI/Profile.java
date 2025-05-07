package com.example.myapplication.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentProfileBinding;


public class Profile extends Fragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Botón para abrir el drawer-menu
        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.tvEditUsername.setOnClickListener(v -> {
            // Editar nombre usuario
            Toast.makeText(getContext(), "Editar username", Toast.LENGTH_SHORT).show();
        });

        binding.tvEditFullName.setOnClickListener(v -> {
            // Editar editar nombre completo
            Toast.makeText(getContext(), "Editar fullname", Toast.LENGTH_SHORT).show();
        });

        binding.tvEditEmail.setOnClickListener(v -> {
            // Editar email
            Toast.makeText(getContext(), "Editar email", Toast.LENGTH_SHORT).show();
        });

        binding.tvEditPassword.setOnClickListener(v -> {
            // Editar cantraseña
            Toast.makeText(getContext(), "Editar password", Toast.LENGTH_SHORT).show();
        });

        binding.tvDeleteAccount.setOnClickListener(v -> {
            // Eliminar cuenta
            Toast.makeText(getContext(), "Eliminar cuenta", Toast.LENGTH_SHORT).show();
        });
    }
}