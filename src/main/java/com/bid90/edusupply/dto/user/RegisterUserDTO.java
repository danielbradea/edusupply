package com.bid90.edusupply.dto.user;

import com.bid90.edusupply.model.Role;
import lombok.Data;

@Data
public class RegisterUserDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
    private Boolean enabled =  true;
}
