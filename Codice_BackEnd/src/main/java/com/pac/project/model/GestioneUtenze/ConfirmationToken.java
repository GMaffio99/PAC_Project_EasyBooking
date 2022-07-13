package com.pac.project.model.GestioneUtenze;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_token")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(nullable = true, name = "user_id")
    private User user;

    public ConfirmationToken() {
    }

    public <T> ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, T entity) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        String typeOfEntity = String.valueOf(entity.getClass());
        switch (typeOfEntity) {
            case "class com.pac.project.model.GestioneUtenze.User":
                this.user = (User) entity;
                break;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime time) {
        this.createdAt = time;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime time) {
        this.expiresAt = time;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime time) {
        this.confirmedAt = time;
    }

    public User getUser() {
        return user;
    }

}
