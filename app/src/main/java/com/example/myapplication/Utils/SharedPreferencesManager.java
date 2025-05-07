package com.example.myapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.ChatClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "pref";
    private static final String CHAT_THREADS_KEY = "chatThreads";
    private static SharedPreferencesManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    // Guardar un chat específico
    public void saveChatThread(ChatClass chatClass) {
        if (chatClass.getThreadId() == null) {
            Log.e("SAVE_ERROR", "Thread ID es nulo. No se puede guardar el chat.");
            return;
        }

        Map<String, ChatClass> chatMap = getChatThreadsMap();
        chatMap.put(chatClass.getThreadId(), chatClass);

        String json = gson.toJson(chatMap);
        boolean isSaved = editor.putString(CHAT_THREADS_KEY, json).commit();

        if (!isSaved) {
            Log.e("SAVE_ERROR", "No se pudo guardar el chat en SharedPreferences");
        } else {
            Log.d("SAVE_SUCCESS", "Chat guardado exitosamente");
        }
    }

    // Obtener un chat específico
    public ChatClass getChatThread(String threadId) {
        Map<String, ChatClass> chatMap = getChatThreadsMap();
        return chatMap.get(threadId);
    }

    // Obtener todos los chats
    public List<ChatClass> getAllChatThreads() {
        return new ArrayList<>(getChatThreadsMap().values());
    }

    // Convertir el JSON a Map para acceso eficiente
    Map<String, ChatClass> getChatThreadsMap() {
        String json = sharedPreferences.getString(CHAT_THREADS_KEY, null);
        Type type = new TypeToken<Map<String, ChatClass>>() {}.getType();
        return json == null ? new HashMap<>() : gson.fromJson(json, type);
    }

    // Eliminar un chat específico
    public void removeChatThread(String threadId) {
        Map<String, ChatClass> chatMap = getChatThreadsMap();
        chatMap.remove(threadId);
        editor.putString(CHAT_THREADS_KEY, gson.toJson(chatMap)).commit();
    }

    // Limpiar todos los chats
    public void clearAllChats() {
        editor.remove(CHAT_THREADS_KEY).commit();
    }
}
