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
        lista.add(new Recommendation("Concatedral de Santa María", "La Concatedral de Santa María es el corazón espiritual del casco antiguo de Cáceres. Su imponente arquitectura gótica, las capillas laterales repletas de arte sacro y el magnífico Cristo Negro —una talla envuelta en leyenda— la convierten en una visita imprescindible. Entrar en ella es como dar un salto en el tiempo: la penumbra, el silencio y la piedra antigua te envuelven, invitándote a descubrir siglos de historia.", R.drawable.ccsc, "https://maps.app.goo.gl/DQ5ryWjrWNotRLrU9"));
        lista.add(new Recommendation("Baluarte de los Pozos", "Situado en una de las zonas más fotogénicas de la judería, el Baluarte de los Pozos es una antigua fortificación que ofrece vistas espectaculares de la parte baja de la ciudad y de la muralla. Su torre, perfectamente restaurada, permite ascender para disfrutar de una panorámica única. Es uno de esos rincones tranquilos y especiales donde Cáceres se revela más íntimo.",R.drawable.baluartedelospozos, "https://maps.app.goo.gl/Hn9t5Ra89yAJZYyX7"));
        lista.add(new Recommendation("Palacio de Carvajal", "El Palacio de Carvajal es uno de los edificios más bellos del casco histórico. Su fachada renacentista y su famosa torre redonda llaman la atención desde lejos, pero lo más encantador está dentro: un jardín interior lleno de vegetación, un auténtico oasis medieval. Pasear por sus salas, hoy convertidas en centro de interpretación, es sumergirse en la nobleza cacereña.", R.drawable.palaciocarvajal, "https://maps.app.goo.gl/Yk7NHWA3zcLqPu5XA"));
        lista.add(new Recommendation("Torre de Bujaco", "La Torre de Bujaco es el auténtico símbolo de Cáceres. Dominando la Plaza Mayor, esta torre almohade del siglo XII impresiona por su presencia y su fortaleza. Subir a su azotea es regalarse una de las mejores vistas de la ciudad: tejados rojizos, murallas, torres medievales… un auténtico viaje visual al Cáceres más histórico.", R.drawable.torrebujaco, "https://maps.app.goo.gl/hz95Ac6kBSL8sBFr6"));
        lista.add(new Recommendation("Palacio y Torre de las Cigüeñas", "Este palacio es un ejemplo perfecto de cómo la historia deja huella en la arquitectura. Su torre —una de las pocas que no fueron desmochadas tras la rebelión de los nobles— mantiene su altura original, coronada casi siempre por cigüeñas, icono de la ciudad. El interior alberga colecciones militares, pero lo que más cautiva es su atmósfera medieval intacta.", R.drawable.torreciguenas, "https://maps.app.goo.gl/TWzK7wP91KWRrmpE6"));
        lista.add(new Recommendation("Plaza Mayor", "Animada, amplia y rodeada de edificios históricos, la Plaza Mayor es la puerta de entrada a la ciudad monumental. Es el lugar ideal para comenzar cualquier recorrido: aquí puedes sentarte en una terraza, disfrutar de las vistas a la muralla y contemplar cómo la Torre de Bujaco, el Arco de la Estrella y las casas porticadas te envuelven en un ambiente único. De noche, iluminada, es pura magia.", R.drawable.plazamayor, "https://maps.app.goo.gl/8GbzSrFrhhyopeFM6"));
        lista.add(new Recommendation("Arco de la Estrella", "Este arco barroco es la entrada más emblemática al casco antiguo. Cruzarlo es como atravesar una frontera temporal: de la vida moderna de la Plaza Mayor pasas directamente a las calles empedradas del medievo y el Renacimiento. Su forma curva, pensada para que pudieran pasar carruajes, lo hace único.", R.drawable.arcoestrella, "https://maps.app.goo.gl/TottiHQ4twGiKmQD7"));
        lista.add(new Recommendation("Casco Histórico", "El casco histórico de Cáceres es uno de los mejor conservados de Europa y Patrimonio de la Humanidad. Pasear por él es perderse entre palacios medievales, torres defensivas, calles estrechas, iglesias y rincones silenciosos donde parece que el tiempo se detuvo. Es un escenario perfecto para amantes de la historia, la fotografía y la arquitectura. No es casualidad que haya sido elegido para rodajes de series y películas.", R.drawable.cascohistorico, "https://maps.app.goo.gl/PHhefUsnUnFbouYR9"));

        RecyclerRecommendations adapter = new RecyclerRecommendations(lista);
        recyclerView.setAdapter(adapter);
    }
}
