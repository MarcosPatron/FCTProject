package com.example.myapplication.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.ChatClass;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerMenu extends RecyclerView.Adapter<RecyclerMenu.ThreadViewHolder> {

    private List<String> threadList;
    private Context context;

    public RecyclerMenu(Context context) {
        this.context = context;
        this.threadList = loadThreadList(context);
    }

    // Carga los hilos de SharedPreferences y elimina los chats si no hay sesión
    private List<String> loadThreadList(Context context) {
        SharedPreferencesManager manager = SharedPreferencesManager.getInstance(context);
        Map<String, ChatClass> chatMap = manager.getChatThreadsMap();
        List<String> threads = new ArrayList<>();

        boolean sesionActiva = Usuario.sesionActiva(context);

        for (Map.Entry<String, ChatClass> entry : chatMap.entrySet()) {
            String threadId = entry.getKey();
            if (sesionActiva) {
                threads.add(threadId); // Solo añadimos si hay sesión
            } else {
                // Si no hay sesión, eliminamos inmediatamente
                manager.removeChatThread(threadId);
            }
        }
        return threads;
    }

    public void updateList(Context context) {
        this.threadList = loadThreadList(context);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
        String threadId = threadList.get(position);
        holder.tvTitle.setText(R.string.chat_del_asistente);
        holder.tvThreadId.setText(threadId);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("threadId", threadId);
            Navigation.findNavController(v).navigate(R.id.action_main_menu_to_chat, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return threadList != null ? threadList.size() : 0;
    }

    public void removeItem(int position) {
        threadList.remove(position);
        notifyItemRemoved(position);
    }

    public String getItem(int position) {
        return threadList.get(position);
    }

    static class ThreadViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvThreadId;

        public ThreadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvThreadId = itemView.findViewById(R.id.tvThreadId);
        }
    }
}
