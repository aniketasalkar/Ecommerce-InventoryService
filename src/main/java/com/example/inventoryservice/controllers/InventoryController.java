package com.example.inventoryservice.controllers;

import com.example.inventoryservice.dtos.*;
import com.example.inventoryservice.models.InventoryItem;
import com.example.inventoryservice.models.InventoryReservation;
import com.example.inventoryservice.services.IInventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/inventory/")
public class InventoryController {

    @Autowired
    IInventoryService inventoryService;

    @PostMapping("/inventory_item/create")
    public ResponseEntity<InventoryItemResponseDto> createInventoryItem(@RequestBody @Valid InventoryItemRequestDto inventoryItemRequestDto) {

        try {
            InventoryItem inventoryItem = inventoryService.createInventoryItem(from(inventoryItemRequestDto));

            return new ResponseEntity<>(from(inventoryItem), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @GetMapping("/inventory_item/get/{product_id}")
    public ResponseEntity<InventoryItemResponseDto> getInventoryItem(@PathVariable("product_id") Long productId) {
        try {
            InventoryItem inventoryItem = inventoryService.getInventoryItem(productId);

            return new ResponseEntity<>(from(inventoryItem), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PatchMapping("/inventory_iem/update/{product_id}")
    public ResponseEntity<InventoryItemResponseDto> updateInventoryItem(@PathVariable("product_id") Long productId,
                                                                        @RequestBody InventoryItemPatchRequestDto inventoryItemPatchRequestDto) {
        try {
            InventoryItem inventoryItem = inventoryService.updateInventory(productId, from(inventoryItemPatchRequestDto));

            return new ResponseEntity<>(from(inventoryItem), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    private InventoryItem from(InventoryItemRequestDto inventoryItemRequestDto) {
        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setProductId(inventoryItemRequestDto.getProductId());
        inventoryItem.setProductName(inventoryItemRequestDto.getProductName());
        inventoryItem.setQuantity(inventoryItemRequestDto.getQuantity());

        return inventoryItem;
    }

    private InventoryItemResponseDto from(InventoryItem inventoryItem) {
        InventoryItemResponseDto inventoryItemResponseDto = new InventoryItemResponseDto();

        inventoryItemResponseDto.setProductId(inventoryItem.getProductId());
        inventoryItemResponseDto.setProductName(inventoryItem.getProductName());
        inventoryItemResponseDto.setQuantity(inventoryItem.getQuantity());
        inventoryItemResponseDto.setReservedQuantity(inventoryItem.getReservedQuantity());
        inventoryItemResponseDto.setAvailableQuantity(inventoryItem.getAvailableQuantity());

        return inventoryItemResponseDto;
    }

    private InventoryItem from(InventoryItemPatchRequestDto inventoryItemPatchRequestDto) {
        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setQuantity(inventoryItemPatchRequestDto.getQuantity());
        inventoryItem.setProductName(inventoryItemPatchRequestDto.getProductName());

        return inventoryItem;
    }
}
