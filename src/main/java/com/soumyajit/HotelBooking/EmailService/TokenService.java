package com.soumyajit.HotelBooking.EmailService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    private final Map<String, String> tokenStore = new HashMap<>();
    private final long tokenExpirationTime = 30 * 60 * 1000; // 30 minutes
    private final Map<String, Long> tokenExpirationStore = new HashMap<>();

    public String createToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);
        tokenExpirationStore.put(token, System.currentTimeMillis() + tokenExpirationTime);
        return token;
    }


    public Optional<String> getEmailByToken(String token) {
        Long expirationTime = tokenExpirationStore.get(token);
        if (expirationTime != null && expirationTime > System.currentTimeMillis()) {
            return Optional.ofNullable(tokenStore.get(token));
        } else {
            tokenStore.remove(token);
            tokenExpirationStore.remove(token);
            return Optional.empty();
        }
    }

    public void invalidateToken(String token) {
        tokenStore.remove(token);
        tokenExpirationStore.remove(token);
    }
}

