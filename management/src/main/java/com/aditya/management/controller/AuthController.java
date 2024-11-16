package com.aditya.management.controller;

import com.aditya.management.DTO.request.AuthRequest;
import com.aditya.management.DTO.response.BaseResponse;
import com.aditya.management.DTO.response.CommonResponse;
import com.aditya.management.DTO.response.LoginResponse;
import com.aditya.management.DTO.response.RegisterResponse;
import com.aditya.management.constant.APiUrl;
import com.aditya.management.entity.Account;
import com.aditya.management.service.intrface.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APiUrl.AUTH)
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<BaseResponse> register (@RequestBody AuthRequest request) {
        RegisterResponse register = authService.register(request);

        CommonResponse<RegisterResponse> response =CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully save data")
                .data(register)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<BaseResponse> login(@RequestBody AuthRequest request) {
        LoginResponse account = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.ACCEPTED.value())
                .message("You have successfully logged in")
                .data(account)
                .build();


        return ResponseEntity.ok(response);
    }
}
