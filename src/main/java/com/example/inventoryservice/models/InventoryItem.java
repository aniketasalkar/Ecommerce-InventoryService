package com.example.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class InventoryItem extends BaseModel {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

//    private String sku;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Column(nullable = false)
    private Integer availableQuantity;

//    @NotNull
//    @OneToMany
//    private InventoryLocation location;

    @Column(nullable = false)
    private Integer restockThreshold;

//    @Enumerated(EnumType.STRING)
//    private Status status;
}
