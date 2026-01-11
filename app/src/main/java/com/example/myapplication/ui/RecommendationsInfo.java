package com.example.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRecommendationsInfoBinding;

public class RecommendationsInfo extends Fragment {

    private FragmentRecommendationsInfoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecommendationsInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Recibir datos del bundle
        if (getArguments() != null) {
            String title = getArguments().getString("title");
            String description = getArguments().getString("description");
            int imgId = getArguments().getInt("imgId");
            String url = getArguments().getString("textLink");

            binding.textPlaceName.setText(title);
            binding.textDescription.setText(description);
            binding.imagePlace.setImageResource(imgId);


            binding.textLink.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }

        binding.buttonAction.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_recommendationsInfo_to_chat);
        });

        binding.btnBack.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_recommendationsInfo_to_recommendations);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; //memory leaks
    }
}
