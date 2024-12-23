package com.example.inventoryservice.utils;

import com.example.inventoryservice.dtos.InventoryItemRequestDto;
import com.example.inventoryservice.dtos.InventoryItemResponseDto;
import com.example.inventoryservice.models.InventoryItem;

public interface IDtoMapper {
    InventoryItem fromInventoryItemDto(InventoryItemRequestDto inventoryItemRequestDto);
    InventoryItemResponseDto fromInventoryItem(InventoryItem inventoryItem);
}
