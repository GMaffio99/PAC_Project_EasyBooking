package com.pac.project.presenterTest.GestioneUtenze;

import com.pac.project.model.GestioneUtenze.Admin;
import com.pac.project.model.GestioneUtenze.AdminDao;
import com.pac.project.presenter.GestioneUtenze.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = AdminService.class)
class AdminServiceTest {

    @Autowired
    AdminService serviceToTest;

    @MockBean
    @Qualifier("adminRepository")
    AdminDao adminRepository;

    @Test
    void insertAdminWithInputNullTest() {
        boolean output = serviceToTest.insertAdmin(null);
        assertFalse(output);
    }

    @Test
    void insertAlreadyExistingAdminTest() {
        //simulo il caso in cui il profilo sia già inserito nel DB
        when(adminRepository.findByEmail(adminBuilder().getEmail()))
                .thenReturn(Optional.of(adminBuilder()));
        boolean output = serviceToTest.insertAdmin(adminBuilder());
        //mi aspetto che in questo caso l'inserimento non vada a buon fine
        assertFalse(output);
    }

    @Test
    void insertAdminTest() {
        //simulo il caso in cui il profilo non sia presente nel DB
        when(adminRepository.findByEmail(adminBuilder().getEmail()))
                .thenReturn(Optional.empty());
        boolean output = serviceToTest.insertAdmin(adminBuilder());
        //mi aspetto che in questo caso l'inserimento vada a buon fine
        //verifico che sia chiamata la INSERT a db
        verify(adminRepository, times(1))
                .save(any(Admin.class));
        assertTrue(output);
    }

    @Test
    void returnAdminByEmailNotFound() {
        when(adminRepository.findByEmail(adminBuilder().getEmail()))
                .thenReturn(Optional.empty());
        Admin output = serviceToTest.returnAdminByEmail("");
        assertNull(output);
    }

    @Test
    void returnAdminByEmailFound() {
        when(adminRepository.findByEmail(adminBuilder().getEmail()))
                .thenReturn(Optional.of(adminBuilder()));
        Admin output = serviceToTest.returnAdminByEmail(adminBuilder().getEmail());
        assertNotNull(output);
    }

    private Admin adminBuilder() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setName("nome");
        admin.setSurname("cognome");
        admin.setEmail("email@test.com");
        admin.setDob(new Date(new java.util.Date().getTime()));
        admin.setLoggedIn(false);
        return admin;
    }
}