package com.pac.project.presenter.commons.email;

public interface EmailSender {
    void send(String to, String subject, String email);
}
