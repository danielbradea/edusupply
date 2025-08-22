package com.bid90.edusupply.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "consumable_tb")
@Data
public class Consumable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int quantity;

    private String unit;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}