package com.flightbooking.auth.service.impl;

import com.flightbooking.auth.dto.AuthResponse;
import com.flightbooking.auth.dto.LoginRequestDto;
import com.flightbooking.auth.service.AuthService;
import com.flightbooking.common.util.JwtUtil;
import com.flightbooking.user.entity.User;
import com.flightbooking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequestDto loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid Credentials!"));

        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPasswordHash())){
            throw new RuntimeException("Invalid Credentials!");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }
}
