package com.pac.project.model.GestioneUtenze;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "t_admin")
public class Admin {
    @Column(name = "cf")
    public String cf;
    @Column(name = "name")
    public String name;
    @Column(name = "surname")
    public String surname;
    @Column(name = "dob")
    public Date dob;
    @Column(name = "email")
    public String email;
    public Boolean loggedIn;
    @Column(name = "pwd")
    protected String pwd;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public Admin() {
    }

    public Admin(String cf, String name, String surname, String email, String pwd, Date dob) {
        this.cf = cf;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pwd = pwd;
        this.dob = dob;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCf() {
        return cf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }


    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
