package com.flapkap.vendingmachine.mapper;

import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.web.request.ProductCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * A MapStruct mapper interface used for mapping between Product-related data models.
 * This mapper is primarily used to convert between `ProductCreationRequest` and `ProductDTO`,
 * as well as between `ProductDTO` and the `Product` entity.
 * The generated implementation of this interface is managed as a Spring component.
 *
 * @author Mahmoud Shtayeh
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    /**
     * Converts a {@link ProductCreationRequest} and a seller's unique identifier
     * into a {@link ProductDTO}.
     *
     * @param request  the product creation request containing details such as name,
     *                 cost, amount, and description of the product.
     * @param sellerId the unique identifier of the seller who owns the product.
     * @return a {@link ProductDTO} containing the mapped product details along
     * with the seller's identifier.
     */
    ProductDTO toDTO(ProductCreationRequest request, UUID sellerId);

    /**
     * Maps a {@link ProductDTO} object and a {@link User} entity to a {@link Product} entity.
     * This method sets the `seller` field in the resulting `Product` entity using
     * the provided `seller` parameter. The `id` field is explicitly ignored during the mapping.
     *
     * @param productDTO the {@link ProductDTO} object containing product details such as name, cost, amount, and description.
     * @param seller     the {@link User} entity representing the seller of the product.
     * @return a {@link Product} entity containing the mapped product details and associated seller information.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", source = "seller")
    Product toEntity(ProductDTO productDTO, User seller);
}