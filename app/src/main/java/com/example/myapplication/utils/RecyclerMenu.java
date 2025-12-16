package com.example.myapplication.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.ChatClass;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerMenu extends RecyclerView.Adapter<RecyclerMenu.ThreadViewHolder> {

    private List<String> threadList;

    public RecyclerMenu(Context context) {
        this.threadList = loadThreadList(context);
    }

    // Saca los hilos de SharedPreferences
    private List<String> loadThreadList(Context context) {
        SharedPreferencesManager manager = SharedPreferencesManager.getInstance(context);
        Map<String, ChatClass> chatMap = manager.getChatThreadsMap();
        return new ArrayList<>(chatMap.keySet());
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
        holder.tvTitle.setText("Restaurante para comer");

        holder.tvThreadId.setText(threadId);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("threadId", threadId);
            Navigation.findNavController(v).navigate(R.id.action_main_menu_to_chat, bundle);
        });
    }

    // Toma el tamaño de la lista
    @Override
    public int getItemCount() {
        return threadList != null ? threadList.size() : 0;
    }

    // Eliminar item de la lista
    public void removeItem(int position) {
        threadList.remove(position);
        notifyItemRemoved(position);
    }

    // Toma item de la lista
    public String getItem(int position) {
        return threadList.get(position);
    }

    // Clase con los datos que muestra el menu principal en la UI
    static class ThreadViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvThreadId;

        public ThreadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvThreadId = itemView.findViewById(R.id.tvThreadId);
        }
    }
}
