package com.example.inventoryservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date updatedAt;
}
