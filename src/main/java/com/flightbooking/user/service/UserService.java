package com.flightbooking.user.service;

import com.flightbooking.user.dto.RegisterRequest;
import com.flightbooking.user.dto.UserResponse;

public interface UserService {
    UserResponse registeUser(RegisterRequest registerRequest);
}
