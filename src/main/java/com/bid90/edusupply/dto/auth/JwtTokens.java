package com.bid90.edusupply.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokens {
    String accessToken;
    Long accessTokenValidTime;
    String refreshToken;
    Long refreshTokenValidTime;

}