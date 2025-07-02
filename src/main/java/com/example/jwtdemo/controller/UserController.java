package com.example.jwtdemo.controller;

import com.example.jwtdemo.exception.UserAlreadyExistsException;
import com.example.jwtdemo.exception.UserNotFoundException;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000") // Allow frontend dev server
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;

    // Constructor Injection
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Check if user exists by email (for frontend validation)
    @GetMapping("/register")
    public ResponseEntity<?> checkIfUserExists(@RequestParam String email) {
        List<User> users = userService.getAllUsers(); // Or use a proper findByEmail()
        boolean exists = users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
        return ResponseEntity.ok(exists ? List.of("exists") : List.of());
    }

    // ✅ Register New User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.addUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getUserId());
            response.put("username", savedUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // ✅ Login User
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");
            Map<String, Object> response = userService.login(username, password);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // ✅ Admin: Get All Users
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ✅ Admin: Delete a User
    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    // ✅ Get User Profile
//    @GetMapping("/user/profile")
//    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
//        try {
//            String username = (String) request.getAttribute("username");
//            User user = userService.getUserProfile(username);
//
//            Map<String, Object> profileResponse = new HashMap<>();
//            profileResponse.put("userId", user.getUserId());
//            profileResponse.put("username", user.getUsername());
//            profileResponse.put("email", user.getEmail());
//            profileResponse.put("role", user.getRole());
//
//            return ResponseEntity.ok(profileResponse);
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
}
