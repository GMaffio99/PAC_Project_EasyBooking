package com.pac.project.presenterTest.commons.email;

import com.pac.project.presenter.commons.email.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import javax.mail.internet.MimeMessage;

import static com.pac.project.presenter.commons.Utils.confirmationMailBuilder;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = EmailService.class)
class EmailServiceTest {

    @Autowired
    EmailService serviceToTest;

    @MockBean
    JavaMailSender mailSender;

    @Test
    void sendTest() {
        /*richiedo che quando venga chiamato il metodo createMimeMessage venga restituito un oggetto
          MimeMessage che mi faccio creare da Mockito (potrei fare un metodo privato in questa classe che lo
          creii, ma è più agile usare Mockito.
          Richiamo dunque il metodo del service che sto testando con dei valori casuali.
          Poi tramite "verify" verifico che il metodo dell'invio della mail sia stato effettivamente invocato.
         */

        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        serviceToTest.send("emailTest", "Completa la registrazione del tuo account",
                confirmationMailBuilder("Utente di test", "token"));
        verify(mailSender).send(Mockito.any(MimeMessage.class));
    }

    @Test
    void sendTestWithMessagingException() {
        /*testo che in caso di lancio di una eccezione in fase di creazione del messaggio questa venga
          correttamente catturata e gestita.
         */
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        doThrow(MailSendException.class).when(mailSender).send(any(MimeMessage.class));
        //richiamo il metodo che non verrà portato a termine a causa dell'eccezione che viene lanciata
        serviceToTest.send("emailTest", "Completa la registrazione del tuo account",
                confirmationMailBuilder("Utente di test", "token"));
        /*questo test funziona in questo modo: ho fatto in modo che venisse lanciata un'eccezione
            però so che questa eccezione viene catturata, quindi mi aspetto che il metodo
            send() del service stiamo testando raggiunga correttamente la fine del suo scope.
         */
        verify(mailSender).send(Mockito.any(MimeMessage.class));
    }
}