package com.example.inventoryservice.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryItemRequestDto {

    @NotNull(message = "product Id required")
    private Long productId;

    @NotNull(message = "Product name required.")
    private String productName;

//    private String sku;

    @NotNull(message = "Quantity required.")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 10000, message = "Exceeded maximum quantity of 10000")
    private Integer quantity;

//    @NotNull
//    @OneToMany
//    private InventoryLocation location;

}
