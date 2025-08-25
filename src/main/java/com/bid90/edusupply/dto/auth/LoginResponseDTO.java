package com.bid90.edusupply.dto.auth;

import com.bid90.edusupply.dto.user.UserDTO;
import lombok.Data;

@Data
public class LoginResponseDTO {

    String accessToken;
    String  refreshToken;
    Long accessTokenValidTime;
    Long refreshTokenValidTime;
    UserDTO user;
}
