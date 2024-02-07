package com.Library.controllers;

import com.Library.services.PurchaseService;
import com.Library.services.dtos.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling purchase operations.
 */
@RestController
@RequestMapping("/v1/purchase")
public class PurchaseController {
    private static final String SUCCESSFULLY_PURCHASE = "Purchase was successfully.";
    private final PurchaseService purchaseItems;

    public PurchaseController(PurchaseService purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    /**
     * Endpoint for initiating a purchase for a specific user.
     * @param userId The ID of the user initiating the purchase.
     * @return A ResponseDTO containing a message indicating the result of the purchase attempt.
     */
    @PostMapping("/userId/{userId}")
    public ResponseDTO<String> purchaseItems(@PathVariable Long userId) {
        try {
            purchaseItems.purchaseItems(userId);
            return new ResponseDTO<>(HttpStatus.OK.value(), SUCCESSFULLY_PURCHASE, null);
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }
}
