package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.ProductDTO;

import java.util.UUID;

/**
 * Interface defining the contract for product-related operations within the application.
 * The ProductService provides methods for managing products, including creating products
 * and associating them with the corresponding seller entity.
 *
 * @author Mahmoud Shtayeh
 */
public interface ProductService {
    /**
     * Creates a new product in the system by mapping the provided product data transfer object
     * to an entity and saving it in the repository. It also associates the product with the
     * corresponding seller based on the seller ID in the provided DTO.
     *
     * @param productDTO the data transfer object containing the product details to be created,
     *                   including the seller ID for association.
     * @return the unique identifier (UUID) of the newly created product.
     */
    UUID create(ProductDTO productDTO);
}