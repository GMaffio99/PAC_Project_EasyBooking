package com.pac.project.model.GestioneUtenze;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("userRepository")
public interface UserDao extends JpaRepository<User, UUID> {
    Optional<User> findByCf(String cf);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User tc set tc.name = ?2 where tc.cf = ?1")
    int updateNameByCf(String cf, String name);

    @Modifying
    @Query("delete from User tc where tc.cf = ?1")
    int deleteUserByCf(String cf);

    @Modifying
    @Query("update User a SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    @Modifying
    @Query("update User tc set tc.pwd = ?2 where tc.email = ?1")
    int updatePwdByEmail(String email, String pwd);

    @Modifying
    @Query("update User tc set tc.isOwner = TRUE where tc.id = ?1")
    int updateTypeById(UUID id);
}