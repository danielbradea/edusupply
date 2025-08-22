package com.bid90.edusupply.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String name;
    private String email;
    private String password;
    private Boolean enabled =  true;
}
