package com.pac.project.modelTest;


import com.pac.project.model.GestioneAlloggi.Apartment;
import com.pac.project.model.GestionePrenotazioni.Reservation;
import com.pac.project.model.GestioneUtenze.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReservationTest {
    Date startReservation = Date.valueOf(LocalDate.of(2022, 2, 11));
    Date endReservation = Date.valueOf(LocalDate.of(2022, 2, 15));
    User user = new User("PLBNCH97T646L", "Paolo", "Bianchi", Date.valueOf(LocalDate.of(1999, 2, 11)), "p.bianchi@gmail.com", "abc123", false);
    Apartment apartment = new Apartment("Sea", 7, "Palermo", "sea", 13.5);
    Reservation reservation = new Reservation(startReservation, endReservation, apartment.getId(), user.getId());


    @Test
    void testGetId() {
        UUID prova = user.getId();
        assertEquals(prova, reservation.getId());
    }

    @Test
    void testGetEndReservation() {
        assertEquals(endReservation, reservation.getEndReservation());
    }

    @Test
    void testGetStartReservation() {
        assertEquals(startReservation, reservation.getStartReservation());
    }

    @Test
    void testGetApartment() {
        assertEquals(apartment.getId(), reservation.getApartment().getId());
    }

    @Test
    void protectedConstructorTest() throws Exception {
        Constructor<Reservation> c = Reservation.class.getDeclaredConstructor();
        c.setAccessible(true);
        Reservation constructed = c.newInstance();
        assertNotNull(constructed);
    }
}
