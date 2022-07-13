package com.pac.project.presenter.commons.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailSender {

    private static final String NOREPLY_ADDRESS = "completeprocesspac@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void send(String to, String subject, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(NOREPLY_ADDRESS);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(email, true);

            mailSender.send(message);
        } catch (MailException | MessagingException exception) {
            exception.printStackTrace();
        }
    }
}
