package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.UserAlreadyExistsException;
import com.example.jwtdemo.exception.UserNotFoundException;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testLogin_UserNotFound() {
        when(userRepository.findByUsernameAndPassword("testuser", "password")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.login("testuser", "password");
        });
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User("user1", "pass1", "user1@test.com", "USER"),
                new User("user2", "pass2", "user2@test.com", "USER")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteUser_Success() throws UserNotFoundException {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });
    }
}