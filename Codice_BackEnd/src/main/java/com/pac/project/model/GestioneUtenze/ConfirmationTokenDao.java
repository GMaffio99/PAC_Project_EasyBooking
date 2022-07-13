package com.pac.project.model.GestioneUtenze;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenDao extends JpaRepository<ConfirmationToken, UUID> {

    Optional<ConfirmationToken> findByToken(String token);

    Optional<ConfirmationToken> findByUserId(UUID id);

    @Modifying
    @Query("update ConfirmationToken c set c.confirmedAt = ?2 where c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);

}
