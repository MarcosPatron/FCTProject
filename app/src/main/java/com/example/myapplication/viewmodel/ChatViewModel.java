package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.MessageRequest;
import com.example.myapplication.api.MessageResponse;
import com.example.myapplication.model.ChatClass;
import com.example.myapplication.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends AndroidViewModel {

    private final MutableLiveData<List<String>> messages =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Boolean>> isUserMessage =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> canSend =
            new MutableLiveData<>(true);

    private final SharedPreferencesManager prefs;

    private String threadId;

    // ─────────────────────────────
    // LiveData getters
    // ─────────────────────────────
    public LiveData<List<String>> getMessages() {
        return messages;
    }

    public LiveData<List<Boolean>> getIsUserMessage() {
        return isUserMessage;
    }

    public LiveData<Boolean> getCanSend() {
        return canSend;
    }

    public ChatViewModel(@NonNull Application application) {
        super(application);
        prefs = SharedPreferencesManager.getInstance(application);
    }


    // ─────────────────────────────
    // Cargar conversación
    // ─────────────────────────────
    public void loadMessages(String id) {
        this.threadId = id;

        ChatClass chat = prefs.getChatThread(id);
        if (chat != null) {
            messages.setValue(new ArrayList<>(chat.getMessages()));
            isUserMessage.setValue(new ArrayList<>(chat.getIsUserMessage()));
        }
    }

    // ─────────────────────────────
    // Enviar mensaje
    // ─────────────────────────────
    public void sendMessage(
            String username,
            String userMessage,
            List<Double> coordinates
    ) {

        if (userMessage == null || userMessage.trim().isEmpty()) return;

        List<String> currentMessages = new ArrayList<>(messages.getValue());
        List<Boolean> currentFlags = new ArrayList<>(isUserMessage.getValue());

        // Mensaje del usuario
        currentMessages.add(userMessage);
        currentFlags.add(true);

        // Mensaje "escribiendo..."
        currentMessages.add(getApplication().getString(R.string.chat_loading));
        currentFlags.add(false);

        messages.setValue(currentMessages);
        isUserMessage.setValue(currentFlags);

        ApiService api = ApiClient
                .getClient(getApplication())
                .create(ApiService.class);

        Call<MessageResponse> call = api.sendMessage(
                new MessageRequest(
                        username,
                        userMessage,
                        threadId,
                        coordinates
                )
        );

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<MessageResponse> call,
                    @NonNull Response<MessageResponse> response
            ) {
                if (!response.isSuccessful() || response.body() == null) {
                    restoreStateAfterError(currentMessages, currentFlags);
                    return;
                }

                MessageResponse res = response.body();
                threadId = res.getThreadId();

                removeLoadingMessage(currentMessages, currentFlags);

                currentMessages.add(res.getMessage());
                currentFlags.add(false);

                messages.setValue(currentMessages);
                isUserMessage.setValue(currentFlags);
                canSend.setValue(true);

                saveThread(currentMessages, currentFlags);
            }

            @Override
            public void onFailure(
                    @NonNull Call<MessageResponse> call,
                    @NonNull Throwable t
            ) {
                restoreStateAfterError(currentMessages, currentFlags);
            }
        });
    }

    // ─────────────────────────────
    // Helpers privados
    // ─────────────────────────────
    private void removeLoadingMessage(
            List<String> msgs,
            List<Boolean> flags
    ) {
        int lastIndex = msgs.size() - 1;
        if (lastIndex >= 0 &&
                getApplication()
                        .getString(R.string.chat_loading)
                        .equals(msgs.get(lastIndex))) {

            msgs.remove(lastIndex);
            flags.remove(lastIndex);
        }
    }

    private void restoreStateAfterError(
            List<String> msgs,
            List<Boolean> flags
    ) {
        removeLoadingMessage(msgs, flags);
        messages.setValue(msgs);
        isUserMessage.setValue(flags);
        canSend.setValue(true);
    }

    private void saveThread(
            List<String> msgs,
            List<Boolean> flags
    ) {
        ChatClass chat = new ChatClass(threadId, msgs, flags);
        prefs.saveChatThread(chat);
    }
}
