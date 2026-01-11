package com.example.myapplication.ui;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.R;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationProviderClient fusedLocationClient;

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    public static double latitude;
    public static double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar idioma guardado en preferencias
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("app_language", "es");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.toolbar);

        // Navigation
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_main);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // AppBarConfiguration: SOLO fragmentos del Drawer
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.main_menu, R.id.profile, R.id.settings, R.id.recommendations, R.id.help
        ).setOpenableLayout(binding.drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Cambiar visibilidad de items del Drawer según sesión
        binding.navView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            boolean sesionActiva = Usuario.sesionActiva(this);

            binding.navView.getMenu().findItem(R.id.profile).setVisible(sesionActiva);
            binding.navView.getMenu().findItem(R.id.help).setVisible(sesionActiva);
        });

        // Listener logout/login
        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.logIn) {
                if (Usuario.sesionActiva(this)) {
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle(getString(R.string.settings_logout))
                            .setMessage(getString(R.string.main_sure))
                            .setPositiveButton(getString(R.string.main_yes), (dialog, which) -> {
                                Usuario.cerrarSesion(this);
                                // Volver a grafo de Auth
                                navController.setGraph(R.navigation.auth_nav_graph);
                            })
                            .setNegativeButton(getString(R.string.main_cancel), (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    // Si no hay sesión, cargar grafo de Auth
                    navController.setGraph(R.navigation.auth_nav_graph);
                }

                binding.drawerLayout.close();
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) binding.drawerLayout.close();
            return handled;
        });

        // Localización
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    // Pide permiso al usuario
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, getString(R.string.main_permision), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });
    }

    public void actualizarDrawerMenu() {
        boolean sesionActiva = Usuario.sesionActiva(this);

        Menu menu = binding.navView.getMenu();
        menu.findItem(R.id.logIn).setTitle(
                sesionActiva ? getString(R.string.settings_logout) : getString(R.string.login_title)
        );

        menu.findItem(R.id.profile).setVisible(sesionActiva);
        menu.findItem(R.id.help).setVisible(sesionActiva);

        // Bloquear drawer si no hay sesión
        binding.drawerLayout.setDrawerLockMode(
                sesionActiva ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        );
    }
}
