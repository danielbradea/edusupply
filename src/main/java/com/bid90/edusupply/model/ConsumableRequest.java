package com.bid90.edusupply.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "consumable_request_tb")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumableRequest extends Request {

    @ManyToOne
    @JoinColumn(name = "consumable_id", nullable = false)
    private Consumable consumable;

    private int quantity; // Cantitatea solicitată
}