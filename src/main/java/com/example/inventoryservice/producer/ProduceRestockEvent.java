package com.example.inventoryservice.producer;

import com.example.inventoryservice.clients.KafkaProducerClient;
import com.example.inventoryservice.models.InventoryItem;
import com.example.inventoryservice.models.InventoryTransaction;
import com.example.inventoryservice.models.TransactionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProduceRestockEvent implements KafkaEventProducer {

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void produceTransactionKafkaEvent(String topic, InventoryItem inventoryItem, int quantity) {
        InventoryTransaction inventoryTransaction = new InventoryTransaction();

        Date now = new Date();
        inventoryTransaction.setInventoryItem(inventoryItem);
        inventoryTransaction.setTransactionDate(now);
        inventoryTransaction.setTransactionType(TransactionType.RESTOCK);
        inventoryTransaction.setQuantity(quantity);
        inventoryTransaction.setDescription("Restock Transaction");

        try {
            kafkaProducerClient.sendMessage(topic, objectMapper.writeValueAsString(inventoryTransaction));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
