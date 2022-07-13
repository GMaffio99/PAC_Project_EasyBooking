package com.pac.project.presenter.GestionePrenotazioni;

import com.pac.project.model.GestionePrenotazioni.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/controller")
public class ReservationController {
    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation/{idApartment}")
    public void insertReservation(@RequestBody RequestReservation request, @PathVariable("idApartment") String idApartment) {
        Reservation reservation = new Reservation(request.getStartReservation(), request.getEndReservation(), UUID.fromString(idApartment), UUID.fromString(request.getUserId()));
        this.reservationService.insertReservation(reservation);
    }

    @GetMapping("/reservation/apartment/{idApartment}")
    public List<Reservation> returnAllReservationsByApartment(@PathVariable("idApartment") String idApartment) {
        return reservationService.returnAllReservationsByApartment(UUID.fromString(idApartment));
    }

    @GetMapping("/reservation/user/{idUser}")
    public List<Reservation> returnAllReservationsByUser(@PathVariable("idUser") String idUser) {
        return reservationService.returnAllReservationsByUser(UUID.fromString(idUser));
    }

    @PutMapping("/reservation/{idReservation}")
    public boolean updateReservationById(@PathVariable("idReservation") String idReservation, @RequestBody RequestUpdateReservation request) {
        return reservationService.updateReservationById(UUID.fromString(idReservation), request.getStartReservation(), request.getEndReservation());
    }

    @DeleteMapping("/reservation/{idReservation}")
    public boolean deleteReservationById(@PathVariable("idReservation") String idReservation) {
        return reservationService.deleteReservationById(UUID.fromString(idReservation));
    }
}
