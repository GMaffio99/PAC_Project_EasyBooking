package com.pac.project.model.GestioneAlloggi;

import com.pac.project.model.GestionePrenotazioni.Reservation;
import com.pac.project.model.GestioneUtenze.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "t_apartment")
public class Apartment {
    @Column(name = "description")
    public String description;
    @Column(name = "numGuests")
    public int numGuests;
    @Column(name = "location")
    public String location;
    @Column(name = "tags")
    public String tags;
    @Column(name = "pricePerNight")
    public double pricePerNight;
    @Column(name = "imageUrl")
    public String imageUrl;
    @Transient
    //this annotation is needed to not create a column calendar in the DB,
    // creating this column would cause an error since there is no SQL equivalent for the HashMap
    public HashMap<Date, Date> calendar = new HashMap<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ownerId")
    private User owner;
    @OneToMany(targetEntity = Reservation.class, mappedBy = "apartmentId")
    private Set<Reservation> reservations;

    public Apartment() {
    }

    public Apartment(String description, int numGuests, String location, String tags, double pricePerNight) {
        this.description = description;
        this.numGuests = numGuests;
        this.location = location;
        this.tags = tags;
        this.pricePerNight = pricePerNight;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public void setOwner(String id) {
        User entity = new User();
        entity.setId(UUID.fromString(id));
        this.owner = entity;
    }

    public String getLocation() {
        return location;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void insertReservationInCalendar(Date startReservation, Date endReservation) {
        calendar.put(startReservation, endReservation);
    }
}
