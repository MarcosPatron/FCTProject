package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.model.ChatClass;
import com.example.myapplication.utils.SharedPreferencesManager;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SharedPreferencesManagerTest {

    private SharedPreferencesManager manager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();

    private Context mockContext;

    @Before
    public void setup() {
        mockContext = mock(Context.class);
        sharedPreferences = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);

        when(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE)))
                .thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(editor.remove(anyString())).thenReturn(editor);
        when(editor.commit()).thenReturn(true);

        // Reiniciar el singleton (solo para tests)
        SharedPreferencesManager.resetInstance();

        manager = SharedPreferencesManager.getInstance(mockContext);
    }

    @Test
    public void saveAndRetrieveChatThread_success() {
        ChatClass chat = new ChatClass(
                "abc123",
                Arrays.asList("Hola", "¿Qué tal?"),
                Arrays.asList(true, false)
        );

        Map<String, ChatClass> fakeMap = new HashMap<>();
        fakeMap.put(chat.getThreadId(), chat);
        String json = gson.toJson(fakeMap);

        // Simular lectura inicial vacía
        when(sharedPreferences.getString(eq("chatThreads"), isNull())).thenReturn(null);

        // Guardar el chat
        manager.saveChatThread(chat);

        verify(editor).putString(eq("chatThreads"), anyString());
        verify(editor).commit();

        // Simular lectura con chat guardado
        when(sharedPreferences.getString(eq("chatThreads"), isNull())).thenReturn(json);

        ChatClass loaded = manager.getChatThread("abc123");

        assertNotNull(loaded);
        assertEquals("abc123", loaded.getThreadId());
        assertEquals(2, loaded.getMessages().size());
        assertEquals("Hola", loaded.getMessages().get(0));
        assertTrue(loaded.getIsUserMessage().get(0));
    }

    @Test
    public void clearAllChats_shouldRemoveKey() {
        manager.clearAllChats();
        verify(editor).remove("chatThreads");
        verify(editor).commit();
    }
}
