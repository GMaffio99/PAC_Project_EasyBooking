package com.pac.project.model.GestionePrenotazioni;

import com.pac.project.model.GestioneAlloggi.Apartment;
import com.pac.project.model.GestioneUtenze.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "t_reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId")
    private User user;
    @Column(name = "startReservation")
    public Date startReservation;
    @Column(name = "endReservation")
    public Date endReservation;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "apartmentId")
    private Apartment apartmentId;

    protected Reservation() {
    }

    public Reservation(Date startReservation, Date endReservation, UUID apartment, UUID user) {
        User entityC = new User();
        entityC.setId(user);
        this.user = entityC;

        Apartment entityA = new Apartment();
        entityA.setId(apartment);
        this.apartmentId = entityA;

        this.startReservation = startReservation;
        this.endReservation = endReservation;
    }

    public UUID getId(){return id;}

    public Date getEndReservation() {
        return endReservation;
    }

    public Date getStartReservation() {
        return startReservation;
    }

    public Apartment getApartment() {
        return apartmentId;
    }

}
