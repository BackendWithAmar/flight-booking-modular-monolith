package com.flightbooking.user.service.impl;

import com.flightbooking.user.dto.RegisterRequest;
import com.flightbooking.user.dto.UserResponse;
import com.flightbooking.user.entity.User;
import com.flightbooking.user.repository.UserRepository;
import com.flightbooking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registeUser(RegisterRequest registerRequest){
        User user = User.builder()
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
