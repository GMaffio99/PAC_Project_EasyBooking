package com.pac.project.model.GestioneUtenze;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("adminRepository")
public interface AdminDao extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByEmail(String cf);

    @Modifying
    @Query("update Admin tc set tc.pwd = ?2 where tc.email = ?1")
    int updatePwdByEmail(String email, String pwd);
}
