package com.pac.project.presenter.GestioneUtenze.controller;

import com.pac.project.model.GestioneUtenze.Admin;
import com.pac.project.presenter.GestioneUtenze.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/controller")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin")
    public void insertAdmin(@RequestBody Admin admin) {
        adminService.insertAdmin(admin);
    }

    @GetMapping("/admin")
    public List<Admin> returnAllAdmins() {
        return adminService.returnAllAdmins();
    }

    @GetMapping("/admin/{email}")
    public Admin returnAdminByEmail(@PathVariable("email") String email) {
        return adminService.returnAdminByEmail(email);
    }
}
