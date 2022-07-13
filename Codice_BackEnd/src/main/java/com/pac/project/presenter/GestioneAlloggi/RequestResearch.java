package com.pac.project.presenter.GestioneAlloggi;

import lombok.Data;

import java.sql.Date;

@Data
public class RequestResearch {
    int numGuests;
    String location;
    int maxDistance; //unita' di misura: m
    //se l'utente non inserisce maxDistance, mi aspetto nella richiesta il valore 0
    String tags; //se l'utente non inserisce tags, mi aspetto nella richiesta una stringa vuota ""
    double maxPricePerNight; //se l'utente non inserisce maxPricePerNight, mi aspetto nella richiesta il valore 0
    Date prenotationStart;
    Date prenotationEnd;
    int multipleApartments; // 1 se accetta gruppi di appartamenti, 0 se accetta solo singoli appartamenti
}