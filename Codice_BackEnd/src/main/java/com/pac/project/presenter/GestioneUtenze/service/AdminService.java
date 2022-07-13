package com.pac.project.presenter.GestioneUtenze.service;

import com.pac.project.model.GestioneUtenze.Admin;
import com.pac.project.model.GestioneUtenze.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("AdminService")
@Transactional
public class AdminService {
    private final AdminDao adminDao;

    @Autowired
    public AdminService(@Qualifier("adminRepository") AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    public boolean insertAdmin(Admin admin) {
        if (admin != null) {
            if (adminDao.findByEmail(admin.getEmail()).isEmpty()) {
                Admin entity = admin;
                adminDao.save(entity);
                return true;
            }
            return false;
        }
        return false;
    }

    public List<Admin> returnAllAdmins() {
        return adminDao.findAll();
    }

    public Admin returnAdminByEmail(String email) {
        Optional<Admin> admin = adminDao.findByEmail(email);
        return admin.orElse(null);
    }

    public int updatePwd(String email, String pwd) {
        return adminDao.updatePwdByEmail(email, pwd);
    }
}
