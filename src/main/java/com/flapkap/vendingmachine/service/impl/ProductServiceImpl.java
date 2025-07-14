package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.mapper.ProductMapper;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.repository.ProductRepository;
import com.flapkap.vendingmachine.service.ProductService;
import com.flapkap.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of the {@link ProductService} interface, providing business logic
 * for handling operations related to products in the vending machine system.
 * This class is annotated as a service component and leverages dependency injection
 * to access required services and repositories.
 *
 * @author Mahmoud Shtayeh
 */
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
}
