package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;
    @MockBean
    private MailSender mailSender;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Test
    void addUser() {
        User user = new User();
        user.setUsername("mike");
        user.setEmail("mike@mail.com");
        boolean isUserCreated = userService.addUser(user);
        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1)).send(
                ArgumentMatchers.eq(user.getEmail()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }
    @Test
    public void userAddUserFail(){
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@mail.com");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("john");
        boolean isUserCreated = userService.addUser(user);
        assertFalse(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0)).send(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    public void activateUserTest() {
        User user = new User();
        user.setActivationCode("code");
        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");
        boolean isActivated = userService.activateUser("activate");
        Assert.assertTrue(isActivated);
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Assert.assertNull(user.getActivationCode());
    }
    @Test
    public void activateUserFailTest(){
        boolean isActivated = userService.activateUser("activate");
        assertFalse(isActivated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}