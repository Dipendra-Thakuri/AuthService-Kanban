package com.example.jwtdemo;

import com.example.jwtdemo.filter.JwtAuthenticationFilter;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JwtdemoApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtAuthenticationFilter jwtFilter; // Autowired JwtFilter

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(JwtdemoApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter); // Use the autowired filter
        registration.addUrlPatterns("/api/v1/admin/*");
        registration.addUrlPatterns("/api/v1/user/*");
        registration.setOrder(1); // Set filter order if needed
        return registration;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            String encodedPassword = passwordEncoder.encode("admin123");
            User admin = new User("admin", encodedPassword, "admin@admin.com", "ADMIN");
            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        }
    }

}