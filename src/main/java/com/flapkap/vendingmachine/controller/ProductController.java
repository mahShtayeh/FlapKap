package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.mapper.ProductMapper;
import com.flapkap.vendingmachine.security.UserPrincipal;
import com.flapkap.vendingmachine.service.ProductService;
import com.flapkap.vendingmachine.web.RestResponse;
import com.flapkap.vendingmachine.web.request.ProductCreationRequest;
import com.flapkap.vendingmachine.web.request.ProductUpdateRequest;
import com.flapkap.vendingmachine.web.response.ProductCreationResponse;
import com.flapkap.vendingmachine.web.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.flapkap.vendingmachine.config.OpenApiConfig.JWT_SECURITY;

/**
 * The ProductController class provides RESTful API endpoints for managing product-related operations.
 * It is responsible for handling incoming API requests, delegating business logic to the ProductService,
 * and returning appropriate responses.
 *
 * @author Mahmoud Shtayeh
 */
@Tag(name = "Product Management", description = "Endpoints for product management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    /**
     * A service layer dependency responsible for handling business logic related to product operations.
     * This field is used by the ProductController to delegate product management tasks to the corresponding service layer.
     * It abstracts the details of the product-related operations and acts as an intermediary between the controller and other layers.
     */
    private final ProductService productService;

    /**
     * A mapper component responsible for converting product-related objects,
     * such as requests and domain objects, into Data Transfer Objects (DTOs).
     * This dependency is used by the ProductController to facilitate
     * object transformations required for product management operations.
     */
    private final ProductMapper productMapper;

    /**
     * Creates a new product based on the provided creation request.
     *
     * @param request the ProductCreationRequest containing the details of the product to be created
     * @return a RestResponse containing a ProductCreationResponse with the ID of the created product
     */
    @Operation(summary = "Create a new product")
    @SecurityRequirement(name = JWT_SECURITY)
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse<ProductCreationResponse> create(@Valid @RequestBody final ProductCreationRequest request,
                                                        @AuthenticationPrincipal final UserPrincipal seller) {
        final UUID productId = productService.create(productMapper.toDTO(request, seller.getId()));
        return RestResponse.ok(ProductCreationResponse.builder()
                .productId(productId)
                .build());
    }

    /**
     * Retrieves a list of all boughtProducts available in the system.
     *
     * @return a RestResponse containing a list of ProductDTO objects representing all boughtProducts.
     */
    @Operation(summary = "Read all boughtProducts")
    @SecurityRequirement(name = JWT_SECURITY)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<List<ProductResponse>> readAll() {
        return RestResponse.ok(productService.readAll());
    }

    /**
     * Updates specific fields of an existing product.
     *
     * @param productId the unique identifier of the product to be updated
     * @param request   the ProductUpdateRequest containing the new values for the fields to be updated
     * @param seller    the authenticated seller performing the update action
     * @return a RestResponse containing the updated ProductResponse object
     */
    @Operation(summary = "Update fields of a product")
    @SecurityRequirement(name = JWT_SECURITY)
    @PreAuthorize("hasRole('SELLER')")
    @PatchMapping(path = "{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<ProductResponse> update(@PathVariable final UUID productId,
                                                @Valid @RequestBody final ProductUpdateRequest request,
                                                @AuthenticationPrincipal final UserPrincipal seller) {
        final ProductResponse productResponse = productService.update(
                productId,
                productMapper.toDTO(request),
                seller.getId());
        return RestResponse.ok(productResponse);
    }

    /**
     * Deletes a product based on the provided product ID.
     *
     * @param productId the unique identifier of the product to be deleted
     * @return a RestResponse with a void payload indicating that the deletion was successful
     */
    @Operation(summary = "Delete a product")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @SecurityRequirement(name = JWT_SECURITY)
    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping(path = "{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RestResponse<Void> delete(@PathVariable final UUID productId,
                                     @AuthenticationPrincipal final UserPrincipal seller) {
        productService.delete(productId, seller.getId());
        return RestResponse.ok(null);
    }
}