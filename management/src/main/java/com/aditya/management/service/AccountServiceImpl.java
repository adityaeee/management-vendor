package com.aditya.management.service;

import com.aditya.management.entity.Account;
import com.aditya.management.repository.AccountRepository;
import com.aditya.management.service.intrface.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    public final AccountRepository accountRepository;

    @Override
    public Account getByAccountId(String id) {
        return accountRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }
}
