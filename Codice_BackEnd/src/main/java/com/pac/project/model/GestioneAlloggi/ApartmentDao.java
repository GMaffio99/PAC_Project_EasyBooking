package com.pac.project.model.GestioneAlloggi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("apartmentRepository")
public interface ApartmentDao extends JpaRepository<Apartment, UUID> {

    Optional<Apartment> findById(UUID id);

    List<Apartment> findByOwnerId(UUID ownerId);

    List<Apartment> findByLocation(String location);

    List<Apartment> findByTags(String tags);

    @Query(value = "select a from Apartment a where numGuests between ?1 and ?1+2")
    List<Apartment> findByNumGuests(int numGuests);

    @Query(value = "select a from Apartment a where pricePerNight <= ?1")
    List<Apartment> findByPricePerNight(double pricePerNight);

    List<Apartment> findAll();

    @Modifying
    @Query("update Apartment ta set ta.numGuests=?2, ta.tags=?3, ta.pricePerNight=?4 where ta.id=?1")
    int updateApartment(UUID id, int numGuests, String tags, double pricePerNight);

    @Modifying
    @Query("delete from Apartment ta where ta.id=?1")
    int deleteApartment(UUID id);

    @Query(value = "select a " +
            "from Apartment a " +
            "where id not in ( " +
            "   select distinct a2.id " +
            "   from Apartment a2 inner join a2.reservations r " +
            "   where r.startReservation between ?1 and ?2 " +
            "   or r.endReservation between ?1 and ?2 " +
            ")")
    List<Apartment> findAvailableApartments(Date startReservation, Date endReservation);

    @Query(value = "select a " +
            "from Apartment a " +
            "where a.numGuests between ?3 and ?3+2 " +
            "and id not in ( " +
            "   select distinct a2.id " +
            "   from Apartment a2 inner join a2.reservations r " +
            "   where r.startReservation between ?1 and ?2 " +
            "   or r.endReservation between ?1 and ?2 " +
            ")")
    List<Apartment> findAvailableApartmentsPerNumGuests(Date startReservation, Date endReservation, int numGuests);

    @Query(value = "select a " +
            "from Apartment a " +
            "where a.pricePerNight <= ?3 " +
            "and id not in ( " +
            "   select distinct a2.id " +
            "   from Apartment a2 inner join a2.reservations r " +
            "   where r.startReservation between ?1 and ?2 " +
            "   or r.endReservation between ?1 and ?2 " +
            ")")
    List<Apartment> findAvailableApartmentsPerMaxPricePerNight(Date startReservation, Date endReservation, double maxPricePerNight);

    @Query(value = "select a " +
            "from Apartment a " +
            "where a.numGuests between ?3 and ?3+2 " +
            "and a.pricePerNight <= ?4 " +
            "and id not in ( " +
            "   select distinct a2.id " +
            "   from Apartment a2 inner join a2.reservations r " +
            "   where r.startReservation between ?1 and ?2 " +
            "   or r.endReservation between ?1 and ?2 " +
            ")")
    List<Apartment> findAvailableApartmentsPerNumGuestsAndMaxPricePerNight(Date startReservation, Date endReservation, int numGuests, double maxPricePerNight);

    @Query(value = "select a " +
            "from Apartment a " +
            "where a.numGuests <= ?3 " +
            "and id not in ( " +
            "   select distinct a2.id " +
            "   from Apartment a2 inner join a2.reservations r " +
            "   where r.startReservation between ?1 and ?2 " +
            "   or r.endReservation between ?1 and ?2 " +
            ")")
    List<Apartment> findAvailableApartmentsPerMaxNumGuests(Date startReservation, Date endReservation, int numGuests);

    @Query(value = "select a " +
            "from Apartment a " +
            "where a.numGuests <= ?3 " +
            "and a.pricePerNight <= ?4 " +
            "and id not in ( " +
            "   select distinct a2.id " +
            "   from Apartment a2 inner join a2.reservations r " +
            "   where r.startReservation between ?1 and ?2 " +
            "   or r.endReservation between ?1 and ?2 " +
            ")")
    List<Apartment> findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(Date startReservation, Date endReservation, int numGuests, double maxPricePerNight);

}