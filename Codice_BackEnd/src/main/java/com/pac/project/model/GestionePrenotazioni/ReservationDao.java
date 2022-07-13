package com.pac.project.model.GestionePrenotazioni;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("reservationRepository")
public interface ReservationDao extends JpaRepository<Reservation, UUID> {

    Optional<Reservation> findById(UUID id);

    //servirà per recuperare le prenotazioni da mettere nella pagina del cliente, che giustamente vuole vedere le sue prenotazioni
    List<Reservation> findByUserId(UUID idUser);

    //recuperare le prenotazioni di un appartamento servirà all'owner per vedere quante prenotazioni ha "venduto" e servirà per valorizzare
    //il calendario di un appartamento così da restituire semplicemente gli appartamenti ancora disponibili
    List<Reservation> findByApartmentId(UUID apartment);

    //servirà per un cliente per poter aggiornare una propria prenotazione
    @Modifying
    @Query("update Reservation tr set tr.startReservation = ?2, tr.endReservation = ?3 where tr.id = ?1")
    int updateReservationById(UUID id, Date newStartReservation, Date newEndReservation);

    //servirà per un cliente per poter eliminare una propria prenotazione
    @Modifying
    @Query("delete from Reservation tr where tr.id=?1")
    int deleteReservationById(UUID id);
}
