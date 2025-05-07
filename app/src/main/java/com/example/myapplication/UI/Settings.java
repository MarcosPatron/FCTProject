package com.example.myapplication.UI;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSettingsBinding;

import java.util.Arrays;
import java.util.List;

public class Settings extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Switch de modo oscuro
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Aquí iría la lógica para cambiar el tema oscuro
        });

        // Spinner de idioma
        List<String> idiomas = Arrays.asList("Español", "English");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, idiomas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLanguage.setAdapter(adapter);

        binding.spinnerLanguage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = idiomas.get(position);
                // Aquí podrías aplicar el cambio de idioma, si se desea
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Botón para abrir el drawer-menu
        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // TextViews clicables
        binding.tvTerms.setOnClickListener(v -> {
            showPopupWindow();
        });

        binding.tvPrivacy.setOnClickListener(v -> {
            showPopupWindow();
        });

        binding.tvContactSupport.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_settings_to_help);
        });

        binding.tvLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cerrar sesión", Toast.LENGTH_SHORT).show();
        });
    }

    private void showPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        View popupView = inflater.inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        final View dimView = new View(requireContext());
        dimView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        dimView.setBackgroundColor(Color.parseColor("#80000000")); // fondo negro con 50% opacidad

        ViewGroup rootView = (ViewGroup) requireActivity().getWindow().getDecorView();
        rootView.addView(dimView);

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(() -> rootView.removeView(dimView));
    }
}
