package com.pac.project.presenter.GestioneAlloggi;

import com.pac.project.model.GestioneAlloggi.Apartment;
import com.pac.project.presenter.GestioneUtenze.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/controller")
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final UserService userService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService, UserService userService) {
        this.apartmentService = apartmentService;
        this.userService = userService;
    }

    @PostMapping("/apartment/{idOwner}")
    public void insertApartment(@RequestBody Apartment apartment, @PathVariable("idOwner") String idOwner) {
        if (!apartmentService.insertApartment(apartment, idOwner))
            return;
        if (!userService.returnUserById(idOwner).getIsOwner()) {
            userService.upgradeUser(idOwner);
        }
    }

    @GetMapping("/apartment/{idApartment}")
    public Apartment returnApartmentById(@PathVariable("idApartment") String idApartment) {
        return apartmentService.returnApartmentById(UUID.fromString(idApartment)).orElse(null);
    }

    @GetMapping("/apartment/owner/{idOwner}")
    public List<Apartment> returnApartmentsByOwner(@PathVariable String idOwner) {
        return apartmentService.returnApartmentsByOwner(idOwner);
    }

    @PutMapping("/apartment/{idApartment}")
    public boolean updateApartment(@PathVariable("idApartment") String idApartment, @RequestBody RequestUpdateApartment requestUpdate) {
        return apartmentService.updateApartment(UUID.fromString(idApartment), requestUpdate.getNumGuests(), requestUpdate.getTags(), requestUpdate.getPricePerNight());
    }

    @DeleteMapping("/apartment/{idApartment}")
    public boolean deleteApartment(@PathVariable("idApartment") String id) {
        return apartmentService.deleteApartment(UUID.fromString(id));
    }

    @PostMapping("/research")
    public List<?> filteredResearch(@RequestBody RequestResearch requestResearch) throws Exception {
        if (requestResearch.multipleApartments == 1)
            return apartmentService.multipleResearch(requestResearch.getNumGuests(), requestResearch.getLocation(), requestResearch.getMaxDistance(), requestResearch.getTags(), requestResearch.getMaxPricePerNight(), requestResearch.getPrenotationStart(), requestResearch.getPrenotationEnd());
        else
            return apartmentService.singleResearch(requestResearch.getNumGuests(), requestResearch.getLocation(), requestResearch.getMaxDistance(), requestResearch.getTags(), requestResearch.getMaxPricePerNight(), requestResearch.getPrenotationStart(), requestResearch.getPrenotationEnd());
    }
}
