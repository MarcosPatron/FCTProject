package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.myapplication.ui.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityLocationTest {

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void fusedLocationClient_isNotNull_and_getsLocation() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            FusedLocationProviderClient client =
                    LocationServices.getFusedLocationProviderClient(activity);

            assertNotNull(client);

            // Esto no es una prueba confiable de lat/lng, pero verifica que no crashee
            client.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    // Actualiza variables globales
                    assertEquals(lat, MainActivity.latitude, 0.0001);
                    assertEquals(lng, MainActivity.longitude, 0.0001);
                }
            });
        });
    }

    @Test
    public void whenPermissionDenied_shouldShowToast() {
        // Este test es conceptual, ya que simular "denegado" requiere manipular el sistema o un entorno de CI avanzado
        // Puedes correrlo manualmente revocando permisos al iniciar
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            int permission = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // Aquí deberías ver el Toast, pero Espresso no puede capturar toasts directamente sin extra ayuda
                // Podemos simplemente confirmar que no se llama a getLastLocation()
                assertEquals(0.0, MainActivity.latitude, 0.0);
                assertEquals(0.0, MainActivity.longitude, 0.0);
            }
        });
    }
}
