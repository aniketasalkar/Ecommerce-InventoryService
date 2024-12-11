package com.example.inventoryservice.consumers;

import com.example.inventoryservice.models.InventoryTransaction;
import com.example.inventoryservice.services.IInventoryTransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LogTransactionComsumer implements ILogTransactionComsumer {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    IInventoryTransactionService inventoryTransactionService;

    private static final Logger logger = LoggerFactory.getLogger(LogTransactionComsumer.class);


    @KafkaListener(topics = "log-inventory-transaction", groupId = "logTransaction")
    @Override
    public void consumeLogTransactionEvent(String transaction) {
        InventoryTransaction inventoryTransaction;
        try {
            inventoryTransaction = objectMapper.readValue(transaction, InventoryTransaction.class);

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        logger.info("Received kafka Event for : {log-inventory-transaction}");
        inventoryTransactionService.logTransaction(inventoryTransaction);
    }
}
