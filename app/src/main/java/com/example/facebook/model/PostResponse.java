package com.example.facebook.model;

public class PostResponse {

    private boolean success;
    private String message;

    private Post post;

    public PostResponse(boolean success, String message, Post post) {
        this.success = success;
        this.message = message;

        this.post = post;
    }


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }


    public Post getPost() {
        return post;
    }
}
