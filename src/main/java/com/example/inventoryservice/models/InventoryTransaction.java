package com.example.inventoryservice.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class InventoryTransaction extends BaseModel {

    @Column(nullable = false)
    private Long inventoryItemId;

    @Column(nullable = false)
    private TransactionType transactionType;  // e.g., "restock", "sale"

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Date transactionDate;

    private String description;
}
