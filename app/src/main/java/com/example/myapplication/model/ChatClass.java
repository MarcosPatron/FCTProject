package com.example.myapplication.model;

import java.util.List;

public class ChatClass { // Clase para crear el chat del asistente
    private String threadId;
    private List<String> messages;
    private List<Boolean> isUserMessage;

    public ChatClass(String threadId, List<String> messages, List<Boolean> isUserMessage) {
        this.threadId = threadId;
        this.messages = messages;
        this.isUserMessage = isUserMessage;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<Boolean> getIsUserMessage() {
        return isUserMessage;
    }

    public void setIsUserMessage(List<Boolean> isUserMessage) {
        this.isUserMessage = isUserMessage;
    }
}

