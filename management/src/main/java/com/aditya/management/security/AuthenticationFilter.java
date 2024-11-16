package com.aditya.management.security;

import com.aditya.management.DTO.response.JwtClaims;
import com.aditya.management.entity.Account;
import com.aditya.management.service.intrface.AccountService;
import com.aditya.management.service.intrface.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    final String AUTO_HEADER = "Authorization";
    private final JwtService jwtService;
    private final AccountService accountService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(AUTO_HEADER);

            if(bearerToken != null && jwtService.verifyJwtToken(bearerToken)) {
                JwtClaims decodeJwt = jwtService.getClaimsByToken(bearerToken);
                Account accountBySub = accountService.getByAccountId(decodeJwt.getAccountId());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        accountBySub.getUsername(),
                        null,
                        accountBySub.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
