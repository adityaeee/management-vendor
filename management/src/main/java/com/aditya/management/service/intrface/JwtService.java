package com.aditya.management.service.intrface;

import com.aditya.management.DTO.response.JwtClaims;
import com.aditya.management.entity.Account;

public interface JwtService {
    String generateToken(Account account);
    boolean verifyJwtToken (String bearerToken);
    JwtClaims getClaimsByToken (String bearerToken);
}
