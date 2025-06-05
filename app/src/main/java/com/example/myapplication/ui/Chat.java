package com.example.myapplication.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.utils.RecyclerChat;
import com.example.myapplication.viewmodel.ChatViewModel;
import com.example.myapplication.databinding.FragmentChatBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class Chat extends Fragment {

    private FragmentChatBinding binding;
    private RecyclerChat adapter;
    private ChatViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        adapter = new RecyclerChat(new ArrayList<>(), new ArrayList<>());
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerChat.setAdapter(adapter);

        // Observers
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            adapter = new RecyclerChat(messages, viewModel.getIsUserMessage().getValue());
            binding.recyclerChat.setAdapter(adapter);
            binding.recyclerChat.scrollToPosition(messages.size() - 1);
        });

        viewModel.getIsUserMessage().observe(getViewLifecycleOwner(), flags -> {
            adapter = new RecyclerChat(viewModel.getMessages().getValue(), flags);
            binding.recyclerChat.setAdapter(adapter);
            binding.recyclerChat.scrollToPosition(flags.size() - 1);
        });

        // Obtener threadId y username desde argumentos
        String threadId = getArguments() != null ? getArguments().getString("threadId") : null;
        String username = getArguments() != null ? getArguments().getString("username") : "invitado";

        if (threadId != null) {
            viewModel.loadMessages(threadId);
        }

        binding.btnButtom.setOnClickListener(v -> {
            String msg = binding.consulta.getText().toString().trim();
            if (TextUtils.isEmpty(msg)) {
                return;
            }

            binding.consulta.setText("");
            viewModel.sendMessage(username, msg, Arrays.asList(MainActivity.latitude, MainActivity.longitude));
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
