package com.example.myapplication.Classes;

public class Reservation {

    public String startReservation;

    public String endReservation;

    private String apartmentId;

    private String userId;

    public Reservation(String startReservation, String endReservation, String apartmentId, String userId) {
        this.startReservation = startReservation;
        this.endReservation = endReservation;
        this.apartmentId = apartmentId;
        this.userId = userId;
    }

    public String getStartReservation() {
        return startReservation;
    }

    public String getEndReservation() {
        return endReservation;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getUserId() {
        return userId;
    }
}
