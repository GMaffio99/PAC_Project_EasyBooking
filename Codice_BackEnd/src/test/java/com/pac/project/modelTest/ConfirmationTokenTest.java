package com.pac.project.modelTest;

import com.pac.project.model.GestioneUtenze.ConfirmationToken;
import com.pac.project.model.GestioneUtenze.User;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfirmationTokenTest {
    String token = "prova";
    LocalDateTime createdAt = LocalDateTime.of(2022, 3, 15, 12, 43);
    LocalDateTime expiresAt = LocalDateTime.of(2022, 3, 18, 12, 43);
    User user = new User("MTTRSS99P27C526K", "Matteo", "Rossi", Date.valueOf(LocalDate.of(1999, 2, 11)), "m.rossi@gmail.com", "abc123", false);
    ConfirmationToken cft = new ConfirmationToken(token, createdAt, expiresAt, user);
    UUID id = UUID.fromString("9491a920-f7f2-44b2-84b4-0dcc8ef0953f");
    ConfirmationToken cft1 = new ConfirmationToken();

    @Test
    public void testSetIdUser() {
        cft.setId(id);
        assertEquals(id, cft.getId());
    }

    @Test
    public void testGetToken() {
        assertEquals(token, cft.getToken());
    }

    @Test
    public void testSetCreatedAt() {
        LocalDateTime test = LocalDateTime.of(2022, 7, 7, 12, 43);
        cft1.setCreatedAt(test);
        assertEquals(test, cft1.getCreatedAt());
    }

    @Test
    public void testSetConfirmedAt() {
        LocalDateTime test = LocalDateTime.of(2022, 8, 8, 12, 43);
        cft.setConfirmedAt(test);
        assertEquals(test, cft.getConfirmedAt());
    }

    @Test
    public void testSetExpiresAt() {
        LocalDateTime test = LocalDateTime.of(2022, 9, 23, 12, 43);
        cft.setExpiresAt(test);
        assertEquals(test, cft.getExpiresAt());
    }

    @Test
    public void testGetUser() {
        assertEquals(user, cft.getUser());
    }

}

