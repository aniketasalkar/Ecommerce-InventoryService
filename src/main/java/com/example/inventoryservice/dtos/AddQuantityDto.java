package com.example.inventoryservice.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddQuantityDto {

    @Min(1)
    @Max(10000)
    @NotNull
    private int quantity;

}
