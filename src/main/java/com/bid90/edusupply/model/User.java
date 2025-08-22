package com.bid90.edusupply.model;

import jakarta.persistence.*;
import lombok.Data;

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

}
