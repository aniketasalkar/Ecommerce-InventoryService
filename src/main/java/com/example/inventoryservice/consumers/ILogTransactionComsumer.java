package com.example.inventoryservice.consumers;

public interface ILogTransactionComsumer {
    void consumeLogTransactionEvent(String transaction);
}
