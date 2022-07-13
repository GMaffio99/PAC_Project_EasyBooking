package com.pac.project.presenter.GestioneAlloggi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestUpdateApartment {
    int numGuests;
    String tags;
    double pricePerNight;
}