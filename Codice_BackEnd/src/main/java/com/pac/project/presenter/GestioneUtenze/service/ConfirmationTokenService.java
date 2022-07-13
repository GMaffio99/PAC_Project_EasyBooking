package com.pac.project.presenter.GestioneUtenze.service;

import com.pac.project.model.GestioneUtenze.ConfirmationToken;
import com.pac.project.model.GestioneUtenze.ConfirmationTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ConfirmationTokenService {

    private final ConfirmationTokenDao confirmationTokenDao;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenDao confirmationTokenDao) {
        this.confirmationTokenDao = confirmationTokenDao;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenDao.saveAndFlush(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenDao.findByToken(token);
    }

    public Optional<ConfirmationToken> getTokenByUser(String id) {
        return confirmationTokenDao.findByUserId(UUID.fromString(id));
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenDao.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
