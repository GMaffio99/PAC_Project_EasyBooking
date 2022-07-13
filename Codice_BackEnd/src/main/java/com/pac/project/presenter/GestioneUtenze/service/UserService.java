package com.pac.project.presenter.GestioneUtenze.service;

import com.pac.project.model.GestioneUtenze.ConfirmationToken;
import com.pac.project.model.GestioneUtenze.User;
import com.pac.project.model.GestioneUtenze.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service("UserService")
@Transactional
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public UserService(@Qualifier("userRepository") UserDao userDao, ConfirmationTokenService confirmationTokenService) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.confirmationTokenService = confirmationTokenService;
    }

    public List<String> insertUser(User user) {
        if (user != null) { //controllo che la Request non abbia inviato un oggetto vuoto, ma mi aspetto che il controllo venga fatto a FE
            Optional<User> toCheck = userDao.findByEmail(user.getUsername());
            if (!toCheck.isPresent()) {// non esiste già un User con la stessa mail
                if (!userDao.findByCf(user.getCf()).isPresent()) {// non esiste un cliente con lo stesso CF
                    User entity = user;
                    // prima di inserire il user nel DB crypto la sua pwd
                    String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                    user.setPwd(encodedPassword);
                    userDao.save(entity);
                    String token = UUID.randomUUID().toString();
                    ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1440), user);
                    confirmationTokenService.saveConfirmationToken(confirmationToken);
                    return Collections.singletonList(token);
                }
            } else {
                ConfirmationToken existingToken = confirmationTokenService.getTokenByUser(String.valueOf(toCheck.get().getId())).get();
                if (existingToken.getConfirmedAt() != null) {
                    return Collections.singletonList("409");
                } else {
                    existingToken.setCreatedAt(LocalDateTime.now());
                    existingToken.setExpiresAt(LocalDateTime.now().plusMinutes(1440));
                    confirmationTokenService.saveConfirmationToken(existingToken);
                    List<String> result = new ArrayList<>();
                    result.add(existingToken.getToken());
                    result.add("406");
                    return result;
                }
            }
        }
        return null;
    }

    public List<User> returnAllUsers() {
        return userDao.findAll();
    }

    public boolean updateUser(String cf, String newName) {
        int esito = userDao.updateNameByCf(cf, newName);
        return esito != 0;
    }

    public boolean deleteUser(String cf) {
        int esito = userDao.deleteUserByCf(cf);
        return esito != 0;
    }

    public User returnUserById(String idOwner) {
        UUID id = UUID.fromString(idOwner);
        Optional<User> user = userDao.findById(id);
        return user.orElse(null);
    }

    public User returnUserByEmail(String email) {
        Optional<User> user = userDao.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDao.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void upgradeUser(String id) {
        userDao.updateTypeById(UUID.fromString(id));
    }

    public int enableUser(String email) {
        return userDao.enableUser(email);
    }

    public int updatePwd(String email, String pwd) {
        return userDao.updatePwdByEmail(email, pwd);
    }
}
