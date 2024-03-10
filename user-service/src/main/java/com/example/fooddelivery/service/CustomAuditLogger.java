package com.example.fooddelivery.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuditLogger {

    private static final Logger auditLogger = LoggerFactory.getLogger("auditLogger");

    public void logSecurityEvent(String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "ANONYMOUS";
        auditLogger.info("User: {}, Action: {}", username, message);
    }
}
