package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {
    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void verifyCreateUser() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Patrizia");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Patrizia", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void verifyFindById() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest ur = new CreateUserRequest();
        ur.setUsername("Patrizia");
        ur.setPassword("testPassword");
        ur.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(ur);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Patrizia", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        when(userRepository.findById(u.getId())).thenReturn(Optional.of(u));
        final ResponseEntity<User> response2 = userController.findById(u.getId());
        User userFound = response2.getBody();
        assertEquals(200, response2.getStatusCodeValue());
        assertEquals(0, userFound.getId());
        assertEquals("Patrizia", userFound.getUsername());
        assertEquals("thisIsHashed", userFound.getPassword());
    }

    @Test
    public void verifyFindByUsername() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Patrizia");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Patrizia", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
        final ResponseEntity<User> response2 = userController.findByUserName(u.getUsername());
        User userFound = response2.getBody();
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(0, userFound.getId());
        assertEquals("Patrizia", userFound.getUsername());
        assertEquals("thisIsHashed", userFound.getPassword());


    }
}
