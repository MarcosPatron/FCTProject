package com.example.myapplication.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.API.ApiService;
import com.example.myapplication.API.MessageRequest;
import com.example.myapplication.API.MessageResponse;
import com.example.myapplication.ChatClass;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils.RecyclerChat;
import com.example.myapplication.Utils.SharedPreferencesManager;
import com.example.myapplication.databinding.FragmentChatBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends Fragment {

    private FragmentChatBinding binding;
    private RecyclerChat adapter;
    private List<String> messages;
    private List<Boolean> isUserMessage;
    private int processingMessageIndex = -1;
    private String currentThreadId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Pasos previos para iniciar el Chat
        binding = FragmentChatBinding.inflate(inflater, container, false);
        messages = new ArrayList<>();
        isUserMessage = new ArrayList<>();
        adapter = new RecyclerChat(messages, isUserMessage);
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerChat.setAdapter(adapter);

        // Cargar mensajes
        currentThreadId = getArguments() != null ? getArguments().getString("threadId") : null;
        if (currentThreadId != null) {
            loadMessages(currentThreadId);
        }

        binding.btnButtom.setOnClickListener(v -> sendMessage());

        return binding.getRoot();
    }

    private void sendMessage() {
        String message = binding.consulta.getText().toString().trim();

        if (message.isEmpty()) {
            Log.e("API_ERROR", "El mensaje no puede estar vacío");
            return;
        }

        messages.add(message);
        isUserMessage.add(true);
        adapter.notifyItemInserted(messages.size() - 1);
        binding.consulta.setText("");

        binding.recyclerChat.postDelayed(() -> {
            messages.add(getString(R.string.chat_loading));
            processingMessageIndex = messages.size() - 1;
            isUserMessage.add(false);
            adapter.notifyItemInserted(messages.size() - 1);
            binding.recyclerChat.smoothScrollToPosition(messages.size() - 1);
        }, 500);

        receiveMessage(message);
    }

    private void receiveMessage(String message) {

        List<Double> coordinates = new ArrayList<>(); // Example data

        Log.e("COORDENADAS", "Latutid:" + MainActivity.latitude + ". Longitud: " + MainActivity.longitude);

        coordinates.add(MainActivity.latitude);
        coordinates.add(MainActivity.longitude);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<MessageResponse> call = apiService.sendMessage(new MessageRequest(message, currentThreadId, coordinates));

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("API_RESPONSE", "Error en la API: " + response.code());
                    return;
                }

                MessageResponse msgResponse = response.body();
                if (msgResponse == null) {
                    Log.e("API_RESPONSE", "El cuerpo de la respuesta es nulo");
                    return;
                }

                currentThreadId = msgResponse.getThreadId();

                if (processingMessageIndex != -1) {
                    messages.remove(processingMessageIndex);
                    isUserMessage.remove(processingMessageIndex);
                    adapter.notifyItemRemoved(processingMessageIndex);
                    processingMessageIndex = -1;
                }

                messages.add(msgResponse.getMessage());
                isUserMessage.add(false);
                adapter.notifyItemInserted(messages.size() - 1);
                binding.recyclerChat.smoothScrollToPosition(messages.size() - 1);

                saveMessages();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void saveMessages() {
        SharedPreferencesManager manager = SharedPreferencesManager.getInstance(requireContext());
        ChatClass chatClass = manager.getChatThread(currentThreadId);

        if (chatClass == null) {
            chatClass = new ChatClass(currentThreadId, new ArrayList<>(), new ArrayList<>());
        }

        chatClass.getMessages().clear();
        chatClass.getIsUserMessage().clear();

        chatClass.getMessages().addAll(messages);
        chatClass.getIsUserMessage().addAll(isUserMessage);

        manager.saveChatThread(chatClass);
    }

    private void loadMessages(String threadId) {
        ChatClass chatClass = SharedPreferencesManager.getInstance(requireContext()).getChatThread(threadId);

        if (chatClass != null) {
            messages.clear();
            isUserMessage.clear();

            messages.addAll(chatClass.getMessages());
            isUserMessage.addAll(chatClass.getIsUserMessage());

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                Log.e("ERROR", "Adapter es nulo.");
            }
        } else {
            Log.d("DEBUG", "No se encontraron mensajes para el thread: " + threadId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}