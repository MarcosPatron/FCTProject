package com.example.myapplication.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageRequest { // Clase que crea los datos para enviarlos al backend
    @SerializedName("Message")
    private String mensaje;

    @SerializedName("ThreadId")
    private String threadId;

    @SerializedName("coordinates")
    private List<Double> coordinates; // Tomo las coordenadas en MainActivity L: 113

    public MessageRequest(String mensaje, String threadId, List<Double> coordinates) {
        this.mensaje = mensaje;
        this.threadId = threadId;
        this.coordinates = coordinates;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getThreadId() {
        return threadId;
    }
    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
    public List<Double> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}

