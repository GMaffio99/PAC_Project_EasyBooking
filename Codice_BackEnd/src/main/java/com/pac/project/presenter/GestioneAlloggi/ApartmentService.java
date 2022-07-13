package com.pac.project.presenter.GestioneAlloggi;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.pac.project.model.GestioneAlloggi.Apartment;
import com.pac.project.model.GestioneAlloggi.ApartmentDao;
import com.pac.project.model.GestioneAlloggi.WrappedApartment;
import com.pac.project.presenter.commons.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;


@Service("ApartmentService")
@Transactional
public class ApartmentService {

    private final ApartmentDao apartmentDao;

    @Autowired
    public ApartmentService(@Qualifier("apartmentRepository") ApartmentDao apartmentDao) {
        this.apartmentDao = apartmentDao;
    }

    public boolean insertApartment(Apartment apartment, String ownerId) {
        if (apartment != null) {
            Apartment entity = apartment;
            entity.setOwner(ownerId);
            apartmentDao.save(entity);
            return true;
        }
        return false;
    }

    public Optional<Apartment> returnApartmentById(UUID id) {
        return apartmentDao.findById(id);
    }

    public List<Apartment> returnApartmentsByOwner(String cfOwner) {
        return apartmentDao.findByOwnerId(UUID.fromString(cfOwner));
    }

    public List<Apartment> returnApartmentsByLocation(String location) {
        List<Apartment> apartments = apartmentDao.findByLocation(location);
        return (apartments.isEmpty() ? apartments : null);
    }

    public List<Apartment> returnApartmentsByNumGuests(int numGuests) {
        return apartmentDao.findByNumGuests(numGuests);
    }

    public List<Apartment> returnApartmentsByTags(String tags) {
        return apartmentDao.findByTags(tags);
    }


    public boolean updateApartment(UUID id, int numGuests, String tags, double pricePerNight) {
        int esito = apartmentDao.updateApartment(id, numGuests, tags, pricePerNight);
        return esito != 0;
    }

    public boolean deleteApartment(UUID id) {
        int esito = apartmentDao.deleteApartment(id);
        return esito != 0;
    }

    public List<Apartment> singleResearch(int numGuests, String location, int maxDistance, String tags, double maxPricePerNight, Date startReservation, Date endReservation) throws Exception {

        List<Apartment> apartmentList;

        //recupero dal db gli appartamenti con i vincoli di date, numGuests e maxPricePerNight
        if (maxPricePerNight > 0)
            apartmentList = apartmentDao.findAvailableApartmentsPerNumGuestsAndMaxPricePerNight(startReservation, endReservation, numGuests, maxPricePerNight);
        else
            apartmentList = apartmentDao.findAvailableApartmentsPerNumGuests(startReservation, endReservation, numGuests);

        //filtro in base ai tags (tutti i tags specificati devono essere presenti nei tags dell'appartamento, che può anche averne degli altri)
        //se nella ricerca non è stato specificato alcun tag (stringa vuota), o se la lista è vuota, non eseguo il controllo
        if (!tags.equals("") && !apartmentList.isEmpty()) {
            List<String> tagList = Arrays.stream(tags.split("-")).toList();
            apartmentList.removeIf(a -> !(new HashSet<>(Arrays.stream(a.getTags().split("-")).toList()).containsAll(tagList)));
        }

        // filtro in base alla distanza massima, tramite Google Maps API
        // se nella ricerca non è stata specificata una distanza massima (valore nullo), la imposto al valore di default (
        // se la lista è vuota non eseguo il controllo
        if (!apartmentList.isEmpty()) {
            if (maxDistance == 0)
                maxDistance = 50000;
            apartmentList = checkDistance(apartmentList, location, maxDistance);
        }

        return apartmentList;

    }

    public List<WrappedApartment> multipleResearch(int numGuests, String location, int maxDistance, String tags, double maxPricePerNight, Date startReservation, Date endReservation) throws Exception {

        List<Apartment> apartmentList;

        if (maxPricePerNight > 0)
            apartmentList = apartmentDao.findAvailableApartmentsPerMaxNumGuestsAndMaxPricePerNight(startReservation, endReservation, numGuests, maxPricePerNight);
        else
            apartmentList = apartmentDao.findAvailableApartmentsPerMaxNumGuests(startReservation, endReservation, numGuests);


        if (!tags.equals("") && !apartmentList.isEmpty()) {
            List<String> tagList = Arrays.stream(tags.split("-")).toList();
            apartmentList.removeIf(a -> !(new HashSet<>(Arrays.stream(a.getTags().split("-")).toList()).containsAll(tagList)));
        }

        // filtro in base alla distanza massima, tramite Google Maps API
        // se nella ricerca non è stata specificata una distanza massima (valore nullo), la imposto al valore di default (
        // se la lista è vuota non eseguo il controllo
        if (!apartmentList.isEmpty()) {
            if (maxDistance == 0)
                maxDistance = 50000;
            apartmentList = checkDistance(apartmentList, location, maxDistance);
        }


        // aggiungo alla lista dei risultati gli appartamenti singoli che rispettano i vincoli

        List<WrappedApartment> resultList = new ArrayList<>();

        for (Apartment a : apartmentList) {
            if (a.numGuests == numGuests)
                resultList.add(new WrappedApartment(a, 1, 1));
        }


        // imposto il massimo numero di appartamenti in un gruppo
        // e il minimo numero di ospiti per appartamento di un gruppo
        // in base al numero totale di ospiti

        int maxNumApartments;

        if (numGuests < 3) return resultList;
        else if (numGuests < 6) maxNumApartments = 2;
        else if (numGuests < 12) maxNumApartments = 3;
        else if (numGuests < 15) maxNumApartments = 4;
        else maxNumApartments = 5;

        int minNumGuests = Math.floorDiv(numGuests, maxNumApartments);


        // rimuovo gli appartamenti singoli e ordino il resto della lista per numero di ospiti

        apartmentList.removeIf(a -> a.numGuests == numGuests);

        apartmentList.sort((a1, a2) -> {
            return Integer.compare(a2.numGuests, a1.numGuests); // ordine decrescente
        });


        // calcolo le possibili combinazioni e restituisco quelle che soddisfano i criteri

        Apartment[] data = new Apartment[maxNumApartments];
        apartmentCombination(apartmentList, data, 0, apartmentList.size() - 1, 0, maxNumApartments, minNumGuests, numGuests, maxPricePerNight, resultList);

        return resultList;

    }

    private void apartmentCombination(List<Apartment> apartmentList, Apartment[] data, int start, int end, int index, int maxNumAp, int minNumGuests, int numGuests, double maxPricePerNight, List<WrappedApartment> resultList) {

        // caso base
        if (index == maxNumAp) return;

        int totGuests = 0;
        double totPrice = 0.0;

        for (int i = 0; i < index; i++) {
            totGuests += data[i].numGuests;
            totPrice += data[i].pricePerNight;
        }

        // iterazione
        for (int i = start; i <= end; i++) {

            Apartment a = apartmentList.get(i);
            if (a.numGuests < minNumGuests)
                continue; // se l'appartamento non ha il numero minimo di ospiti, salto l'iterazione
            if (totGuests + a.numGuests > numGuests)
                continue; // se aggiungendo l'appartamento al gruppo supero il numero di ospiti fissato, salto l'iterazione
            if (maxPricePerNight > 0 && (totPrice + a.pricePerNight) > maxPricePerNight)
                continue; // se aggiungendo l'appartamento supero il massimo prezzo (se presente), salto l'iterazione

            // se ho raggiunto il numero di ospiti, aggiungo il gruppo alla lista dei risultati
            // poi termino, perché andando avanti supererei il numero di ospiti
            if (totGuests + a.numGuests == numGuests) {
                for (int j = 0; j < index; j++)
                    resultList.add(new WrappedApartment(data[j], j + 1, index + 1));
                resultList.add(new WrappedApartment(a, index + 1, index + 1));
                continue;
            }

            data[index] = a;
            apartmentCombination(apartmentList, data, i + 1, end, index + 1, maxNumAp, minNumGuests, numGuests, maxPricePerNight, resultList);

        }

    }


    private List<Apartment> checkDistance(List<Apartment> apartmentList, String location, int maxDistance) throws Exception {

        List<Apartment> result = new ArrayList<>();

        // creo un array di destinazioni
        String[] destinations = new String[apartmentList.size()];
        int i = 0;
        for (Apartment a : apartmentList) {
            destinations[i++] = a.getLocation();
        }

        // creo la connessione ed eseguo una richiesta per l'API DistanceMatrixApi
        GeoApiContext context = new GeoApiContext.Builder().apiKey(Variables.GEO_API_KEY).build();

        DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                .origins(location)
                .destinations(destinations)
                .mode(TravelMode.WALKING)
                .language("en")
                .units(Unit.METRIC)
                .await();

        // aggiungo alla lista dei risultati gli appartamenti che rispettano il vincolo
        i = 0;
        for (DistanceMatrixElement e : distanceMatrix.rows[0].elements) {
            if (e.distance != null) {
                if (e.distance.inMeters <= maxDistance)
                    result.add(apartmentList.get(i));
            }
            i++;
        }

        context.shutdown();
        return result;

    }

}
