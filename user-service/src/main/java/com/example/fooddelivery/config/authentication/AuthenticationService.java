package com.example.fooddelivery.config.authentication;

import com.example.fooddelivery.common.TokenType;
import com.example.fooddelivery.entity.PasswordCredential;
import com.example.fooddelivery.entity.Token;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.entity.UserProfile;
import com.example.fooddelivery.repository.TokenRepository;
import com.example.fooddelivery.repository.UserRepository;
import com.example.fooddelivery.service.CustomUserDetailsService;
import com.example.fooddelivery.repository.PasswordCredentialRepository;
import com.example.fooddelivery.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordCredentialRepository passwordCredentialRepository;
  private final UserProfileRepository userProfileRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService userDetailsService;

  public AuthenticationResponse register(RegisterRequest request) {
    PasswordCredential passwordCredential = new PasswordCredential();
    passwordCredential.setPassword(passwordEncoder.encode(request.getPassword()));
    passwordCredential.setAccountNonExpired(true);
    passwordCredential.setAccountNonLocked(true);
    passwordCredential.setCredentialsNonExpired(true);
    passwordCredential.setEnabled(true); // Assume enabled by default or based on business logic

    UserProfile userProfile = new UserProfile();
    userProfile.setFirstname(request.getFirstname());
    userProfile.setLastname(request.getLastname());

    User user = User.builder()
            .email(request.getEmail())
            .role(request.getRole())
            .build();

    // Link entities
    passwordCredential.setUser(user);
    userProfile.setUser(user);
    
    // Save entities
    User savedUser = repository.save(user); // Adjust UserRepository to cascade or manually save related entities
    userProfileRepository.save(userProfile);
    passwordCredentialRepository.save(passwordCredential);

    // Generate tokens
    UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
    String jwtToken = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    // Link JWT token to user, not covered here due to complexity and varies by implementation

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}