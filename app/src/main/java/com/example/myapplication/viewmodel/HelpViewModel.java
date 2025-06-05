package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.Ticket;
import com.example.myapplication.model.Usuario;
import com.example.myapplication.R;
import com.example.myapplication.utils.Categoria;
import com.example.myapplication.utils.Prioridad;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> ticketSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> getTicketSuccess() {
        return ticketSuccess;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public HelpViewModel(@NonNull Application application) {
        super(application);
    }

    // Envia el ticket al backend
    public void sendTicket(String category, String priority, String description, Usuario usuario) {
        if (usuario == null) {
            errorMessage.setValue(getApplication().getString(R.string.help_error_login));
            return;
        }

        if (category.equals(getApplication().getString(R.string.select))) {
            errorMessage.setValue(getApplication().getString(R.string.help_pop_cat));
            return;
        }
        if (priority.equals(getApplication().getString(R.string.select))) {
            errorMessage.setValue(getApplication().getString(R.string.help_pop_prio));
            return;
        }
        if (description.isEmpty()) {
            errorMessage.setValue(getApplication().getString(R.string.help_pop_desc));
            return;
        }

        Ticket ticket = new Ticket(usuario, getCategoriaEnum(category), getPrioridadEnum(priority), description);

        ApiService apiService = ApiClient.getClient(getApplication()).create(ApiService.class);
        apiService.sendTicket(ticket).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    ticketSuccess.setValue(true);
                } else {
                    errorMessage.setValue(getApplication().getString(R.string.help_ticket_error));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.setValue(getApplication().getString(R.string.login_conexion));
            }
        });
    }

    // Pasa de String a Enum(Categoria)
    private Categoria getCategoriaEnum(String selectedText) {
        if (selectedText.equals(getApplication().getString(R.string.help_prio_acc))) {
            return Categoria.Cuenta;
        } else if (selectedText.equals(getApplication().getString(R.string.help_prio_assis))) {
            return Categoria.Asistente;
        } else {
            return Categoria.Tecnicos;
        }
    }

    // Pasa de String a Enum(Prioridad)
    private Prioridad getPrioridadEnum(String selectedText) {
        if (selectedText.equals(getApplication().getString(R.string.help_cat_low))) {
            return Prioridad.baja;
        } else if (selectedText.equals(getApplication().getString(R.string.help_cat_med))) {
            return Prioridad.media;
        } else {
            return Prioridad.alta;
        }
    }
}
