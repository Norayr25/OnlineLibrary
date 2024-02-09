package com.Library.controllers;

import com.Library.services.PurchaseService;
import com.Library.services.dtos.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling purchase operations.
 */
@RestController
@RequestMapping("/api/v1/purchase")
@PreAuthorize("hasRole('ROLE_USER')")
public class PurchaseController {
    private static final String SUCCESSFULLY_PURCHASE = "Purchase was successfully. Payment took %s $.";
    private final PurchaseService purchaseItems;

    public PurchaseController(PurchaseService purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    /**
     * Endpoint for initiating a purchase for a specific user.
     * @return A ResponseDTO containing a message indicating the result of the purchase attempt.
     */
    @PostMapping
    public ResponseDTO<String> purchaseItems() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            int totalPrice = purchaseItems.purchaseItems(userEmail);
            return new ResponseDTO<>(HttpStatus.OK.value(), String.format(SUCCESSFULLY_PURCHASE, totalPrice), null);
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }
}
