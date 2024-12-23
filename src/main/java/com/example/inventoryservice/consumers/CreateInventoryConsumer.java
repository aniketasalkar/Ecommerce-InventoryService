package com.example.inventoryservice.consumers;

import com.example.inventoryservice.dtos.InventoryItemRequestDto;
import com.example.inventoryservice.models.InventoryItem;
import com.example.inventoryservice.services.IInventoryService;
import com.example.inventoryservice.utils.DtoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class CreateInventoryConsumer {

    @Autowired
    IInventoryService inventoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    private Validator validator;

    @KafkaListener(topics = "initialize-inventory", groupId = "newInventoryItem")
    public void createInventoryItem(String inventoryData) {
        try {
            log.info("Received kafka event for topic: initialize-inventory");
            InventoryItemRequestDto inventoryItemRequestDto = objectMapper.readValue(inventoryData, InventoryItemRequestDto.class);

            Set<ConstraintViolation<InventoryItemRequestDto>> violations = validator.validate(inventoryItemRequestDto);
            if (!violations.isEmpty()) {
                // If there are validation errors, log them and throw an exception
                StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<InventoryItemRequestDto> violation : violations) {
                    errorMessage.append(violation.getMessage()).append("; ");
                }
                log.error(errorMessage.toString());
                throw new IllegalArgumentException(errorMessage.toString());
            }

            InventoryItem inventoryItem = dtoMapper.fromInventoryItemDto(inventoryItemRequestDto);
            InventoryItem storedItem = inventoryService.createInventoryItem(inventoryItem);

            if (storedItem.getId() == null) {
                log.error("Error occured while saving to database.");
                throw new RuntimeException("Error occured while saving to database.");
            }

            log.info("Inventory Item created for product Id: " + storedItem.getProductId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
