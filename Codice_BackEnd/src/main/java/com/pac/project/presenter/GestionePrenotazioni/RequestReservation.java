package com.pac.project.presenter.GestionePrenotazioni;

import lombok.Data;

import java.sql.Date;

@Data
public class RequestReservation {
    Date startReservation;
    Date endReservation;
    String userId;
}
