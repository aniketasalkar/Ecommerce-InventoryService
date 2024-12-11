package com.example.inventoryservice.producer;

import com.example.inventoryservice.models.InventoryItem;

public interface KafkaEventProducer {
    void produceTransactionKafkaEvent(String topic, InventoryItem inventoryItem, int quantity);
}
