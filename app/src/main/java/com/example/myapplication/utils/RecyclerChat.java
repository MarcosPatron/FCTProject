package com.example.myapplication.utils;

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
    private List<Boolean> isUserMessage;

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
                ? R.layout.chat_item_user
                : R.layout.chat_item_received;

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String message = messages.get(position);
        holder.tvMessage.setText(message);

        // Animación ligera solo si es "Procesando petición..."
        if (message.equals("Procesando petición...") || message.equals("Procesing request...")) {
            holder.tvMessage.animate()
                    .alpha(0.5f)
                    .setDuration(800)
                    .withEndAction(() -> holder.tvMessage.animate()
                            .alpha(1f)
                            .setDuration(800)
                            .start())
                    .start();
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

    // Método para actualizar datos sin recrear el adapter
    public void updateData(List<String> newMessages, List<Boolean> newFlags) {
        this.messages = newMessages;
        this.isUserMessage = newFlags;
        notifyDataSetChanged();
    }
}
