package com.aditya.management.service;

import com.aditya.management.DTO.request.AuthRequest;
import com.aditya.management.DTO.response.LoginResponse;
import com.aditya.management.DTO.response.RegisterResponse;
import com.aditya.management.constant.UserRole;
import com.aditya.management.entity.Account;
import com.aditya.management.entity.Role;
import com.aditya.management.repository.AccountRepository;
import com.aditya.management.service.intrface.AuthService;
import com.aditya.management.service.intrface.JwtService;
import com.aditya.management.service.intrface.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.management.username-admin}")
    private String AdminUsername;

    @Value("${app.management.password-admin}")
    private String AdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initialSuperAdmin() {
        Optional<Account> currentAccount = accountRepository.findByUsername(AdminUsername);
        if(currentAccount.isPresent()) {
            return;
        } else {
            Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
            Role user = roleService.getOrSave(UserRole.ROLE_USER);

            Account account = Account.builder()
                    .username(AdminUsername)
                    .password(passwordEncoder.encode(AdminPassword))
                    .role(List.of(admin, user))
                    .isEnable(true)
                    .build();

            accountRepository.save(account);
        }
    }

    @Override
    public RegisterResponse register(AuthRequest request) {
        Role role = roleService.getOrSave(UserRole.ROLE_USER);

        String hasPassword = passwordEncoder.encode(request.getPassword());

        Account account = Account.builder()
                .username(request.getUsername())
                .password(hasPassword)
                .role(List.of(role))
                .isEnable(true)
                .build();

        accountRepository.saveAndFlush(account);


        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        Account account = (Account) authenticated.getPrincipal();

        String token = jwtService.generateToken(account);

        return LoginResponse.builder()
                .token(token)
                .username(account.getUsername())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}
