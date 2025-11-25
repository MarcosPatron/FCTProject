package com.example.myapplication.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentMainMenuBinding;
import com.example.myapplication.databinding.FragmentRecommendationsBinding;
import com.example.myapplication.model.Recommendation;
import com.example.myapplication.utils.RecyclerRecommendations;

import java.util.ArrayList;
import java.util.List;

public class Recommendations extends Fragment {

    private FragmentRecommendationsBinding binding;
    private RecyclerView recyclerView;

    public Recommendations() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecommendationsBinding.inflate(inflater, container, false);

        // Abrir drawer
        binding.btnOpenDrawer.setOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerRec);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Recommendation> lista = new ArrayList<>();
        lista.add(new Recommendation("Concatedral de Santa María", R.drawable.ccsc));
        lista.add(new Recommendation("Baluarte de los Pozos", R.drawable.baluartedelospozos));
        lista.add(new Recommendation("Palacio de Carvajal", R.drawable.palaciocarvajal));
        lista.add(new Recommendation("Torre de Bujaco", R.drawable.torrebujaco));
        lista.add(new Recommendation("Palacio y Torre de las Cigüeñas", R.drawable.torreciguenas));
        lista.add(new Recommendation("Plaza Mayor", R.drawable.plazamayor));
        lista.add(new Recommendation("Arco de la Estrella", R.drawable.arcoestrella));
        lista.add(new Recommendation("Casco Histórico", R.drawable.cascohistorico));

        RecyclerRecommendations adapter = new RecyclerRecommendations(lista);
        recyclerView.setAdapter(adapter);
    }
}
