package com.bid90.edusupply.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "equipment_tb")
@Data
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category; //todo

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDate purchaseDate;

    private String inventoryCode;
}