package com.example.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class InventoryReservation extends BaseModel {

    @ManyToOne(fetch = FetchType.EAGER)
    private InventoryItem inventoryItem;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private Date reservationDate;

    @Column(nullable = false)
    private Date expirationDate;

    @Column(nullable = false)
    private String reservationId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InventoryReservationStatus inventoryReservationStatus;
}
