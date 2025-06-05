package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.utils.RecyclerMenu;
import com.example.myapplication.viewmodel.MainMenuViewModel;
import com.example.myapplication.databinding.FragmentMainMenuBinding;

public class MainMenu extends Fragment {

    private FragmentMainMenuBinding binding;
    private RecyclerMenu adapter;
    private MainMenuViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);

        adapter = new RecyclerMenu(requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        // Observer de los threadIds
        viewModel.getThreadIds().observe(getViewLifecycleOwner(), threadIds -> {
            adapter.updateList(requireContext()); // Actualiza desde SharedPreferences
        });

        // Abrir drawer
        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Nuevo chat
        binding.fab.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_main_menu_to_chat);
        });

        // Deslizar para eliminar
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String threadId = adapter.getItem(position);

                viewModel.deleteThread(threadId);
            }
        }).attachToRecyclerView(binding.recyclerView);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadThreadIds();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
