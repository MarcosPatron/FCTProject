package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.model.ChatClass;
import com.example.myapplication.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class MainMenuViewModel extends AndroidViewModel {

    private final MutableLiveData<List<String>> threadIdsLiveData = new MutableLiveData<>();
    private final SharedPreferencesManager prefsManager;

    public MainMenuViewModel(@NonNull Application application) {
        super(application);
        prefsManager = SharedPreferencesManager.getInstance(application.getApplicationContext());
    }

    public LiveData<List<String>> getThreadIds() {
        return threadIdsLiveData;
    }

    public void loadThreadIds() {
        List<ChatClass> allChats = prefsManager.getAllChatThreads();
        List<String> threadIds = new ArrayList<>();
        for (ChatClass chat : allChats) {
            if (chat.getThreadId() != null) {
                threadIds.add(chat.getThreadId());
            }
        }
        threadIdsLiveData.setValue(threadIds);
    }

    public void deleteThread(String threadId) {
        prefsManager.removeChatThread(threadId);
        loadThreadIds(); // Refresca la lista después de eliminar
    }
}