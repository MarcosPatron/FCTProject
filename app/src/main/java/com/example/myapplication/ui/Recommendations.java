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

        lista.add(new Recommendation(getString(R.string.concatedral_nombre), getString(R.string.concatedral_desc), R.drawable.ccsc, "https://maps.app.goo.gl/DQ5ryWjrWNotRLrU9"));
        lista.add(new Recommendation(getString(R.string.baluarte_pozos_nombre), getString(R.string.baluarte_pozos_desc), R.drawable.baluartedelospozos, "https://maps.app.goo.gl/Hn9t5Ra89yAJZYyX7"));
        lista.add(new Recommendation(getString(R.string.palacio_carvajal_nombre), getString(R.string.palacio_carvajal_desc), R.drawable.palaciocarvajal, "https://maps.app.goo.gl/Yk7NHWA3zcLqPu5XA"));
        lista.add(new Recommendation(getString(R.string.torre_bujaco_nombre), getString(R.string.torre_bujaco_desc), R.drawable.torrebujaco, "https://maps.app.goo.gl/hz95Ac6kBSL8sBFr6"));
        lista.add(new Recommendation(getString(R.string.torre_ciguenas_nombre), getString(R.string.torre_ciguenas_desc), R.drawable.torreciguenas, "https://maps.app.goo.gl/TWzK7wP91KWRrmpE6"));
        lista.add(new Recommendation(getString(R.string.plaza_mayor_nombre), getString(R.string.plaza_mayor_desc), R.drawable.plazamayor, "https://maps.app.goo.gl/8GbzSrFrhhyopeFM6"));
        lista.add(new Recommendation(getString(R.string.arco_estrella_nombre), getString(R.string.arco_estrella_desc), R.drawable.arcoestrella, "https://maps.app.goo.gl/TottiHQ4twGiKmQD7"));
        lista.add(new Recommendation(getString(R.string.casco_historico_nombre), getString(R.string.casco_historico_desc), R.drawable.cascohistorico, "https://maps.app.goo.gl/PHhefUsnUnFbouYR9"));

        RecyclerRecommendations adapter = new RecyclerRecommendations(lista);
        recyclerView.setAdapter(adapter);
    }
}
