package com.example.inventoryservice.services;

import com.example.inventoryservice.dtos.AddQuantityDto;
import com.example.inventoryservice.exceptions.*;
import com.example.inventoryservice.models.InventoryItem;
import com.example.inventoryservice.models.InventoryReservationStatus;
import com.example.inventoryservice.models.InventoryTransaction;
import com.example.inventoryservice.models.TransactionType;
import com.example.inventoryservice.producer.KafkaEventProducer;
import com.example.inventoryservice.producer.ProduceRestockEvent;
import com.example.inventoryservice.repositories.InventoryItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class InventoryService implements IInventoryService {

    @Autowired
    InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ProduceRestockEvent produceRestockEvent;

    private final int initialInventoryQuantity = 0;

    private final int initialInventoryReservedQuantity = 0;

    private final int thresholdQuantity = 100;

    @Override
    public InventoryItem createInventoryItem(InventoryItem inventoryItem) {

        //User validation



        if (inventoryItemRepository.findByProductId(inventoryItem.getProductId()).isPresent()) {
            throw new InventoryAlreadyExists("Inventory with given product already exists.");
        }

//        if (inventoryItem.getQuantity() < 0 || inventoryItem.getQuantity() > 10000) {
//            throw new InvalidQuantityException("Invalid Quantity");
//        }

        inventoryItem.setReservedQuantity(initialInventoryReservedQuantity);
        inventoryItem.setQuantity(initialInventoryQuantity);
        inventoryItem.setAvailableQuantity(inventoryItem.getQuantity() - inventoryItem.getReservedQuantity());
        inventoryItem.setRestockThreshold(thresholdQuantity);

        Date now = new Date();
        inventoryItem.setCreatedAt(now);
        inventoryItem.setUpdatedAt(now);

        System.out.println("ProductId: " + inventoryItem.getProductId());

        return inventoryItemRepository.save(inventoryItem);
    }

    @Override
    public InventoryItem getInventoryItem(Long productId) {

        if (productId < 0) {
            throw new InvalidProductIdException("Invalid Product Id");
        }

        InventoryItem inventoryItem = inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryDoesNotExist("Inventory Does Not exist with product Id: " + productId));

        return inventoryItem;
    }

    @Override
    public InventoryItem updateInventory(Long productId, InventoryItem inventoryItem) {
        if (productId < 0) {
            throw new InvalidProductIdException("Invalid Product Id");
        }

        InventoryItem storedInventoryItem = inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryDoesNotExist("Inventory Does Not exist with product Id: " + productId));

        System.out.println("available Count: " + storedInventoryItem.getAvailableQuantity());

        Field[] fields = inventoryItem.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            try {

                Object value = field.get(inventoryItem);
                if (value == null) {
                    continue;
                }
                Field inventoryItemField = InventoryItem.class.getDeclaredField(field.getName());
                inventoryItemField.setAccessible(true);
                inventoryItemField.set(storedInventoryItem, value);
            } catch (NoSuchFieldException e) {
                throw new FieldNotFoundException("Invalid Filed");
            } catch (IllegalAccessException e) {
                throw new NonModifiablefieldException("Field not modifiable");
            }

        }

        Date now = new Date();
        storedInventoryItem.setUpdatedAt(now);

        storedInventoryItem.setAvailableQuantity(storedInventoryItem.getQuantity() - storedInventoryItem.getReservedQuantity());

        InventoryItem savedInventoryItem = inventoryItemRepository.save(storedInventoryItem);

        return savedInventoryItem;
    }

    @Override
    public InventoryItem addQuantity(Long productId, AddQuantityDto addQuantityDto) {

        int quantity = addQuantityDto.getQuantity();

        if (productId < 0) {
            throw new InvalidProductIdException("Invalid Product Id");
        }

        if (quantity < 0 || quantity > 10000) {
            throw new InvalidQuantityException("Quantity should in in range of 0 to 10000");
        }

        InventoryItem inventoryItem = inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryDoesNotExist("Inventory Does not exists for the product Id: " + productId));

        Date now = new Date();

        inventoryItem.setQuantity(inventoryItem.getQuantity() + quantity);
        inventoryItem.setAvailableQuantity(inventoryItem.getAvailableQuantity() + quantity);
        inventoryItem.setUpdatedAt(now);

        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        String topic = "log-inventory-transaction";
        produceRestockEvent.produceTransactionKafkaEvent(topic, savedInventoryItem, quantity);

        return savedInventoryItem;
    }
}
