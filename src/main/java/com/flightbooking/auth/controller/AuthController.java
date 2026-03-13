package com.flightbooking.auth.controller;

import com.flightbooking.auth.dto.AuthResponse;
import com.flightbooking.auth.dto.LoginRequestDto;
import com.flightbooking.common.util.JwtUtil;
import com.flightbooking.user.entity.User;
import com.flightbooking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequestDto requestDto){

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid Credentials!"));

        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPasswordHash())){
            throw new RuntimeException("Invalid Credentials!");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("JWT Bearer")
                .build();
    }

}
