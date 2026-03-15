package com.flightbooking.auth.service;

import com.flightbooking.auth.dto.AuthResponse;
import com.flightbooking.auth.dto.LoginRequestDto;

public interface AuthService {

    AuthResponse login(LoginRequestDto loginRequest);
}
