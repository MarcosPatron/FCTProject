package com.example.myapplication.UI;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.API.ApiService;
import com.example.myapplication.API.Ticket;
import com.example.myapplication.API.Usuario;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Categoria;
import com.example.myapplication.Utils.Prioridad;
import com.example.myapplication.databinding.FragmentHelpBinding;

import java.util.Arrays;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

public class Help extends Fragment {

    private FragmentHelpBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Spinners
        List<String> categories = Arrays.asList(getString(R.string.select), getString(R.string.help_prio_acc), getString(R.string.help_prio_assis), getString(R.string.help_prio_prob));
        List<String> priorities = Arrays.asList(getString(R.string.select), getString(R.string.help_cate_high), getString(R.string.help_cat_med), getString(R.string.help_cat_low));

        setupSpinner(binding.spinnerCategory, categories);
        setupSpinner(binding.spinnerPriority, priorities);

        // Botón para abrir el drawer-menu
        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.btnSendTicket.setOnClickListener(v -> {
            // Obtener usuario desde SharedPreferences
            Usuario usuario = Usuario.obtenerSesion(requireContext());

            if (usuario == null) {
                Toast.makeText(getContext(), (R.string.help_error_login), Toast.LENGTH_LONG).show();
                return;
            }

            String category = binding.spinnerCategory.getSelectedItem().toString();
            String priority = binding.spinnerPriority.getSelectedItem().toString();
            String description = binding.etDescription.getText().toString().trim();

            if (category.equals(getString(R.string.select))) {
                Toast.makeText(getContext(), getString(R.string.help_pop_cat), Toast.LENGTH_SHORT).show();
                return;
            }
            if (priority.equals(getString(R.string.select))) {
                Toast.makeText(getContext(), getString(R.string.help_pop_prio), Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.help_pop_desc), Toast.LENGTH_SHORT).show();
                return;
            }

            Ticket ticket = new Ticket(usuario, getCategoriaEnum(category), getPrioridadEnum(priority), description);

            ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
            apiService.sendTicket(ticket).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), getString(R.string.help_pop_ok), Toast.LENGTH_LONG).show();
                        Navigation.findNavController(v).navigate(R.id.action_help_to_main_menu);
                    } else {
                        Toast.makeText(getContext(), (R.string.help_ticket_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.login_conexion), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void setupSpinner(android.widget.Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, items
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Item predeterminado
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                android.widget.TextView tv = (android.widget.TextView) view;
                tv.setTextColor(position == 0
                        ? getResources().getColor(android.R.color.darker_gray)
                        : getResources().getColor(android.R.color.black));
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private Categoria getCategoriaEnum(String selectedText) {
        if (selectedText.equals(getString(R.string.help_prio_acc))) {
            return Categoria.Cuenta;
        } else if (selectedText.equals(getString(R.string.help_prio_assis))) {
            return Categoria.Asistente;
        } else if (selectedText.equals(getString(R.string.help_prio_prob))) {
            return Categoria.Tecnicos;
        } else {
            throw new IllegalArgumentException("Categoría inválida: " + selectedText);
        }
    }

    private Prioridad getPrioridadEnum(String selectedText) {
        if (selectedText.equals(getString(R.string.help_cat_low))) {
            return Prioridad.baja;
        } else if (selectedText.equals(getString(R.string.help_cat_med))) {
            return Prioridad.media;
        } else if (selectedText.equals(getString(R.string.help_cate_high))) {
            return Prioridad.alta;
        } else {
            throw new IllegalArgumentException("Prioridad inválida: " + selectedText);
        }
    }

}
