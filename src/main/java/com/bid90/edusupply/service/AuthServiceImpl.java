package com.bid90.edusupply.service;

import com.bid90.edusupply.dto.auth.LoginRequestDTO;
import com.bid90.edusupply.dto.auth.LoginResponseDTO;
import com.bid90.edusupply.dto.user.UserDTO;
import com.bid90.edusupply.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserService userService, JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
       var optionalUser = userService.getUserByEmail(loginRequestDTO.getEmail());
       var userLogin = optionalUser.orElseThrow(() -> new AuthenticationException("Invalid email or password",HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches( loginRequestDTO.getPassword(), userLogin.getPassword())) {
            throw new AuthenticationException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }


        try {

            // Generate claims for access and refresh tokens
            Map<String, Object> accessTokenClaims = createClaims(UUID.randomUUID().toString(), userLogin.getRole().name(), false);
            Map<String, Object> refreshTokenClaims = createClaims(UUID.randomUUID().toString(), null, true);

            // Generate new tokens
            var tokens = jwtTokenService.generateTokens(userLogin.getId().toString(), accessTokenClaims, refreshTokenClaims);
            var loginResponse = new LoginResponseDTO();
            // Populate response
            loginResponse.setAccessToken(tokens.getAccessToken());
            loginResponse.setRefreshToken(tokens.getRefreshToken());
            loginResponse.setAccessTokenValidTime(tokens.getAccessTokenValidTime());
            loginResponse.setRefreshTokenValidTime(tokens.getRefreshTokenValidTime());
            loginResponse.setUser(new UserDTO(userLogin));

            return  loginResponse;
        } catch (Exception e) {
            throw new AuthenticationException("Failed to renew token", HttpStatus.UNAUTHORIZED);
        }

    }



    private Map<String, Object> createClaims(String uuid, String role, boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", uuid);
        if (isRefreshToken) {
            claims.put("refresh", true);
        } else {
            claims.put("role", role);
        }
        return claims;
    }
}
