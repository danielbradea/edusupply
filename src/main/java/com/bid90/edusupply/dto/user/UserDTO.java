package com.bid90.edusupply.dto.user;

import com.bid90.edusupply.dto.group.GroupDTO;
import com.bid90.edusupply.model.Group;
import com.bid90.edusupply.model.Role;
import com.bid90.edusupply.model.User;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private List<GroupDTO> group;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.group = user.getGroups().stream().map(GroupDTO::new).toList();
        this.enabled = user.isEnabled();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
