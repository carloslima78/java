package com.algaworks.algadelivery.Delivery.Tracking.domain.model;

import lombok.*;

import java.util.UUID;

// Entity for Item
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Item {
    @EqualsAndHashCode.Include
    private UUID id;
    private String name;
    private Integer quantity;

    // Factory Method for Brand New Item.
    static Item brandNew(String name, Integer quantity) {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);
        item.setQuantity(quantity);
        return item;
    }
}
