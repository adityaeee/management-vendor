package com.aditya.management.service.intrface;

import com.aditya.management.DTO.request.AuthRequest;
import com.aditya.management.DTO.response.LoginResponse;
import com.aditya.management.DTO.response.RegisterResponse;
import com.aditya.management.entity.Account;

public interface AuthService {
    RegisterResponse register (AuthRequest request);
    LoginResponse login (AuthRequest request);
}
