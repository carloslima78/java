package com.algaworks.algadelivery.Delivery.Tracking.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryStatusTest {

    @Test
    void draft_canChangeTo_waitingForCourier() {
        assertTrue(
                DeliveryStatus.DRAFT.canChangeTo(DeliveryStatus.WAITING_FOR_COURIER));
    }

    @Test
    void draft_canCannotChangeTo_inTransit() {
        assertFalse(
                DeliveryStatus.DRAFT.canChangeTo(DeliveryStatus.IN_TRANSIT));
    }
}