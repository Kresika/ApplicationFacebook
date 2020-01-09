package com.example.facebook.model;

public class Post {
    private User userid;
    private String caption;
    private String image;
    private String creared_at;


    public Post(String caption, String image, String creared_at, User userid) {
        this.caption = caption;
        this.image = image;
        this.creared_at = creared_at;
        this.userid = userid;
    }

    public User getUser() {
        return userid;
    }

    public String getCaption() {
        return caption;
    }

    public String getImage() {
        return image;
    }

    public String getCreared_at() {
        return creared_at;
    }
}
