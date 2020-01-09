package com.example.facebook.model;

public class Response {

    private boolean success;
    private String message;
    private String token;
    private User user;

    public Response(boolean success, String message, String token, User user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
