package com.example.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

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
import java.util.Locale;

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

        // Inicializar spinner
        List<String> idiomas = Arrays.asList("Español", "English");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, idiomas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLanguage.setAdapter(adapter);
        // Dar funcion spinner
        binding.spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = idiomas.get(position);
                SharedPreferences prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
                String currentLang = prefs.getString("app_language", "es");

                if ((selectedLanguage.equals("Español") && !currentLang.equals("es")) ||
                        (selectedLanguage.equals("English") && !currentLang.equals("en"))) {
                    if (selectedLanguage.equals("Español")) {
                        setLocale("es");
                    } else {
                        setLocale("en");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
    }

    // Abre los terminos y condiciones o la privacidad
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

    // Cambiar idioma
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(
                config,
                requireContext().getResources().getDisplayMetrics()
        );

        // Guardar idioma en SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        prefs.edit().putString("app_language", languageCode).apply();

        // Reiniciar la actividad para aplicar el cambio
        requireActivity().recreate();
    }

}
