package com.pac.project.model.GestioneAlloggi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrappedApartment {
    Apartment a;
    int order;
    int total;
}
