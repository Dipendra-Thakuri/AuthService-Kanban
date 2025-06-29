package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.UserAlreadyExistsException;
import com.example.jwtdemo.exception.UserNotFoundException;
import com.example.jwtdemo.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User addUser(User user) throws UserAlreadyExistsException;
    Map<String, Object> login(String username, String password) throws UserNotFoundException;
    void deleteUser(Long userId) throws UserNotFoundException;
    List<User> getAllUsers();

    // New methods for user profile management
    User getUserProfile(String username) throws UserNotFoundException;
}