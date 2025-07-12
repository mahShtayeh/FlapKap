package com.flapkap.vendingmachine.repository;

import com.flapkap.vendingmachine.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Product} entities.
 * Extends the {@link JpaRepository} interface to provide CRUD operations,
 * pagination, and query support for the `Product` entity identified by a {@link UUID}.
 *
 * @author Mahmoud Shtayeh
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {
}