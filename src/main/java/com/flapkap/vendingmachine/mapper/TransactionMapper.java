package com.flapkap.vendingmachine.mapper;

import com.flapkap.vendingmachine.dto.BuyDTO;
import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.web.response.BuyResponse;
import com.flapkap.vendingmachine.web.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * TransactionMapper is an interface that defines the mapping between domain objects and their
 * corresponding Data Transfer Objects (DTOs) for transactions and products. It is primarily used
 * to convert between response and DTO representations of transaction and product details.
 * This interface leverages the MapStruct library for the implementation.
 *
 * @author Mahmoud Shtayeh
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    /**
     * Maps a {@link BuyDTO} object to a {@link BuyResponse} object.
     *
     * @param buyDTO the {@link BuyDTO} object containing the details of a purchase,
     *               including bought products, total spent, and coin changes.
     * @return a {@link BuyResponse} object representing the mapped purchase details
     * for the response, including bought products, total spent, and coin changes.
     */
    @Mapping(target = "boughtProducts", source = "boughtProducts")
    BuyResponse toResponse(BuyDTO buyDTO);

    /**
     * Converts a {@link ProductDTO} object into a {@link ProductResponse} object.
     * This method maps the fields of the provided ProductDTO to create a response object
     * that can be used in API outputs or service responses.
     *
     * @param productDTO the {@link ProductDTO} containing product details such as ID, name, cost,
     *                   amount, description, and seller ID.
     * @return a {@link ProductResponse} representing the mapped product details as a response object.
     */
    ProductResponse toProductResponse(ProductDTO productDTO);

    /**
     * Converts a list of {@link Product} entities into a list of {@link ProductDTO} objects.
     * This method facilitates the mapping of product entities to their corresponding
     * Data Transfer Object (DTO) representations, which are typically used to transfer
     * data between application layers.
     *
     * @param boughtProducts the list of {@link Product} entities to be converted into a list
     *                       of {@link ProductDTO} objects. Each {@link Product} in the list
     *                       contains details such as ID, name, cost, amount, description, and seller information.
     * @return a list of {@link ProductDTO} objects that represent the mapped data of the input
     * {@link Product} entities.
     */
    List<ProductDTO> toProductDTOs(List<Product> boughtProducts);
}