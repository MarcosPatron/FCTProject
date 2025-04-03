package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Utils.RecyclerMenu;
import com.example.myapplication.Utils.SharedPreferencesManager;
import com.example.myapplication.databinding.FragmentMainMenuBinding;

public class MainMenu extends Fragment {

    private FragmentMainMenuBinding binding;
    private RecyclerMenu adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);

        //SharedPreferencesManager.getInstance(requireContext()).clearAllChats(); // Para eliminar todos los elementos del SharedPreferences

        adapter = new RecyclerMenu(requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.fab.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_main_menu_to_chat);
        });

        // Gesto de deslizar
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                SharedPreferencesManager.getInstance(requireContext()).removeChatThread(adapter.getItem(position));

                adapter.removeItem(position);
                adapter.notifyItemRemoved(position);
            }
        }).attachToRecyclerView(binding.recyclerView);

        
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Actualizar la lista cuando se vuelve al fragmento
        adapter.updateList(requireContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
