package com.flightbooking.user.controller;

import com.flightbooking.user.dto.RegisterRequest;
import com.flightbooking.user.dto.UserResponse;
import com.flightbooking.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest registerRequest){
        return userService.registeUser(registerRequest);
    }
}
