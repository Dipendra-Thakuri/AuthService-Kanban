package com.example.jwtdemo.repository;

import com.example.jwtdemo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use real database
@ActiveProfiles("test") // Optional: use a test profile with test-specific DB config
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsernameAndPassword() {
        User user = new User("testuser", "password", "test@test.com", "USER");
        entityManager.persistAndFlush(user);

        User found = userRepository.findByUsernameAndPassword("testuser", "password");
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    public void testFindByUsername() {
        User user = new User("testuser", "password", "test@test.com", "USER");
        entityManager.persistAndFlush(user);

        User found = userRepository.findByUsername("testuser");
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    public void testFindByUsernameAndPassword_NotFound() {
        User found = userRepository.findByUsernameAndPassword("nonexistent", "wrongpass");
        assertNull(found);
    }

    @Test
    public void testFindByUsername_NotFound() {
        User found = userRepository.findByUsername("nonexistent");
        assertNull(found);
    }
}