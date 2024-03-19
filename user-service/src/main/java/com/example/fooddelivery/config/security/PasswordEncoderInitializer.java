// package com.example.fooddelivery.config.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Scope;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

// import jakarta.annotation.PostConstruct;



// @Component
// @Scope("singleton")
// public class PasswordEncoderInitializer {
//     private PasswordEncoder passwordEncoder;

//     @Autowired
//     private SecurityConfiguration securityConfiguration;

//     @PostConstruct
//     public void init() {
//         passwordEncoder = new BCryptPasswordEncoder();
//         securityConfiguration.setPasswordEncoder(passwordEncoder);
//     }
// }