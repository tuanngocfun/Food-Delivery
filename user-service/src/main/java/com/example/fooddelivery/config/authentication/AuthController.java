package com.example.fooddelivery.config.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddelivery.config.security.RequestLimitService;
import com.example.fooddelivery.util.XSSUtil;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;

    // Inject RequestLimitService
    private final RequestLimitService requestLimitService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request
    ) {
        // Sanitize input fields
        request.setFirstname(XSSUtil.sanitize(request.getFirstname()));
        request.setLastname(XSSUtil.sanitize(request.getLastname()));
        request.setEmail(XSSUtil.sanitize(request.getEmail()));
        // Do not sanitize passwords; they are not output to HTML and will be hashed

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request,
        HttpServletRequest httpRequest // Inject HttpServletRequest to get client IP
    ) {
        String clientIP = httpRequest.getRemoteAddr();
        if (requestLimitService.isBlocked(clientIP)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthenticationResponse("You are temporarily blocked due to too many failed attempts. Please try again later.", null));
        }

        try {
            AuthenticationResponse response = service.authenticate(request);
            requestLimitService.loginSucceeded(clientIP);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            requestLimitService.loginFailed(clientIP);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("Authentication failed. Please check your credentials.", null));
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException, java.io.IOException {
        service.refreshToken(request, response);
    }
}
