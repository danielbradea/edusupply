package com.bid90.edusupply.dto.group;

import com.bid90.edusupply.model.Group;
import com.bid90.edusupply.model.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GroupDTO {

    private Long id;
    private String name;
    private String description;
    private Boolean immutable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GroupDTO(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.immutable = group.isImmutable();
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getUpdatedAt();
    }
}
