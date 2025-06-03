package com.example.myapplication.api;

public class MessageResponse {
    private String threadId;
    private String message;
    private String description;


    public MessageResponse(String threadId, String message, String description) {
        this.threadId = threadId;
        this.message = message;
        this.description = description;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

