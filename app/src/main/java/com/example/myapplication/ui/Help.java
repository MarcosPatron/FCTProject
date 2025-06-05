package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplication.model.Usuario;
import com.example.myapplication.R;
import com.example.myapplication.viewmodel.HelpViewModel;
import com.example.myapplication.databinding.FragmentHelpBinding;

import java.util.Arrays;
import java.util.List;

public class Help extends Fragment {

    private FragmentHelpBinding binding;
    private HelpViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(HelpViewModel.class);

        // Spinners
        List<String> categories = Arrays.asList(getString(R.string.select), getString(R.string.help_prio_acc), getString(R.string.help_prio_assis), getString(R.string.help_prio_prob));
        List<String> priorities = Arrays.asList(getString(R.string.select), getString(R.string.help_cate_high), getString(R.string.help_cat_med), getString(R.string.help_cat_low));

        setupSpinner(binding.spinnerCategory, categories);
        setupSpinner(binding.spinnerPriority, priorities);

        // Boton menu drawer
        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Observers
        viewModel.getTicketSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), getString(R.string.help_pop_ok), Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.action_help_to_main_menu);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSendTicket.setOnClickListener(v -> {
            Usuario usuario = Usuario.obtenerSesion(requireContext());
            String category = binding.spinnerCategory.getSelectedItem().toString();
            String priority = binding.spinnerPriority.getSelectedItem().toString();
            String description = binding.etDescription.getText().toString().trim();

            viewModel.sendTicket(category, priority, description, usuario);
        });

        return view;
    }

    private void setupSpinner(android.widget.Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((android.widget.TextView) view).setTextColor(
                        position == 0 ? getResources().getColor(android.R.color.darker_gray) : getResources().getColor(android.R.color.black)
                );
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
