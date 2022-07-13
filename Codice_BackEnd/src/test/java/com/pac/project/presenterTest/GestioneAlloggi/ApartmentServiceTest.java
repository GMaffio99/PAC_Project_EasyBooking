package com.pac.project.presenterTest.GestioneAlloggi;

import com.pac.project.model.GestioneAlloggi.Apartment;
import com.pac.project.model.GestioneAlloggi.ApartmentDao;
import com.pac.project.presenter.GestioneAlloggi.ApartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = ApartmentService.class)
class ApartmentServiceTest {

    @Autowired
    ApartmentService serviceToTest;

    @MockBean
    @Qualifier("apartmentRepository")
    ApartmentDao apartmentRepository;

    @Test
    void insertApartment() {
        boolean output = serviceToTest.insertApartment(apartmentBuilder(), String.valueOf(UUID.randomUUID()));
        verify(apartmentRepository, times(1)).save(any(Apartment.class));
        assertTrue(output);
    }

    @Test
    void insertApartmentWithNullInput() {
        boolean output2 = serviceToTest.insertApartment(null, "");
        //in questo caso verifico che NON sia stata effettuata la INSERT a DB
        verify(apartmentRepository, times(0)).save(any());
        assertFalse(output2);
    }

    @Test
    void singleResearch() throws Exception {
        int numGuests = 1;
        double maxPricePerNight = 10.50;
        String tags = "CITY";
        String location = "Dalmine";
        Date start=Date.valueOf(LocalDate.of(2022, 10, 10));
        Date end=Date.valueOf(LocalDate.of(2022, 10, 20));
        List<Apartment> result = new ArrayList<>();
        result.add(apartmentBuilder());
        result.get(0).setTags("CITY");
        when(apartmentRepository.findAvailableApartmentsPerNumGuestsAndMaxPricePerNight(start,
                end,
                numGuests,
                maxPricePerNight)).thenReturn(result);

        serviceToTest.singleResearch(numGuests,
                location,
                10,
                tags,
                maxPricePerNight,
                start,
                end
        );
        //verifico che sia stata chiamata la query corretta
        verify(apartmentRepository, times(1)).findAvailableApartmentsPerNumGuestsAndMaxPricePerNight(any(), any(), eq(numGuests), eq(maxPricePerNight));
        //verifico che le altre query non siano state chiamate
        verify(apartmentRepository, times(0)).findAvailableApartmentsPerMaxPricePerNight(any(), any(), eq(maxPricePerNight));
        verify(apartmentRepository, times(0)).findAvailableApartmentsPerNumGuests(any(), any(), eq(numGuests));
        verify(apartmentRepository, times(0)).findAvailableApartments(any(), any());
    }

    @Test
    void multipleResearchTest1() throws Exception {
        int numGuests = 10;
        String location = "Dalmine";
        Date startReservation=Date.valueOf(LocalDate.of(2022, 10, 10));
        Date endReservation=Date.valueOf(LocalDate.of(2022, 10, 20));
        int maxDistance = 100;
        String tags = "CITY";
        Double maxPricePerNight = 25.5;
        List<Apartment> result = new ArrayList<>();
        Apartment a1 = new Apartment(
                "",
                10,
                "Dalmine",
                "CITY",
                25.5
        );
        Apartment a2 = apartmentBuilder2();
        result.add(a1);
        result.add(a2);
        when(apartmentRepository.findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(
                startReservation,
                endReservation,
                numGuests,
                maxPricePerNight)).thenReturn(result);
        serviceToTest.multipleResearch(numGuests, location, maxDistance, tags, maxPricePerNight, startReservation, endReservation);
        verify(apartmentRepository, times(1)).findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(any(), any(), eq(numGuests), eq(25.5));
    }

    @Test
    void multipleResearchTest2() throws Exception {
        int numGuests = 1;
        String location = "Dalmine";
        Date startReservation=Date.valueOf(LocalDate.of(2022, 10, 10));
        Date endReservation=Date.valueOf(LocalDate.of(2022, 10, 20));
        int maxDistance = 0;
        String tags = "CITY";
        Double maxPricePerNight = 25.5;
        List<Apartment> result = new ArrayList<>();
        Apartment a1 = apartmentBuilder();
        Apartment a2 = apartmentBuilder2();
        result.add(a1);
        result.add(a2);
        when(apartmentRepository.findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(
                startReservation,
                endReservation,
                numGuests,
                maxPricePerNight)).thenReturn(result);
        serviceToTest.multipleResearch(numGuests, location, maxDistance, tags, maxPricePerNight, startReservation, endReservation);
        verify(apartmentRepository, times(1)).findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(any(), any(), eq(1), eq(25.5));
    }

    @Test
    void multipleResearchTest3() throws Exception {
        int numGuests = 1;
        String location = "Dalmine";
        Date startReservation=Date.valueOf(LocalDate.of(2022, 10, 10));
        Date endReservation=Date.valueOf(LocalDate.of(2022, 10, 20));
        int maxDistance = 100;
        String tags = "CITY";
        Double maxPricePerNight = 0.0;
        List<Apartment> result = new ArrayList<>();
        Apartment a1 = apartmentBuilder();
        Apartment a2 = apartmentBuilder2();
        result.add(a1);
        result.add(a2);
        when(apartmentRepository.findAvailableApartmentsPerMaxNumGuests(
                startReservation,
                endReservation,
                numGuests)).thenReturn(result);
        serviceToTest.multipleResearch(numGuests, location, maxDistance, tags, maxPricePerNight, startReservation, endReservation);
        verify(apartmentRepository, times(1)).findAvailableApartmentsPerMaxNumGuests(any(), any(), eq(1));
    }

    @Test
    void multipleResearchTest4() throws Exception {
        int numGuests = 11;
        String location = "Dalmine";
        Date startReservation=Date.valueOf(LocalDate.of(2022, 10, 10));
        Date endReservation=Date.valueOf(LocalDate.of(2022, 10, 20));
        int maxDistance = 100;
        String tags = "CITY";
        Double maxPricePerNight = 25.5 * 2;
        List<Apartment> result = new ArrayList<>();
        Apartment a1 = apartmentBuilder();
        Apartment a2 = apartmentBuilder2();
        result.add(a1);
        result.add(a2);
        when(apartmentRepository.findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(
                startReservation,
                endReservation,
                numGuests,
                maxPricePerNight)).thenReturn(result);
        serviceToTest.multipleResearch(numGuests, location, maxDistance, tags, maxPricePerNight, startReservation, endReservation);
        verify(apartmentRepository, times(1)).findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(any(), any(), eq(numGuests), eq(maxPricePerNight));
    }

    @Test
    void multipleResearchTest5() throws Exception {
        int numGuests = 15;
        String location = "Dalmine";
        Date startReservation=Date.valueOf(LocalDate.of(2022, 10, 10));
        Date endReservation=Date.valueOf(LocalDate.of(2022, 10, 20));
        int maxDistance = 100;
        String tags = "CITY";
        Double maxPricePerNight = 25.5 * 2;
        List<Apartment> result = new ArrayList<>();
        Apartment a1 = apartmentBuilder();
        Apartment a2 = apartmentBuilder2();
        result.add(a1);
        result.add(a2);
        when(apartmentRepository.findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(
                startReservation,
                endReservation,
                numGuests,
                maxPricePerNight)).thenReturn(result);
        serviceToTest.multipleResearch(numGuests, location, maxDistance, tags, maxPricePerNight, startReservation, endReservation);
        verify(apartmentRepository, times(1)).findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(any(), any(), eq(numGuests), eq(maxPricePerNight));
    }

    @Test
    void multipleResearchTest6() throws Exception {
        int numGuests = 20;
        String location = "Dalmine";
        Date startReservation=Date.valueOf(LocalDate.of(2022, 10, 10));
        Date endReservation=Date.valueOf(LocalDate.of(2022, 10, 20));
        int maxDistance = 100;
        String tags = "CITY";
        Double maxPricePerNight = 25.5 * 2;
        List<Apartment> result = new ArrayList<>();
        Apartment a1 = apartmentBuilder();
        Apartment a2 = apartmentBuilder2();
        result.add(a1);
        result.add(a2);
        when(apartmentRepository.findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(
                startReservation,
                endReservation,
                numGuests,
                maxPricePerNight)).thenReturn(result);
        serviceToTest.multipleResearch(numGuests, location, maxDistance, tags, maxPricePerNight, startReservation, endReservation);
        verify(apartmentRepository, times(1)).findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(any(), any(), eq(numGuests), eq(maxPricePerNight));
    }

    private Apartment apartmentBuilder() {
        Apartment apartment = new Apartment(
                "",
                4,
                "Dalmine",
                "CITY",
                25.5
        );
        apartment.setId(UUID.randomUUID());
        apartment.setOwner(String.valueOf(UUID.randomUUID()));
        apartment.setTags("CITY");
        return apartment;
    }

    private Apartment apartmentBuilder2() {
        Apartment apartment = new Apartment(
                "",
                7,
                "Dalmine",
                "CITY",
                25.5
        );
        apartment.setId(UUID.randomUUID());
        apartment.setOwner(String.valueOf(UUID.randomUUID()));
        apartment.setTags("CITY");
        return apartment;
    }
}