package com.example.inventoryservice.dtos;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class InventoryItemResponseDto {

    private Long productId;

    private String productName;

//    private String sku;

    private Integer quantity;

    private Integer reservedQuantity;

    private Integer availableQuantity;

//    @NotNull
//    @OneToMany
//    private InventoryLocation location;

//    private Integer restockThreshold;
}
