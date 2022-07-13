package com.example.myapplication.Classes;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class User {

    public String id;

    public String cf;

    public String name;

    public String surname;

    public String dob;

    public String email;

    public String pwd;

    public User(String id, String cf, String name, String surname, String dob, String email, String pwd) {
        this.id = id;
        this.cf = cf;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.pwd = pwd;
    }

    public User(String cf, String name, String surname, String dob, String email, String pwd) {
        this.id = "0";
        this.cf = cf;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.pwd = pwd;
    }

    public String getCf() {
        return cf;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDob() { return dob; }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }
}
