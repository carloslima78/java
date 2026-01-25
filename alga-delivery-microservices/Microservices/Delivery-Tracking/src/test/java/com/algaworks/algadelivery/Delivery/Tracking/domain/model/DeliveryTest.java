package com.algaworks.algadelivery.Delivery.Tracking.domain.model;

import com.algaworks.algadelivery.Delivery.Tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    // Test for placing a delivery
    @Test
    public void shouldChangeStatusToPlaced() {
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlaceAt());
    }

    // Helper method to create valid preparation details
    private Delivery.PreparationDetails createValidPreparationDetails() {
        ContactPoint sender = ContactPoint.builder()
                .zipCode("12345-678")
                .street("Main St")
                .number("100")
                .complement("Apt 1")
                .name("Sender Name")
                .phone("123-456-7890")
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode("98765-432")
                .street("Second St")
                .number("200")
                .complement("Suite 2")
                .name("Recipient Name")
                .phone("098-765-4321")
                .build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new java.math.BigDecimal("15.00"))
                .courierPayout(new java.math.BigDecimal("10.00"))
                .expectedDeliveryTime(java.time.Duration.ofHours(2))
                .build();
    }
}