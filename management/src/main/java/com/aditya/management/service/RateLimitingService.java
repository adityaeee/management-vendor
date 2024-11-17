package com.aditya.management.service;

import com.aditya.management.DTO.response.JwtClaims;
import com.aditya.management.service.intrface.JwtService;
import io.github.bucket4j.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

    private final Map<String, Bucket> userBuckets;
    final String AUTO_HEADER = "Authorization";
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;


    private Bucket getBucketForUser(String accountId) {
        if (!userBuckets.containsKey(accountId)) {
            Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofSeconds(60)));
            Bucket bucket = Bucket.builder()
                    .addLimit(limit)
                    .build();
            userBuckets.put(accountId, bucket);
        }
        return userBuckets.get(accountId);
    }


    public boolean tryConsume() {
        String bearerToken = httpServletRequest.getHeader(AUTO_HEADER);
        JwtClaims jwtClaims = jwtService.getClaimsByToken(bearerToken);

        if (jwtClaims != null) {
            String accountId = jwtClaims.getAccountId();

            Bucket bucket = getBucketForUser(accountId);
            return bucket.tryConsume(1);
        }
        return false;
    }

    public long getRemainingRequests() {
        String bearerToken = httpServletRequest.getHeader(AUTO_HEADER);
        JwtClaims jwtClaims = jwtService.getClaimsByToken(bearerToken);

        if (jwtClaims != null) {
            String accountId = jwtClaims.getAccountId();

            Bucket bucket = getBucketForUser(accountId);
            return bucket.getAvailableTokens();
        }
        return 0;
    }
}
