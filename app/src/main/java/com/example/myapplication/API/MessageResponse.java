package com.example.myapplication.API;

public class MessageResponse {
    private String threadId;
    private String mensaje;

    public String getThreadId() {
        return threadId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

