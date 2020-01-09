package com.example.facebook.model;

public class User {

    private String firstname;
    private String lastname;
    private String dob;
    private String phone;
    private String email;
    private String password;
    private String image;

    public User(String firstname, String lastname, String dob, String phone, String email, String password, String image) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.image = image;
    }


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDob() {
        return dob;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImg() {
        return image;
    }
}
