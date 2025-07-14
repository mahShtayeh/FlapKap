package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.exception.ProductNotFoundException;
import com.flapkap.vendingmachine.mapper.ProductMapper;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.repository.ProductRepository;
import com.flapkap.vendingmachine.service.ProductService;
import com.flapkap.vendingmachine.service.UserService;
import com.flapkap.vendingmachine.util.AssertUtil;
import com.flapkap.vendingmachine.web.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link ProductService} interface, providing business logic
 * for handling operations related to products in the vending machine system.
 * This class is annotated as a service component and leverages dependency injection
 * to access required services and repositories.
 *
 * @author Mahmoud Shtayeh
 */
@Transactional
@Service("productService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    /**
     * Repository interface for managing {@link Product} entities, injected as a dependency
     * for performing persistence operations related to products. This variable is used
     * by the {@code ProductServiceImpl} service to handle product-related data access,
     * including saving and retrieving product entities from the database.
     */
    private final ProductRepository productRepository;

    /**
     * Mapper interface for converting between {@link ProductDTO} and {@link Product} entities.
     * Injected as a dependency into the service layer to facilitate transformations between
     * domain objects and Data Transfer Objects (DTOs). This ensures a clear separation of concerns
     * between the application layers and simplifies data handling processes.
     */
    private final ProductMapper productMapper;

    /**
     * Service interface for managing user-related operations. This variable is injected
     * as a dependency into the service layer to enable access to business logic for handling
     * user operations. It provides methods for managing {@link User} entities, including creating,
     * reading, and authenticating users. The {@code userService} is used to interact with and
     * retrieve user-related data and processes critical for application functionalities.
     */
    private final UserService userService;

    /**
     * Creates a new product in the system by mapping the provided product data transfer object
     * to an entity and saving it in the repository. It also associates the product with the
     * corresponding seller based on the seller ID in the provided DTO.
     *
     * @param productDTO the data transfer object containing the product details to be created,
     *                   including the seller ID for association.
     * @return the unique identifier (UUID) of the newly created product.
     */
    @Override
    public UUID create(final ProductDTO productDTO) {
        final User seller = userService.read(productDTO.sellerId());
        final Product product = productMapper.toEntity(productDTO, seller);
        return productRepository.save(product).getId();
    }

    /**
     * Retrieves a list of all products and maps them to their response representation.
     *
     * @return a list of {@code ProductResponse} objects, each representing a product
     * with its details such as ID, name, cost, amount, and description.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> readAll() {
        final List<Product> products = productRepository.findAll();
        return productMapper.toResponseList(products);
    }

    /**
     * Updates an existing product entity with the provided data transfer object and ensures
     * the updated product belongs to the specified seller.
     *
     * @param productId  the unique identifier of the product to be updated.
     * @param productDTO the data transfer object containing the updated product details.
     * @param sellerId   the unique identifier of the seller to verify ownership of the product.
     * @return the updated product details as a {@code ProductResponse} object.
     * @throws ProductNotFoundException if the product with the given {@code productId} is not found.
     * @throws AccessDeniedException    if the {@code sellerId} does not match the owner of the product.
     */
    @Override
    public ProductResponse update(final UUID productId, final ProductDTO productDTO, final UUID sellerId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> ProductNotFoundException.builder()
                        .productId(productId)
                        .message("error.product.notFound")
                        .build());

        AssertUtil.isTrue(sellerId.equals(product.getSeller().getId()),
                () -> new AccessDeniedException("ACCESS_DENIED"));
        productMapper.updateEntity(productDTO, product);
        return productMapper.toResponse(product);
    }
}
