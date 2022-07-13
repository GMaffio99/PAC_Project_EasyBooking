package com.pac.project.modelTest;

import com.pac.project.model.GestioneAlloggi.Apartment;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApartmentTest {
    String description = "Villa Imperiale";
    int numGuest = 4;
    String location = "Monza";
    String tags = "città";
    double pricePerNight = 25.0;
    Apartment apartment = new Apartment(description, numGuest, location, tags, pricePerNight);
    Apartment apartment1 = new Apartment();

    @Test
    public void testSetId() {
        UUID expected = UUID.fromString("73b820e6-ad16-4dfe-ac9b-04f2999fed77");
        apartment.setId(expected);
        assertEquals(expected, apartment.getId());
    }

    @Test
    public void testGetLocation() {
        assertEquals(location, apartment.getLocation());
    }

    @Test
    public void testSetTags() {
        String tags1 = "montagna";
        apartment1.setTags(tags1);
        assertEquals(tags1, apartment1.getTags());
    }

    @Test
    public void testInsertReservationInCalendar() {
        Date startReservation = Date.valueOf(LocalDate.of(2022, 2, 11));
        Date endReservation = Date.valueOf(LocalDate.of(2022, 2, 14));
        apartment.insertReservationInCalendar(startReservation, endReservation);
        String prova = "9491a920-f7f2-44b2-84b4-0dcc8ef0953f";
        apartment.setOwner(prova);
        Date actual = apartment.calendar.get(startReservation);
        assertEquals(actual, endReservation);
    }


}
