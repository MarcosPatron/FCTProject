package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.model.ChatClass;
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
    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;
    final Gson gson;

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

    // Guarda un chat
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

    // Obtiene un chat
    public ChatClass getChatThread(String threadId) {
        Map<String, ChatClass> chatMap = getChatThreadsMap();
        return chatMap.get(threadId);
    }

    // Obtiene todos los chats
    public List<ChatClass> getAllChatThreads() {
        return new ArrayList<>(getChatThreadsMap().values());
    }

    // Convierte el JSON a Map
    Map<String, ChatClass> getChatThreadsMap() {
        String json = sharedPreferences.getString(CHAT_THREADS_KEY, null);
        Type type = new TypeToken<Map<String, ChatClass>>() {}.getType();
        return json == null ? new HashMap<>() : gson.fromJson(json, type);
    }

    // Elimina un chat
    public void removeChatThread(String threadId) {
        Map<String, ChatClass> chatMap = getChatThreadsMap();
        chatMap.remove(threadId);
        editor.putString(CHAT_THREADS_KEY, gson.toJson(chatMap)).commit();
    }

    // Limpia todos los chats
    public void clearAllChats() {
        editor.remove(CHAT_THREADS_KEY).commit();
    }

    // METODO SOLO PARA PRUEBAS, permite reiniciar el singleton
    public static void resetInstance() {
        instance = null;
    }
}
