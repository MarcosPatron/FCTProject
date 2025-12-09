package com.example.myapplication.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Recommendation;

import java.util.List;

public class RecyclerRecommendations extends RecyclerView.Adapter<RecyclerRecommendations.RecomendacionViewHolder> {

    private List<Recommendation> lista;

    public RecyclerRecommendations(List<Recommendation> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public RecomendacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendations_list, parent, false);
        return new RecomendacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecomendacionViewHolder holder, int position) {
        Recommendation recommendation = lista.get(position);
        holder.tvTitulo.setText(recommendation.getTitle());
        holder.imgRecommendation.setImageResource(recommendation.getImgId());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", recommendation.getTitle());
            bundle.putString("description", recommendation.getDescription());
            bundle.putInt("imgId", recommendation.getImgId());
            bundle.putString("textLink",recommendation.getUrl());

            Navigation.findNavController(v)
                    .navigate(R.id.action_recommendations_to_recommendationsInfo, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class RecomendacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        ImageView imgRecommendation;

        public RecomendacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitle);
            imgRecommendation = itemView.findViewById(R.id.bgImage);
        }
    }
}
