package com.flapkap.vendingmachine.model;

/**
 * Enumeration representing the roles within the vending machine system.
 * This enum defines the possible roles a user can have, determining their
 * level of access and the actions they are authorized to perform.
 * The roles include:
 * - BUYER: A user who can purchase items from the vending machine.
 * - SELLER: A user who can list and manage products available in the vending machine.
 *
 * @author Mahmoud Shtayeh
 */
public enum Role {
    BUYER, SELLER
}