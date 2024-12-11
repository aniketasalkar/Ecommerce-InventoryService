package com.example.inventoryservice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class InventoryTransaction extends BaseModel {

    @Column(nullable = false)
    private String transactionID;

    @ManyToOne(fetch = FetchType.EAGER)
    private InventoryItem inventoryItem;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;  // e.g., "restock", "sale"

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Date transactionDate;

    private String description;
}
