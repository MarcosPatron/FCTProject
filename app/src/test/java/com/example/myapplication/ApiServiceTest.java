package com.example.myapplication;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.Usuario;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApiServiceTest {

    @Test
    public void getPerfil_retornaUsuarioMockeado() throws IOException {
        // 1. Crear mock de ApiService
        ApiService apiService = mock(ApiService.class);

        // 2. Crear mock de la llamada
        Call<Usuario> mockCall = mock(Call.class);

        // 3. Crear un objeto Usuario con datos reales
        Usuario usuarioMock = new Usuario(
                "Juan Pérez",
                "juanp",
                "jwt_token_123",
                "juan@example.com",
                "https://example.com/juan.jpg",
                "password123"
        );

        // 4. Simular que el call.execute() devuelve el usuario
        when(mockCall.execute()).thenReturn(Response.success(usuarioMock));

        // 5. Simular que getPerfil() devuelve esa llamada
        when(apiService.getPerfil()).thenReturn(mockCall);

        // 6. Ejecutar la llamada simulada
        Response<Usuario> response = apiService.getPerfil().execute();

        // 7. Verificar resultados
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        Usuario result = response.body();
        assertEquals("juanp", result.getUsername());
        assertEquals("Juan Pérez", result.getFullname());
        assertEquals("juan@example.com", result.getEmail());
        assertEquals("jwt_token_123", result.getJWToken());
        assertEquals("https://example.com/juan.jpg", result.getProfilePicture());
    }
}
