package com.aditya.management.service.intrface;

import com.aditya.management.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    Account getByAccountId (String id);
}
