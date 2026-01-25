package com.algaworks.algadelivery.Delivery.Tracking.domain.model;

import java.util.List;

// Enum representing the status of a delivery
public enum DeliveryStatus {
    DRAFT,
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERED(IN_TRANSIT);

    // List of statuses from which the current status can be changed
    private final List<DeliveryStatus> previousStatuses;

    // Constructor to initialize the previous statuses
    DeliveryStatus(DeliveryStatus... previousStatuses) {
        this.previousStatuses = List.of(previousStatuses);
    }

    // Method to check if the status can not be changed to the new status
    public boolean canNotChangeTo(DeliveryStatus newStatus) {
        DeliveryStatus current = this;
        return !newStatus.previousStatuses.contains(current);
    }

    // Method to check if the status can be changed to the new status
    public boolean canChangeTo(DeliveryStatus newStatus) {
        return !canNotChangeTo(newStatus);
    }
}
