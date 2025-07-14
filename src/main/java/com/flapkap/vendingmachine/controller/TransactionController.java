package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.security.UserPrincipal;
import com.flapkap.vendingmachine.service.TransactionService;
import com.flapkap.vendingmachine.web.RestResponse;
import com.flapkap.vendingmachine.web.request.DepositRequest;
import com.flapkap.vendingmachine.web.response.DepositResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The TransactionController class provides RESTful API endpoints for managing transactions
 * within the system. It is mapped to handle requests at the base path "/api/v1/transactions"
 * with JSON request and response formats.
 *
 * @author Mahmoud Shtayeh
 */
@Tag(name = "Transaction Management", description = "Endpoints for transaction management")
@RestController
@RequiredArgsConstructor
@RequestMapping(
        path = "/api/v1/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class TransactionController {
    /**
     * Service layer dependency responsible for handling business logic related to transactions.
     * This is used within {@link TransactionController} to process and manage transaction-related operations,
     * such as deposits.
     */
    private final TransactionService transactionService;

    /**
     * Processes a deposit transaction by accepting a request containing coins to deposit.
     * Only users with the "BUYER" role are authorized to invoke this method.
     *
     * @param request The deposit request containing a list of coins to be deposited.
     *                Each coin value in the list must be between 5 and 100, inclusive.
     *                The request must not be null and must adhere to the validation constraints.
     * @return A {@link RestResponse} indicating the success status of the deposit operation.
     * The payload is null in the response.
     */
    @Operation(summary = "Make a deposit")
    @PostMapping("/deposit")
    @PreAuthorize("hasRole('BUYER')")
    public RestResponse<DepositResponse> deposit(@Valid @RequestBody final DepositRequest request,
                                                 @AuthenticationPrincipal final UserPrincipal buyer) {
        final Integer balance = transactionService.deposit(buyer.getId(), request.coins());
        return RestResponse.ok(DepositResponse.builder()
                .currentBalance(balance)
                .build());
    }
}