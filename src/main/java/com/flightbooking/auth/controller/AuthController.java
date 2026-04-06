package com.flightbooking.auth.controller;

import com.flightbooking.auth.dto.AuthResponse;
import com.flightbooking.auth.dto.LoginRequestDto;
import com.flightbooking.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequestDto loginRequest){
        return authService.login(loginRequest);
    }

}
