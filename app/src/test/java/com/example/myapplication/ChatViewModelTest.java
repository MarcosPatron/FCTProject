package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.R;
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

        // Usamos una subclase para inyectar el mock de SharedPreferencesManager
        viewModel = new ChatViewModel(mockApp) {
            @Override
            public void saveThread(List<String> msgs, List<Boolean> flags) {
                mockPrefs.saveChatThread(new ChatClass("abc123", msgs, flags));
            }

            @Override
            public Application getApplication() {
                return mockApp;
            }
        };

        // Simulamos carga inicial del thread para establecer threadId correctamente
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
        viewModel.getMessages().setValue(new ArrayList<>());
        viewModel.getIsUserMessage().setValue(new ArrayList<>());

        viewModel.sendMessage("Hola IA", Arrays.asList(1.0, 2.0));

        List<String> mensajes = viewModel.getMessages().getValue();
        List<Boolean> autores = viewModel.getIsUserMessage().getValue();

        assertNotNull(mensajes);
        assertNotNull(autores);

        assertTrue(mensajes.contains("Hola IA"));
        assertTrue(autores.contains(true));

        verify(mockPrefs).saveChatThread(any(ChatClass.class));
    }
}
