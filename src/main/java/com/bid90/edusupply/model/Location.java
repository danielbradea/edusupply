package com.bid90.edusupply.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "location_tb")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private LocationType type; // tip: STORAGE, CLASSROOM, LAB

    private int floor;

    private String building;

    private String description;

}