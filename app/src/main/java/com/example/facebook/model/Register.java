package com.example.facebook.model;

public class Register {


    private String fname;
    private String lname;
    private String dob;
    private String phone;
    private String email;
    private String password;


    public Register(String fname, String lname, String dob, String phone, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.password = password;

    }


    public String getFirstname() {
        return fname;
    }

    public String getLastname() {
        return lname;
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


}
