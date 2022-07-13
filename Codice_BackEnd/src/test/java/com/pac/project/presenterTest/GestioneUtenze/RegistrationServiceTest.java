package com.pac.project.presenterTest.GestioneUtenze;

import com.pac.project.model.GestioneUtenze.ConfirmationToken;
import com.pac.project.model.GestioneUtenze.User;
import com.pac.project.presenter.GestioneUtenze.RequestRegistration;
import com.pac.project.presenter.GestioneUtenze.exceptions.AlreadyUsedTokenException;
import com.pac.project.presenter.GestioneUtenze.exceptions.ExpiredTokenException;
import com.pac.project.presenter.GestioneUtenze.service.ConfirmationTokenService;
import com.pac.project.presenter.GestioneUtenze.service.RegistrationService;
import com.pac.project.presenter.GestioneUtenze.service.UserService;
import com.pac.project.presenter.commons.email.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.pac.project.presenter.commons.Utils.confirmationMailBuilder;
import static com.pac.project.presenter.commons.Variables.firstPartPath;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = RegistrationService.class)
class RegistrationServiceTest {

    @Autowired
    RegistrationService serviceToTest;

    @MockBean
    UserService userService;

    @MockBean
    ConfirmationTokenService confirmationTokenService;

    @MockBean
    EmailSender emailSender;

    @Test
    void registerAccountTest() throws ExpiredTokenException, IllegalAccessException {
        RequestRegistration input = requestBuilder();
        List<String> token = Collections.singletonList("token");
        when(userService.insertUser(any(User.class))).thenReturn(token);
        serviceToTest.register(requestBuilder());
        //verifico che venga chiamato il metodo per inviare la mail
        String link = firstPartPath + "/controller/confirm?token=" + "token";
        verify(emailSender, times(1)).send(
                requestBuilder().getEmail(),
                "Completa la registrazione del tuo account",
                confirmationMailBuilder("nome", link));
    }

    @Test
    void registerNotConfirmedAccountTest() {
        RequestRegistration input = requestBuilder();
        List<String> token = List.of("token", "406");
        when(userService.insertUser(any(User.class))).thenReturn(token);
        //verifico che venga chiamato il metodo per inviare la mail
        String link = firstPartPath + "/controller/confirm?token=" + "token";
        //controllo che venga lanciata l'eccezione (catturata e gestita dal controller per il FE)
        assertThrows(ExpiredTokenException.class, () -> serviceToTest.register(requestBuilder()));
        //controllo che sia stata inviata la mail
        verify(emailSender, times(1)).send(
                requestBuilder().getEmail(),
                "Completa la registrazione del tuo account",
                confirmationMailBuilder("nome", link));
    }

    @Test
    void registerAlreadyConfirmedAccountTest() {
        List<String> token = Collections.singletonList("409");
        when(userService.insertUser(any(User.class))).thenReturn(token);
        assertThrows(IllegalAccessException.class,
                () -> serviceToTest.register(requestBuilder()));
    }

    private RequestRegistration requestBuilder() {
        RequestRegistration registration = new RequestRegistration(
                "BRBDRS11S41B123V",
                "nome",
                "cognome",
                new Date(1656163289524L),
                "email@test.com",
                "12345678"
        );
        return registration;
    }

    @Test
    void confirmNotExpiredTokenTest() throws AlreadyUsedTokenException, ExpiredTokenException {
        ConfirmationToken token = tokenBuilder();
        when(confirmationTokenService.getToken("token")).thenReturn(java.util.Optional.of(token));
        serviceToTest.confirmToken("token");
        //verifico che venga fatta la chiamata del metodo che deve impostare la data di conferma
        verify(confirmationTokenService, times(1)).setConfirmedAt("token");
    }

    @Test
    void confirmAlreadyUsedTokenTest() {
        ConfirmationToken token = tokenBuilder();
        token.setConfirmedAt(LocalDateTime.MIN);
        when(confirmationTokenService.getToken("token")).thenReturn(java.util.Optional.of(token));
        //il token che viene recuperato dal DB è già stato usato, verifico che venga lanciata la relativa eccezione
        assertThrows(AlreadyUsedTokenException.class, () -> serviceToTest.confirmToken("token"));
    }

    @Test
    void confirmAlreadyExpiredTokenTest() {
        ConfirmationToken token = tokenBuilder();
        token.setExpiresAt(LocalDateTime.MIN);
        when(confirmationTokenService.getToken("token")).thenReturn(java.util.Optional.of(token));
        //il token che viene recuperato dal DB è già scaduto, verifico che venga lanciata la relativa eccezione
        assertThrows(ExpiredTokenException.class, () -> serviceToTest.confirmToken("token"));
    }

    private ConfirmationToken tokenBuilder() {
        ConfirmationToken token = new ConfirmationToken(
                "token",
                LocalDateTime.now(),
                LocalDateTime.MAX,
                new User()
        );
        return token;
    }
}