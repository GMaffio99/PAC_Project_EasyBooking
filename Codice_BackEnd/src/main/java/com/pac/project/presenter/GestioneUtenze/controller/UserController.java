package com.pac.project.presenter.GestioneUtenze.controller;

import com.pac.project.model.GestioneUtenze.User;
import com.pac.project.presenter.GestioneUtenze.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/controller")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> returnAllUsers() {
        return userService.returnAllUsers();
    }

    @PutMapping(path = "/user/{cf}")
    public void updateUser(@PathVariable("cf") String cf, @RequestBody String newName) {
        userService.updateUser(cf, newName);
    }

    @DeleteMapping(path = "/user/{cf}")
    public void deleteUser(@PathVariable("cf") String cf) {
        userService.deleteUser(cf);
    }

    @GetMapping("/user/{email}")
    public User returnUserByEmail(@PathVariable("email") String email) {
        return userService.returnUserByEmail(email);
    }
}