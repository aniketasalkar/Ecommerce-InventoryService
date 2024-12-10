package com.example.inventoryservice.repositories;

import com.example.inventoryservice.models.InventoryReservation;
import com.example.inventoryservice.models.InventoryReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {

    InventoryReservation save(InventoryReservation inventoryReservation);
    Optional<InventoryReservation> findByProductId(Long productId);
    Optional<InventoryReservation> findByProductIdAndOrderId(Long productId, String orderId);
    Optional<InventoryReservation> findByReservationIdAndInventoryReservationStatus(String reservationId, InventoryReservationStatus inventoryReservationStatus);
}
