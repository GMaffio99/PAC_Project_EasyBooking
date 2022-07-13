package com.pac.project.presenter.GestioneUtenze.service;

import com.pac.project.model.GestioneUtenze.ConfirmationToken;
import com.pac.project.model.GestioneUtenze.User;
import com.pac.project.presenter.GestioneUtenze.RequestRegistration;
import com.pac.project.presenter.GestioneUtenze.exceptions.AlreadyUsedTokenException;
import com.pac.project.presenter.GestioneUtenze.exceptions.ExpiredTokenException;
import com.pac.project.presenter.commons.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.pac.project.presenter.commons.Utils.confirmationMailBuilder;
import static com.pac.project.presenter.commons.Variables.firstPartPath;

@Service
@Transactional
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    @Autowired
    public RegistrationService(UserService userService, ConfirmationTokenService confirmationTokenService, EmailSender emailSender) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
    }

    public void register(RequestRegistration request) throws IllegalAccessException, ExpiredTokenException {
        List<String> tokenAndError;

        tokenAndError = userService.insertUser(
                new User(
                        request.getCf(),
                        request.getName(),
                        request.getSurname(),
                        request.getDob(),
                        request.getEmail(),
                        request.getPwd(),
                        request.getAccountType()));

        if (tokenAndError.size() == 1) {
            if (tokenAndError.get(0).equals("409")) {
                throw new IllegalAccessException();
            } else {
                String link = firstPartPath + "/controller/confirm?token=" + tokenAndError.get(0);
                emailSender.send(request.getEmail(),
                        "Completa la registrazione del tuo account",
                        confirmationMailBuilder(request.getName(), link));
            }
        } else {
            String link = firstPartPath + "/controller/confirm?token=" + tokenAndError.get(0);
            emailSender.send(request.getEmail(),
                    "Completa la registrazione del tuo account",
                    confirmationMailBuilder(request.getName(), link));
            throw new ExpiredTokenException();
        }
    }

    public void confirmToken(String token) throws ExpiredTokenException, AlreadyUsedTokenException {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElse(null);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new AlreadyUsedTokenException();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ExpiredTokenException();
        }

        confirmationTokenService.setConfirmedAt(token);

        if (confirmationToken.getUser() != null) {
            userService.enableUser(
                    confirmationToken.getUser().getUsername());
        }
    }
}