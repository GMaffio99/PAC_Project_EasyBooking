package com.pac.project.presenter.GestionePrenotazioni;

import com.pac.project.model.GestioneAlloggi.Apartment;
import com.pac.project.model.GestioneAlloggi.ApartmentDao;
import com.pac.project.model.GestionePrenotazioni.Reservation;
import com.pac.project.model.GestionePrenotazioni.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.pac.project.presenter.commons.Utils.buildCalendar;
import static com.pac.project.presenter.commons.Utils.checkDate;

@Service("ReservationService")
@Transactional
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ApartmentDao apartmentDao;


    @Autowired
    public ReservationService(@Qualifier("reservationRepository") ReservationDao reservationDao, @Qualifier("apartmentRepository") ApartmentDao apartmentDao) {
        this.reservationDao = reservationDao;
        this.apartmentDao = apartmentDao;
    }

    public void insertReservation(Reservation reservation) {
        // tecnicamente non c'è nessun controllo da fare, se io sono arrivato
        // a fare la prenotazione è perché l'appartamento è apparso nella ricerca, e se
        // è apparso significa che è disponibile per le date cercate
        reservationDao.save(reservation);
    }

    public List<Reservation> returnAllReservationsByApartment(UUID idApartment) {
        return reservationDao.findByApartmentId(idApartment);
    }

    public List<Reservation> returnAllReservationsByUser(UUID idUser) {
        return reservationDao.findByUserId(idUser);
    }

    public boolean updateReservationById(UUID id, Date newStart, Date newEnd) {
        //se voglio cambiare le date di una prenotazione, devo prima controllare che le nuove
        //date siano disponibili, quindi prima recupero la lista di prenotazioni, costruisco il calendario
        //dal vecchio calendario tolgo le vecchie date e vedo se posso inserire le nuove, se posso faccio
        //l'update e restituisco true, altrimenti restituisco false e a front-end in base al booleano
        //confermiamo l'azione oppure (in caso di false) chiediamo se vuole eliminare la prenotazione
        Optional<Reservation> entity = reservationDao.findById(id);
        if (entity.isPresent()) { //controllo che il risultato della query non sia nullo
            //devo recuperare l'appartamento tramite la chiave esterna
            Apartment apartment = (entity.get().getApartment());
            // devo valorizzare la sua lista di prenotazioni
            List<Reservation> reservationList = reservationDao.findByApartmentId(apartment.getId());
            //tolgo la prenotazione attuale che vogliamo aggiornare
            for (Reservation res : reservationList) {
                if (res.getStartReservation() == entity.get().getStartReservation() && res.getEndReservation() == entity.get().getEndReservation()) {
                    reservationList.remove(res);
                    break; //ho rimosso, posso uscire dal ciclo
                }
            }
            if (!reservationList.isEmpty()) {
                buildCalendar(apartment, reservationList);
                if (checkDate(apartment, newStart, newEnd)) {
                    //se entriamo in questo IF checkDate ha restituito true, dunque si può fare la modifica delle date e procedo appunto ad aggiornare la tabella
                    reservationDao.updateReservationById(id, newStart, newEnd);
                    //abbiamo finito e confermo tutto a front-end
                    return true;
                }
            }
            //o non è stata trovata la prenotazione (strano) o non è stato trovato l'appartamento (strano)
            return false;
        }
        return false;
    }

    public boolean deleteReservationById(UUID id) {
        int esito = reservationDao.deleteReservationById(id);
        return esito != 0;
    }
}
