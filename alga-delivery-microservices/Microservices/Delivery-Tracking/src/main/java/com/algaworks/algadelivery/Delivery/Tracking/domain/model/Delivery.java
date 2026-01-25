package com.algaworks.algadelivery.Delivery.Tracking.domain.model;

import com.algaworks.algadelivery.Delivery.Tracking.domain.exception.DomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

// Aggregate Root for Delivery
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    @Setter
    private DeliveryStatus status;

    private OffsetDateTime placeAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    private ContactPoint sender;
    private ContactPoint recipient;

    private List<Item> items = new ArrayList<>();

    // Factory Method for Draft Delivery
    public static Delivery draft(){
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    // Method to Add Item to Delivery
    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity);
        this.items.add(item);
        return item.getId();
    }

    // Method to Change Item Quantity in Delivery
    public void changeItemQuantity(UUID itemId, Integer quantity) {
       Item item = getItems().stream()
               .filter(i -> i.getId().equals(itemId))
               .findFirst()
               .orElseThrow(() -> new IllegalArgumentException("Item not found"));
       calculateTotalItems();
    }

    // Method to Remove Item from Delivery
    public void removeItem(UUID itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
    }

    // Method to Remove All Items from Delivery
    public void removeItens() {
        this.items.clear();
    }

    // Method to Edit Preparation Details
    public void editPreparationDetails(PreparationDetails details) {
        verifyIfCanBeEdited();

        this.setSender(details.getSender());
        this.setRecipient(details.getRecipient());
        this.setDistanceFee(details.getDistanceFee());
        this.setCourierPayout(details.getCourierPayout());
        this.setExpectedDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveryTime()));
        this.setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    // Method to Place Delivery
    public void place(){
        this.setStatus(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlaceAt(OffsetDateTime.now());
    }

    // Method to Pick Up Delivery by Courier
    public void pickUp(UUID courierId) {
        this.setCourierId(courierId);
        this.setStatus(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(OffsetDateTime.now());
    }

    // Method to Mark Delivery as Delivered
    public void markAsDelivered() {
        this.setStatus(DeliveryStatus.DELIVERED);
        this.setFulfilledAt(OffsetDateTime.now());
    }

    // Unmodifiable List Getter for Items
    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    // Method to Recalculate Total Items
   private void calculateTotalItems() {
       this.totalItems = getItems().stream()
               .mapToInt(Item::getQuantity)
               .sum();
   }

    // Method to Verify if Delivery Can Be Placed
   private void verifyIfCanBePlaced() {
       // Check if all required fields are filled
       if (!isFilled()) {
           throw new DomainException();
       }
       // Check if current status is DRAFT
       if(!getStatus().equals(DeliveryStatus.DRAFT)){
           throw new DomainException();
       }
   }

   // Method to Verify if Delivery Can Be Edited
   private void verifyIfCanBeEdited() {
        // Check if current status is DRAFT
        if(!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException();
        }
    }

   // Method to Check if Delivery is Properly Filled
   private boolean isFilled() {
       return this.getSender() != null
               && this.getRecipient() != null
               && this.getTotalCost() != null;
   }

   // Method to Change Delivery Status with Validation
   private void changeStatusTo(DeliveryStatus newStatus) {
       if (newStatus != null && this.getStatus().canNotChangeTo(newStatus)) {
           throw new DomainException("Cannot change status from "
                   + this.getStatus() + " to " + newStatus);
       }
       this.setStatus(newStatus);
   }

   // Preparation Details to Set Up Delivery
   @Getter
   @AllArgsConstructor
   @Builder
   public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
   }
}
