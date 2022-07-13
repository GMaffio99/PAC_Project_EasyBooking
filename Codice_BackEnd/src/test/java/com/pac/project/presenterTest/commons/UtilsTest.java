package com.pac.project.presenterTest.commons;

import com.pac.project.model.GestioneAlloggi.Apartment;
import com.pac.project.model.GestionePrenotazioni.Reservation;
import com.pac.project.model.GestioneUtenze.User;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.pac.project.presenter.commons.Utils.buildCalendar;
import static com.pac.project.presenter.commons.Utils.checkDate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {

    Apartment apartment = new Apartment("Mare", 20, "Palermo", "tags", 12.6);
    Date in = Date.valueOf(LocalDate.of(2022, 2, 13));
    Date out = Date.valueOf(LocalDate.of(2022, 2, 23));
    User user = new User("MTSDGFH99T5342W", "Paolo", "Bianchi", Date.valueOf(LocalDate.of(1999, 2, 3)), "p.bianchi@gmail.com", "abcd123", true);
    Reservation reservation = new Reservation(in, out, apartment.getId(), user.getId());

    @Test
    public void testCheckDateFalse() {
        Date ins = Date.valueOf(LocalDate.of(2022, 2, 11));
        Date outs = Date.valueOf(LocalDate.of(2022, 2, 17));
        apartment.insertReservationInCalendar(in, out);
        assertFalse(checkDate(apartment, ins, outs));
    }

    @Test
    public void testCheckDateTrue() {
        Date ins = Date.valueOf(LocalDate.of(2022, 2, 8));
        Date outs = Date.valueOf(LocalDate.of(2022, 2, 11));
        apartment.insertReservationInCalendar(in, out);
        assertTrue(checkDate(apartment, ins, outs));
    }

    @Test
    public void testBuildCalendar() {
        Date ins = Date.valueOf(LocalDate.of(2022, 2, 8));
        Date outs = Date.valueOf(LocalDate.of(2022, 2, 15));
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);
        buildCalendar(apartment, reservationList);
        assertFalse(checkDate(apartment, ins, outs));
    }
}
