package com.example.inventoryservice.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class InventoryItemPatchRequestDto {

    private String productName;

//    private String sku;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 10000, message = "Exceeded maximum quantity of 10000")
    private Integer quantity;

//    @NotNull
//    @OneToMany
//    private InventoryLocation location;
}
