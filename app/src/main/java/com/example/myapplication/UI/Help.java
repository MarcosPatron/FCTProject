package com.example.myapplication.UI;

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

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHelpBinding;

import java.util.Arrays;
import java.util.List;

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

        // Botón
        binding.btnSendTicket.setOnClickListener(v -> {
            String category = binding.spinnerCategory.getSelectedItem().toString();
            String priority = binding.spinnerPriority.getSelectedItem().toString();
            String description = binding.etDescription.getText().toString().trim();

            if (category.equals(categories.get(0))) {
                Toast.makeText(getContext(), getString(R.string.help_pop_cat), Toast.LENGTH_SHORT).show();
                return;
            }
            if (priority.equals(priorities.get(0))) {
                Toast.makeText(getContext(), getString(R.string.help_pop_prio), Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.help_pop_desc), Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), getString(R.string.help_pop_ok), Toast.LENGTH_LONG).show();

            // LLamada a la API

            Navigation.findNavController(v).navigate(R.id.action_help_to_main_menu);
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
}
