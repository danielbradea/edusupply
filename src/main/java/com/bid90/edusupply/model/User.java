package com.bid90.edusupply.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "email" , nullable = false, unique = true)
    private String email;

    @Column(name = "password" , nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "enabled" , nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
