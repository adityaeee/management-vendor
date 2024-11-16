package com.aditya.management.service;

import com.aditya.management.DTO.response.JwtClaims;
import io.github.bucket4j.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitingService {

    private final JwtServiceImpl jwtService;
    private final Map<String, Bucket> userBuckets;

    @Autowired
    public RateLimitingService(JwtServiceImpl jwtService) {
        this.jwtService = jwtService;
        this.userBuckets = new HashMap<>();
    }

    // Fungsi untuk mendapatkan Bucket untuk pengguna berdasarkan accountId
    private Bucket getBucketForUser(String accountId) {
        if (!userBuckets.containsKey(accountId)) {
            Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1))); // 5 requests per minute
            Bucket bucket = Bucket.builder()
                    .addLimit(limit)
                    .build();
            userBuckets.put(accountId, bucket);
        }
        return userBuckets.get(accountId);
    }

    // Fungsi untuk memeriksa apakah pengguna dapat melakukan request
    public boolean tryConsume(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        // Ambil claims dari JWT token
        JwtClaims jwtClaims = jwtService.getClaimsByToken(bearerToken);

        if (jwtClaims != null) {
            String accountId = jwtClaims.getAccountId();  // Ambil accountId dari JWT

            // Dapatkan bucket untuk pengguna dan coba konsumsi token
            Bucket bucket = getBucketForUser(accountId);
            return bucket.tryConsume(1);  // Mengkonsumsi 1 token untuk request ini
        }
        return false;  // Jika token tidak valid atau tidak ada, mengembalikan false
    }

    // Fungsi untuk mendapatkan jumlah request yang tersisa
    public long getRemainingRequests(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        // Ambil claims dari JWT token
        JwtClaims jwtClaims = jwtService.getClaimsByToken(bearerToken);

        if (jwtClaims != null) {
            String accountId = jwtClaims.getAccountId();  // Ambil accountId dari JWT

            // Dapatkan bucket untuk pengguna
            Bucket bucket = getBucketForUser(accountId);
            return bucket.getAvailableTokens();
        }
        return 0;  // Jika token tidak valid atau tidak ada, mengembalikan 0
    }
}
