package com.example.myapplication.Classes;

public class SearchBody {
    int numGuests;
    String location;
    double maxDistance;
    String tags;
    double maxPricePerNight;
    String prenotationStart;
    String prenotationEnd;
    int multipleApartments;

    public SearchBody(int numGuests, String location, double maxDistance, String tags, double maxPricePerNight, String prenotationStart, String prenotationEnd) {
        this.numGuests = numGuests;
        this.location = location;
        this.maxDistance = maxDistance;
        this.tags = tags;
        this.maxPricePerNight = maxPricePerNight;
        this.prenotationStart = prenotationStart;
        this.prenotationEnd = prenotationEnd;
        this.multipleApartments = 0;
    }

    public SearchBody(int numGuests, String location, double maxDistance, String tags, double maxPricePerNight, String prenotationStart, String prenotationEnd, int multipleApartments) {
        this.numGuests = numGuests;
        this.location = location;
        this.maxDistance = maxDistance;
        this.tags = tags;
        this.maxPricePerNight = maxPricePerNight;
        this.prenotationStart = prenotationStart;
        this.prenotationEnd = prenotationEnd;
        this.multipleApartments = multipleApartments;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public String getLocation() {
        return location;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public String getTags() {
        return tags;
    }

    public double getMaxPricePerNight() {
        return maxPricePerNight;
    }

    public String getPrenotationStart() {
        return prenotationStart;
    }

    public String getPrenotationEnd() {
        return prenotationEnd;
    }

    public int getMultipleApartments() { return multipleApartments; }
}
