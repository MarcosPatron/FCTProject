package com.example.myapplication.utils;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class RecyclerChat extends RecyclerView.Adapter<RecyclerChat.ChatViewHolder> {

    private List<String> messages;
    private List<Boolean> isUserMessage; // Si es mensaje del usuario = true

    public RecyclerChat(List<String> messages, List<Boolean> isUserMessage) {
        this.messages = messages;
        this.isUserMessage = isUserMessage;
    }

    @Override
    public int getItemViewType(int position) {
        return isUserMessage.get(position) ? 1 : 0;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (viewType == 1)
                ? R.layout.chat_item_user // Mensaje del usuario, posicion derecha
                : R.layout.chat_item_received; // Mensaje recibido, posicion izquierda
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String message = messages.get(position);
        holder.tvMessage.setText(message);

        // Poner un mensaje para hacer saber que se esta procesando la petición
        if(holder.tvMessage.getText().equals("Procesando petición...") || holder.tvMessage.getText().equals("Procesing request...")){
            ObjectAnimator anim = ObjectAnimator.ofArgb(holder.tvMessage, "textColor", Color.DKGRAY, Color.LTGRAY);
            anim.setDuration(800);
            anim.setRepeatMode(ObjectAnimator.REVERSE);
            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.start();
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}

