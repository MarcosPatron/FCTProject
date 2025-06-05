package com.example.myapplication;

import android.app.Application;
import com.example.myapplication.model.ChatClass;
import com.example.myapplication.utils.SharedPreferencesManager;
import com.example.myapplication.viewmodel.ChatViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChatViewModelTest {

    private ChatViewModel viewModel;
    private SharedPreferencesManager mockPrefs;
    private Application mockApp;

    @Before
    public void setUp() {
        mockPrefs = mock(SharedPreferencesManager.class);
        mockApp = mock(Application.class);
        when(mockApp.getString(R.string.chat_loading)).thenReturn("Procesando petición...");

        // Crea una instancia de ChatViewModel con mocks
        viewModel = new ChatViewModel(mockApp) {
            @Override
            public Application getApplication() {
                return mockApp;
            }
        };

        // Establecer estado inicial para threadId usando loadMessages()
        ChatClass mockChat = new ChatClass("abc123", new ArrayList<>(), new ArrayList<>());
        when(mockPrefs.getChatThread("abc123")).thenReturn(mockChat);
        viewModel.loadMessages("abc123");
    }

    @Test
    public void loadMessages_cargaDesdeSharedPreferences() {
        List<String> mensajes = Arrays.asList("Hola", "¿Qué tal?");
        List<Boolean> autores = Arrays.asList(true, false);
        ChatClass chat = new ChatClass("abc123", mensajes, autores);

        when(mockPrefs.getChatThread("abc123")).thenReturn(chat);

        viewModel.loadMessages("abc123");

        assertEquals(mensajes, viewModel.getMessages().getValue());
        assertEquals(autores, viewModel.getIsUserMessage().getValue());
    }

    @Test
    public void sendMessage_actualizaMensajesYGuarda() {
        // Inicializar mensajes antes de enviar
        viewModel.loadMessages("abc123");

        // Simular valores iniciales
        List<String> mensajesIniciales = new ArrayList<>();
        List<Boolean> autoresIniciales = new ArrayList<>();

        // Forzar estado inicial para las listas
        viewModel.getMessages().observeForever(mensajesIniciales::addAll);
        viewModel.getIsUserMessage().observeForever(autoresIniciales::addAll);

        // Ejecutar método
        viewModel.sendMessage("Hola IA", Arrays.asList(1.0, 2.0));

        // Verificaciones
        List<String> actualMsgs = viewModel.getMessages().getValue();
        List<Boolean> actualFlags = viewModel.getIsUserMessage().getValue();

        assertNotNull(actualMsgs);
        assertNotNull(actualFlags);
        assertTrue(actualMsgs.contains("Hola IA"));
        assertTrue(actualFlags.contains(true));
    }
}
