package com.pac.project.presenter.GestionePrenotazioni;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class RequestUpdateReservation {
    Date startReservation;
    Date endReservation;
}
