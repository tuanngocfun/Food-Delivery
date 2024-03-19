package com.example.fooddelivery.config.security;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class RequestLimitService {

    private final ConcurrentHashMap<String, Integer> loginAttemptsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> blockCache = new ConcurrentHashMap<>();

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_DURATION = 15; // in minutes

    public void loginSucceeded(String key) {
        loginAttemptsCache.remove(key);
        blockCache.remove(key);
    }

    public void loginFailed(String key) {
        loginAttemptsCache.merge(key, 1, Integer::sum);
        if (loginAttemptsCache.get(key) >= MAX_ATTEMPTS) {
            blockCache.put(key, LocalDateTime.now().plusMinutes(BLOCK_DURATION));
            loginAttemptsCache.remove(key);
        }
    }

    public boolean isBlocked(String key) {
        if (blockCache.containsKey(key)) {
            LocalDateTime unblockTime = blockCache.get(key);
            if (unblockTime.isBefore(LocalDateTime.now())) {
                blockCache.remove(key);
                return false;
            }
            return true;
        }
        return false;
    }
}
