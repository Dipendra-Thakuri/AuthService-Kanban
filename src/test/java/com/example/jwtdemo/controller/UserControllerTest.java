package com.example.jwtdemo.controller;

import com.example.jwtdemo.exception.UserAlreadyExistsException;
import com.example.jwtdemo.exception.UserNotFoundException;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

public class UserControllerTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    public void testRegisterUser_Success() throws UserAlreadyExistsException {
        User user = new User("testuser", "password", "test@test.com", "USER");
        User savedUser = new User("testuser", "password", "test@test.com", "USER");
        savedUser.setUserId(1L);

        Mockito.when(userService.addUser(any(User.class))).thenReturn(savedUser);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("User registered successfully", responseBody.get("message"));
        assertEquals(1L, responseBody.get("userId"));
        assertEquals("testuser", responseBody.get("username"));
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() throws UserAlreadyExistsException {
        User user = new User("testuser", "password", "test@test.com", "USER");

        Mockito.when(userService.addUser(any(User.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    public void testLoginUser_Success() throws UserNotFoundException {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", "testuser");
        loginData.put("password", "password");

        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("token", "token123");
        serviceResponse.put("role", "USER");
        serviceResponse.put("email", "test@test.com");
        serviceResponse.put("username", "testuser");

        Mockito.when(userService.login(anyString(), anyString())).thenReturn(serviceResponse);

        ResponseEntity<?> response = userController.loginUser(loginData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("token123", responseBody.get("token"));
        assertEquals("USER", responseBody.get("role"));
        assertEquals("test@test.com", responseBody.get("email"));
        assertEquals("testuser", responseBody.get("username"));
    }

    @Test
    public void testLoginUser_UserNotFound() throws UserNotFoundException {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", "testuser");
        loginData.put("password", "wrongpassword");

        Mockito.when(userService.login(anyString(), anyString()))
                .thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<?> response = userController.loginUser(loginData);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }






}