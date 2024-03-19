// SecurityConfiguration.java
package com.example.fooddelivery.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.example.fooddelivery.config.authentication.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import static com.example.fooddelivery.config.authorization.Permission.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(req -> req
                .requestMatchers(WHITE_LIST_URL).permitAll()
                // Define permission-based access control for each endpoint or group of endpoints
                .requestMatchers("/api/v1/customer/**").hasAuthority(CUSTOMER_READ_PROFILE.getPermission())
                // For simplicity, using one permission as an example. You should adjust according to your endpoint's specific permission requirements.
                .requestMatchers("/api/v1/delivery/**").hasAuthority(DELIVERY_READ_PROFILE.getPermission())
                .requestMatchers("/api/v1/restaurant/**").hasAuthority(RESTAURANT_MANAGE_PROFILE.getPermission())
                .requestMatchers("/api/v1/admin/**").hasAuthority(ADMIN_MANAGE_USERS.getPermission())
                // Further refine here as per your actual permission requirements
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
            );

        return http.build();
    }
}