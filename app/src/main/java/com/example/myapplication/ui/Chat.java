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

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentChatBinding;
import com.example.myapplication.utils.RecyclerChat;
import com.example.myapplication.viewmodel.ChatViewModel;

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

        // Crear adapter una sola vez (MUY IMPORTANTE)
        adapter = new RecyclerChat(new ArrayList<>(), new ArrayList<>());
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerChat.setAdapter(adapter);

        // Observa los mensajes
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            adapter.updateData(messages, viewModel.getIsUserMessage().getValue());
            binding.recyclerChat.scrollToPosition(messages.size() - 1);
        });

        // Observa flags (user o sistema)
        viewModel.getIsUserMessage().observe(getViewLifecycleOwner(), flags -> {
            adapter.updateData(viewModel.getMessages().getValue(), flags);
            binding.recyclerChat.scrollToPosition(flags.size() - 1);
        });

        // Observa si se puede enviar
        viewModel.getCanSend().observe(getViewLifecycleOwner(), canSend -> {
            binding.btnButtom.setEnabled(canSend);
        });

        // Obtener argumentos
        String threadId = getArguments() != null ? getArguments().getString("threadId") : null;
        String username = getArguments() != null ? getArguments().getString("username") : "invitado";

        if (threadId != null) {
            viewModel.loadMessages(threadId);
        }

        // Botón enviar
        binding.btnButtom.setOnClickListener(v -> {

            if (!Boolean.TRUE.equals(viewModel.getCanSend().getValue()))
                return; // bloquear mientras no llegue respuesta

            String msg = binding.consulta.getText().toString().trim();
            if (TextUtils.isEmpty(msg)) return;

            // Límite para invitados
            if (!viewModel.canGuestSendMessage(username)) {
                binding.consulta.setText("");
                binding.consulta.setHint(R.string.max_msgs);
                return;
            }

            binding.consulta.setText("");
            viewModel.sendMessage(username, msg, Arrays.asList(
                    MainActivity.latitude,
                    MainActivity.longitude
            ));
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
