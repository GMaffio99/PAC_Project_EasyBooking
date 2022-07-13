package com.pac.project.presenter.GestioneUtenze.controller;

import com.pac.project.model.GestioneUtenze.ConfirmationToken;
import com.pac.project.presenter.GestioneUtenze.exceptions.AlreadyUsedTokenException;
import com.pac.project.presenter.GestioneUtenze.exceptions.ExpiredTokenException;
import com.pac.project.presenter.GestioneUtenze.service.AdminService;
import com.pac.project.presenter.GestioneUtenze.service.ConfirmationTokenService;
import com.pac.project.presenter.GestioneUtenze.service.UserService;
import com.pac.project.presenter.commons.email.EmailSender;
import com.pac.project.presenter.commons.exceptions.KO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.pac.project.presenter.commons.Utils.resetMailBuilder;
import static com.pac.project.presenter.commons.Variables.firstPartPath;

@RestController
@RequestMapping(path = "/resetPwd")
public class ResetPwdController {

    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private AdminService adminService;
    private UserService userService;
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    public ResetPwdController(AdminService adminService, UserService userService,
                              ConfirmationTokenService confirmationTokenService,
                              EmailSender emailSender) {
        this.adminService = adminService;
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
    }

    @GetMapping("/request/{email}")
    public void requestReset(@PathVariable String email, HttpServletResponse response) {
        // Prima di inviare la mail di reset devo controllare che la mail sia registrata nel DB:
        //  1) la mail appartiene al DB (o User o Owner), quello che faccio è inviare la mail con il link per resettare la password
        //  2) la mail non appartiene al DB, viene inviato un HTTP Unauthorized, il F.E. dovrà indicare che la mail non è registrata
        if (userService.returnUserByEmail(email) != null) {
            createAndSendToken(email, "CUSTOMER");
            response.setStatus(HttpStatus.OK.value());
        } else if (adminService.returnAdminByEmail(email) != null) {
            createAndSendToken(email, "ADMIN");
            response.setStatus(HttpStatus.OK.value());
        } else {//l'account per cui resettare la password non è presente, restituisco NOT_FOUND a F.E.
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @GetMapping("/execute")
    public ModelAndView executeReset(@RequestParam("token") String token, HttpSession session) {
        ModelAndView model = new ModelAndView("resetPwdPage");
        try {
            confirmToken(token);
        } catch (ExpiredTokenException e) {
            model = new ModelAndView("errorPage");
            model.addObject("error", e.getMessage());
            return model;
        } catch (KO e) {
            model = new ModelAndView("errorPage");
            model.addObject("error", e.getMessage());
            return model;
        } catch (AlreadyUsedTokenException e) {
            model = new ModelAndView("errorPage");
            model.addObject("error", e.getMessage());
            return model;
        }
        session.setMaxInactiveInterval(900);
        session.setAttribute("token", token);
        return model;
    }

    @PostMapping("/execute/result")
    public ModelAndView update(HttpSession session, HttpServletRequest request) {
        //crypto la nuova pwd
        String newPwd = bCryptPasswordEncoder.encode(request.getParameter("newPwd"));
        String token = (String) session.getAttribute("token");
        ModelAndView operationResult = new ModelAndView("errorPage");
        int res = 0;
        ConfirmationToken tokenEntity = confirmationTokenService.getToken(token).orElse(null);
        if (tokenEntity.getUser() != null)
            res = userService.updatePwd(tokenEntity.getUser().getEmail(), newPwd);

        if (res != 0) {
            operationResult = new ModelAndView("successPage");
            operationResult.addObject("message", "Done! Your password have been changed.");
        }
        return operationResult;
    }

    private void createAndSendToken(String email, String accountType) {
        Object entity = null;
        switch (accountType) {
            case "CUSTOMER":
                entity = userService.returnUserByEmail(email);
                break;
        }
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), entity);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = firstPartPath + "/resetPwd/execute?token=" + token;
        emailSender.send(email, "Reset your password", resetMailBuilder(link));
    }

    private void confirmToken(String token) throws ExpiredTokenException, KO, AlreadyUsedTokenException {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElse(null);
        if (confirmationToken == null) {
            throw new KO("The link is not working, please ask for another mail.");
        }
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
