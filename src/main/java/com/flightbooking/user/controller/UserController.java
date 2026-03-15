package com.flightbooking.user.controller;

import com.flightbooking.user.dto.RegisterRequest;
import com.flightbooking.user.dto.UserResponse;
import com.flightbooking.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Authentication authentication){
        return "Logged in user: " + authentication.getName();
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest registerRequest){
        return userService.registeUser(registerRequest);
    }
}
