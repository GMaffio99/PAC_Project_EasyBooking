package com.pac.project.presenterTest.GestioneUtenze;

import com.pac.project.model.GestioneUtenze.ConfirmationToken;
import com.pac.project.model.GestioneUtenze.User;
import com.pac.project.model.GestioneUtenze.UserDao;
import com.pac.project.presenter.GestioneUtenze.service.ConfirmationTokenService;
import com.pac.project.presenter.GestioneUtenze.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = UserService.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    @Qualifier("userRepository")
    UserDao userRepository;

    @MockBean
    ConfirmationTokenService confirmationTokenService;

    @Test
    void insertUserWithNullInput() {
        List<String> output = userService.insertUser(null);
        assertNull(output);
    }

    @Test
    void insertAlreadyExistingUserWithConfirmedMailTest() {
        User input = userBuilder();
        /*faccio in modo che quando venga interrogata la repository venga
        restituito l'utente che stiamo cercando di inserire, quindi siamo nel
        caso di registrazione di un utente già esistente
         */
        when(userRepository.findByEmail(input.getEmail())).thenReturn(java.util.Optional.of(userBuilder()));
        ConfirmationToken token = new ConfirmationToken();
        token.setConfirmedAt(LocalDateTime.now());
        //un utente che ha già confermato il suo token
        when(confirmationTokenService.getTokenByUser(any()))
                .thenReturn(java.util.Optional.of(token));
        List<String> output = userService.insertUser(userBuilder());
        //in questa casistica mi aspetto che venga restituito l'http 409
        assertEquals("409", output.get(0));
    }

    @Test
    void insertAlreadyExistingUserMailNotConfirmedTest() {
        User input = userBuilder();
        /*faccio in modo che quando venga interrogata la repository venga
        restituito l'utente che stiamo cercando di inserire, quindi siamo nel
        caso di registrazione di un utente già esistente
         */
        when(userRepository.findByEmail(input.getEmail())).thenReturn(java.util.Optional.of(userBuilder()));
        ConfirmationToken token = new ConfirmationToken();
        //non setto la data di conferma, quindi non ha confermato il suo token
        when(confirmationTokenService.getTokenByUser(any()))
                .thenReturn(java.util.Optional.of(token));
        List<String> output = userService.insertUser(userBuilder());
        //in questa casistica mi aspetto che venga restituito l'http 406
        assertEquals(2, output.size());//la dimensione è 2 perché ho sia l'http status che il token
        assertEquals("406", output.get(1));
    }

    @Test
    void insertNewUserTest() {
        User input = userBuilder();
        /*faccio in modo che quando venga interrogata la repository NON venga
        restituito l'utente che stiamo cercando di inserire, quindi siamo nel
        caso di registrazione di un utente nuovo
         */
        when(userRepository.findByEmail(input.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCf(input.getCf())).thenReturn(Optional.empty());
        List<String> output = userService.insertUser(input);
        //verifico che sia stato chiamato il salvataggio a DB
        verify(userRepository, times(1)).save(input);
        //verifico che sia stato chiamato il salvataggio a DB del token (essendo questo un metodo void, la sintatti è un po' diversa)
        verify(confirmationTokenService, times(1)).saveConfirmationToken(Mockito.any(ConfirmationToken.class));
        assertNotNull(output);
        assertNotEquals(input.getPassword(), "12345678"); //non rimane uguale in quanto ormai è stata criptata
        assertEquals(1, output.size()); //size è 1 in quanto l'arraylist contiene solo la stringa del token
    }

    @Test
    void loadUserByUsername() {
        //faccio in modo che il db non restituisca niente cercando la mail
        when(userRepository.findByEmail(userBuilder().getEmail())).thenReturn(Optional.empty());
        //quindi mi aspetto che venga lanciata l'eccezione corrispondente
        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(userBuilder().getEmail()));
    }

    @Test
    void testDeleteUser(){
        when(userRepository.deleteUserByCf(userBuilder().getCf())).thenReturn(1);
        assertTrue(userService.deleteUser(userBuilder().getCf()));
    }

    @Test
    void testUpdateUser(){
        when(userRepository.updateNameByCf(userBuilder().getCf(),userBuilder().getName())).thenReturn(1);
        assertTrue(userService.updateUser(userBuilder().getCf(),userBuilder().getName()));
    }

    @Test
    void testReturnUserByEmail(){
        when(userRepository.findByEmail("email@test.com")).thenReturn(Optional.of(userBuilder()));
        User user=userService.returnUserByEmail("email@test.com");
        assertNotNull(user);
    }
    @Test
    void testReturnUserById(){
        when(userRepository.findById(UUID.fromString("73b820e6-ad16-4dfe-ac9b-04f2999fed77"))).thenReturn(Optional.of(userBuilder()));
        User user=userService.returnUserById("73b820e6-ad16-4dfe-ac9b-04f2999fed77");
        assertNotNull(user);
    }

    private User userBuilder() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("nome");
        user.setDob(new Date(new java.util.Date().getTime()));
        user.setSurname("cognome");
        user.setEmail("email@test.com");
        user.setCF("TNOGSZ64S65B026U");
        user.setPwd("12345678");
        user.setIsOwner(false);
        return user;
    }

}