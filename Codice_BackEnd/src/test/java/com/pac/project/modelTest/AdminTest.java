package com.pac.project.modelTest;


import com.pac.project.model.GestioneUtenze.Admin;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminTest {
    String cf = "MTRSMN";
    String name = "Paolo";
    String surname = "Rossi";
    String email = "paolo.rossi@gmail.com";
    String pwd = "abc123";
    Date dob = Date.valueOf(LocalDate.of(2022, 2, 11));
    Admin admin = new Admin(cf, name, surname, email, pwd, dob);


    @Test
    public void testSetId() {
        UUID expected = UUID.fromString("73b820e6-ad16-4dfe-ac9b-04f2999fed77");
        Admin publicAdmin = new Admin();
        publicAdmin.setId(expected);
        assertEquals(expected, publicAdmin.getId());
    }

    @Test
    public void testGetCf() {
        String expected = "MTRSMN";
        assertEquals(expected, admin.getCf());
    }


    @Test
    public void testSetName() {
        String expected = "Ivan";
        admin.setName(expected);
        assertEquals(expected, admin.getName());
    }

    @Test
    public void testSetSurname() {
        String expected = "Bianchi";
        admin.setSurname(expected);
        assertEquals(expected, admin.getSurname());
    }

    @Test
    public void testSetEmail() {
        String expected = "ivan.bianchi@gmail.com";
        admin.setEmail(expected);
        assertEquals(expected, admin.getEmail());
    }

    @Test
    public void testSetDob() {
        Date expected1 = Date.valueOf(LocalDate.of(2023, 2, 11));
        admin.setDob(expected1);
        assertEquals(expected1, admin.getDob());
    }


    @Test
    public void testSetLoggedIn() {
        admin.setLoggedIn(true);
        assertTrue(admin.loggedIn);
    }
}