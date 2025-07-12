package com.flapkap.vendingmachine.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents a Product entity in the vending machine model.
 *
 * @author Mahmoud Shtayeh
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    /**
     * Unique identifier for the Product entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * Represents the cost of the product.
     */
    private double cost;

    /**
     * Represents the quantity of the product available in stock.
     */
    private int amount;

    /**
     * A brief description or details about the product.
     */
    private String description;

    /**
     * Represents the seller associated with the product.
     * The seller is a user entity who owns or is responsible for the product.
     * This is a mandatory field and is linked to the `User` entity via a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
}