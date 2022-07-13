package com.pac.project.modelTest;

import com.pac.project.model.GestioneUtenze.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Constructor;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {
    String cf = "BNCHXRL97T14524L";
    String name = "Carlo";
    String surname = "Bianchi";
    Date dob = Date.valueOf(LocalDate.of(1997, 2, 11));
    String email = "c.bianchi@gmail.com";
    String pwd = "abc123";
    User user = new User(cf, name, surname, dob, email, pwd, true);
    UUID id = UUID.fromString("9491a920-f7f2-44b2-84b4-0dcc8ef0953f");
    User user1 = new User();

    @Test
    public void testSetId() {
        user.setId(id);
        assertEquals(id, user.getId());
    }


    @Test
    public void testSetName() {
        user1.setName("Pippo");
        assertEquals("Pippo", user1.getName());
    }

    @Test
    public void testSetDob() {
        Date prova = Date.valueOf(LocalDate.of(2022, 2, 11));
        user.setDob(prova);
        assertEquals(prova, user.getDob());
    }

    @Test
    public void testSetCf() {
        user.setCF("PPPRSS96T256352R");
        assertEquals("PPPRSS96T256352R", user.getCf());
    }

    @Test
    public void testSetSurname() {
        user.setSurname("Rossi");
        assertEquals("Rossi", user.getSurname());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("p.rossi@gmail.com");
        assertEquals("p.rossi@gmail.com", user.getEmail());
    }

    @Test
    public void testSetPwd() {
        user.setPwd("abcd1234");
        assertEquals("abcd1234", user.getPassword());
    }

    @Test
    public void testSetIsOwner() {
        user.setIsOwner(false);
        assertFalse(user.getIsOwner());
    }


    @Test
    public void testGetUsername() {
        assertEquals(email, user.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    public void testGetAuthorities() {
        Collection<SimpleGrantedAuthority> res = (Collection<SimpleGrantedAuthority>) user.getAuthorities();
        assertEquals(res.size(), 1);
        assertEquals(res.iterator().next().toString(), "User");
    }

    @Test
    void protectedConstructorTest() throws Exception {
        Constructor<User> c = User.class.getDeclaredConstructor();
        c.setAccessible(true);
        User constructed = c.newInstance();
        assertNotNull(constructed);
    }


}
