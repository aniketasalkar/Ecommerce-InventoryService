package com.example.inventoryservice.utils;

import com.example.inventoryservice.dtos.InventoryItemRequestDto;
import com.example.inventoryservice.dtos.InventoryItemResponseDto;
import com.example.inventoryservice.models.InventoryItem;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper implements IDtoMapper {
    @Override
    public InventoryItem fromInventoryItemDto(InventoryItemRequestDto inventoryItemRequestDto) {
        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setProductId(inventoryItemRequestDto.getProductId());
        inventoryItem.setProductName(inventoryItemRequestDto.getProductName());
        inventoryItem.setQuantity(inventoryItemRequestDto.getQuantity());

        return inventoryItem;
    }

    @Override
    public InventoryItemResponseDto fromInventoryItem(InventoryItem inventoryItem) {
        InventoryItemResponseDto inventoryItemResponseDto = new InventoryItemResponseDto();

        inventoryItemResponseDto.setProductId(inventoryItem.getProductId());
        inventoryItemResponseDto.setProductName(inventoryItem.getProductName());
        inventoryItemResponseDto.setQuantity(inventoryItem.getQuantity());
        inventoryItemResponseDto.setReservedQuantity(inventoryItem.getReservedQuantity());
        inventoryItemResponseDto.setAvailableQuantity(inventoryItem.getAvailableQuantity());

        return inventoryItemResponseDto;
    }
}
