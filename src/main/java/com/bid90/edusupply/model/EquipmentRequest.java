package com.bid90.edusupply.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "equipment_request_tb")
@Data
@EqualsAndHashCode(callSuper = true)
public class EquipmentRequest extends Request {

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
}
