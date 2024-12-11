package com.example.inventoryservice.services;

import com.example.inventoryservice.clients.KafkaProducerClient;
import com.example.inventoryservice.dtos.RevokeReservationDto;
import com.example.inventoryservice.exceptions.FieldNotFoundException;
import com.example.inventoryservice.exceptions.InventoryDoesNotExist;
import com.example.inventoryservice.exceptions.InventoryReservationAlreadyExists;
import com.example.inventoryservice.exceptions.InventoryReservationDoesNotExists;
import com.example.inventoryservice.models.*;
import com.example.inventoryservice.repositories.InventoryItemRepository;
import com.example.inventoryservice.repositories.InventoryReservationRepository;
import com.example.inventoryservice.utils.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class InventoryReservationService implements IInventoryReservatonService {

    @Autowired
    private InventoryReservationRepository inventoryReservationRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(InventoryReservationService.class);

    private final int reservationValidityInMin = 30;

    private final int reservationIdCharLength = 16;

    @Transactional
    @Override
    public InventoryReservation reserveInventoryItem(InventoryReservation inventoryReservation) {

        InventoryItem inventoryItem = inventoryItemRepository.findByProductId(inventoryReservation.getProductId()).
                orElseThrow(() -> new InventoryDoesNotExist("Inventory with product Id " + inventoryReservation.getProductId() + " does not exist."));

        if (inventoryReservationRepository.findByProductIdAndOrderId(inventoryReservation.getProductId(), inventoryReservation.getOrderId()).isPresent()) {
            throw new InventoryReservationAlreadyExists("Reservation with order id " + inventoryReservation.getOrderId() + " already exists");
        }

        Date now = new Date();

        inventoryItem.setReservedQuantity(inventoryItem.getReservedQuantity() + inventoryReservation.getQuantity());
        inventoryItem.setAvailableQuantity(inventoryItem.getAvailableQuantity() - inventoryReservation.getQuantity());
        inventoryItem.setUpdatedAt(now);

        InventoryItem storedInventoryItem = inventoryItemRepository.save(inventoryItem);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, reservationValidityInMin);
        Date expiryDate = calendar.getTime();

        inventoryReservation.setInventoryItem(storedInventoryItem);
        inventoryReservation.setReservationDate(now);
        inventoryReservation.setExpirationDate(expiryDate);
        inventoryReservation.setCreatedAt(now);
        inventoryReservation.setUpdatedAt(now);
        inventoryReservation.setInventoryReservationStatus(InventoryReservationStatus.RESERVED);
        inventoryReservation.setReservationId(IdGenerator.generateReservationId(reservationIdCharLength));

        return inventoryReservationRepository.save(inventoryReservation);
    }

    @Transactional
    @Override
    public InventoryReservation revokeReservation(RevokeReservationDto revokeReservationDto) {
        InventoryReservation inventoryReservation = inventoryReservationRepository.findByReservationIdAndInventoryReservationStatus(
                revokeReservationDto.getReservationId(), InventoryReservationStatus.RESERVED)
                .orElseThrow(() -> new InventoryReservationDoesNotExists("Reservation does not exists with reservation Id: "
                        + revokeReservationDto.getReservationId()));

        InventoryItem inventoryItem = inventoryReservation.getInventoryItem();
        InventoryReservationStatus inventoryReservationStatus;
        try {
            inventoryReservationStatus = InventoryReservationStatus.valueOf(revokeReservationDto.getRevokeType().trim().toUpperCase());
        } catch (Exception exception) {
            throw new FieldNotFoundException("revokeType should be either COMPLETED, CANCELLED");
        }

        Date now = new Date();

        inventoryItem.setReservedQuantity(inventoryItem.getReservedQuantity() - inventoryReservation.getQuantity());
        if (inventoryReservationStatus == InventoryReservationStatus.CANCELLED) {
            inventoryItem.setAvailableQuantity(inventoryItem.getAvailableQuantity() + inventoryReservation.getQuantity());
        } else if (inventoryReservationStatus == InventoryReservationStatus.COMPLETED) {
            inventoryItem.setQuantity(inventoryItem.getQuantity() - inventoryReservation.getQuantity());
        }
        inventoryItem.setUpdatedAt(now);

        InventoryItem storedInventoryItem = inventoryItemRepository.save(inventoryItem);

        inventoryReservation.setInventoryReservationStatus(InventoryReservationStatus.valueOf(revokeReservationDto.getRevokeType().trim().toUpperCase()));
        inventoryReservation.setUpdatedAt(now);
        inventoryReservation.setInventoryItem(storedInventoryItem);

        InventoryReservation updatedInventoryReservation = inventoryReservationRepository.save(inventoryReservation);

        String topic = "log-inventory-transaction";
        if (updatedInventoryReservation.getInventoryReservationStatus() == InventoryReservationStatus.COMPLETED) {
            InventoryTransaction inventoryTransaction = new InventoryTransaction();

            inventoryTransaction.setInventoryItem(storedInventoryItem);
            inventoryTransaction.setTransactionDate(now);
            inventoryTransaction.setTransactionType(TransactionType.SALE);
            inventoryTransaction.setQuantity(updatedInventoryReservation.getQuantity());
            inventoryTransaction.setDescription("Sale Transaction");

            try {
                kafkaProducerClient.sendMessage(topic, objectMapper.writeValueAsString(inventoryTransaction));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return updatedInventoryReservation;
    }
}
