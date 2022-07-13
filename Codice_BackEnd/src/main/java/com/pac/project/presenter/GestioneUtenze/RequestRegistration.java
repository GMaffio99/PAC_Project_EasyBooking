package com.pac.project.presenter.GestioneUtenze;

import java.sql.Date;

public class RequestRegistration {

    String cf;
    String name;
    String surname;
    Date dob;
    String email;
    String pwd;
    boolean accountType;

    public RequestRegistration(String cf, String name, String surname, Date dob, String email, String pwd) {
        this.cf = cf;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.pwd = pwd;
        this.accountType = false;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean getAccountType() {
        return this.accountType;
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

    public Date getDob() {
        return dob;
    }

    public String getPwd() {
        return pwd;
    }
}