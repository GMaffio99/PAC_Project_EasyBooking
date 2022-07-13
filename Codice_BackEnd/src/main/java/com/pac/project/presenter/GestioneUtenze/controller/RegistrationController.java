package com.pac.project.presenter.GestioneUtenze.controller;

import com.pac.project.presenter.GestioneUtenze.RequestRegistration;
import com.pac.project.presenter.GestioneUtenze.exceptions.AlreadyUsedTokenException;
import com.pac.project.presenter.GestioneUtenze.exceptions.ExpiredTokenException;
import com.pac.project.presenter.GestioneUtenze.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/controller")
public class RegistrationController {
    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public void register(@RequestBody RequestRegistration registration, HttpServletResponse response) throws Exception {
        String result = null;
        try {
            registrationService.register(registration);
        } catch (IllegalAccessException ex) {
            response.setStatus(409);
        } catch (ExpiredTokenException ex) {
            response.setStatus(406);
        }
    }

    @GetMapping("/confirm")
    public ModelAndView confirm(@RequestParam("token") String token) {
        ModelAndView model = new ModelAndView("successPage");
        try {
            registrationService.confirmToken(token);
            model.addObject("message", "Account activated!");
            return model;
        } catch (ExpiredTokenException e) {
            model = new ModelAndView("errorPage");
            model.addObject("error", e.getMessage());
            return model;
        } catch (AlreadyUsedTokenException e) {
            model = new ModelAndView("errorPage");
            model.addObject("error", e.getMessage());
            return model;
        }
    }
}