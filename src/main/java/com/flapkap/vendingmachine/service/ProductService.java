package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.exception.ProductNotFoundException;
import com.flapkap.vendingmachine.web.response.ProductResponse;

import java.nio.channels.AcceptPendingException;
import java.util.List;
import java.util.UUID;

/**
 * Interface defining the contract for product-related operations within the application.
 * The ProductService provides methods for managing boughtProducts, including creating boughtProducts
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

    /**
     * Reads and retrieves the details of a product identified by the specified unique identifier.
     *
     * @param productId the unique identifier of the product to be retrieved
     * @return a {@code ProductDTO} representing the product details, such as ID, name, cost, amount,
     * description, and associated seller information
     */
    ProductDTO read(UUID productId);

    /**
     * Retrieves a list of all boughtProducts and maps them to their response representation.
     *
     * @return a list of {@code ProductResponse} objects, each representing a product
     * with its details such as ID, name, cost, amount, and description.
     */
    List<ProductResponse> readAll();

    /**
     * Updates an existing product entity with the provided data transfer object and ensures
     * the updated product belongs to the specified seller.
     *
     * @param productId  the unique identifier of the product to be updated.
     * @param productDTO the data transfer object containing the updated product details.
     * @param sellerId   the unique identifier of the seller to verify ownership of the product.
     * @return the updated product details as a {@code ProductResponse} object.
     * @throws ProductNotFoundException if the product with the given {@code productId} is not found.
     * @throws AcceptPendingException   if the {@code sellerId} does not match the owner of the product.
     */
    ProductResponse update(UUID productId, ProductDTO productDTO, UUID sellerId);

    /**
     * Deletes a product from the system by its unique identifier and verifies the operation
     * by requiring the owner's ID for authorization.
     *
     * @param productId the unique identifier of the product to be deleted
     * @param id        the unique identifier of the user or seller performing the deletion
     */
    void delete(UUID productId, UUID id);
}