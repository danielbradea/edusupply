package com.bid90.edusupply.service;

import com.bid90.edusupply.dto.auth.LoginRequestDTO;
import com.bid90.edusupply.dto.auth.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
