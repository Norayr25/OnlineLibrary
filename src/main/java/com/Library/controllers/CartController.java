package com.Library.controllers;


import com.Library.services.dtos.CartItemDTO;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.CartService;
import com.Library.services.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling cart operations.
 */
@RestController
@RequestMapping("/v1/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Adds an item to the cart.
     * @param cartItemDTO The DTO containing information about the item to be added.
     * @return A response indicating whether the operation was successful or not.
     */
    @PostMapping("/addItem")
    public ResponseDTO<Item> addItemToCart(@RequestBody CartItemDTO cartItemDTO) {
        try {
            Item item = cartService.addItemToCart(cartItemDTO);
            return new ResponseDTO<>(HttpStatus.OK.value(), "The item was successfully added to the cart", item);
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /**
     * Removes an item from the cart.
     * @param cartItemDTO The DTO containing information about the item to be removed.
     * @return A response indicating whether the operation was successful or not.
     */
    @PostMapping("/removeItem")
    public ResponseDTO<Item> removeItemFromCart(@RequestBody CartItemDTO cartItemDTO) {
        try {
            Item item = cartService.removeItemFromCart(cartItemDTO);
            return new ResponseDTO<>(HttpStatus.OK.value(), "The item was successfully removed from the cart", item);
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }
}