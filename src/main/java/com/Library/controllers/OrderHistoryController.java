package com.Library.controllers;

import com.Library.services.OrderService;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for retrieving order history.
 */
@RestController
@RequestMapping("/v1/order/history")
public class OrderHistoryController {
    private final OrderService orderService;

    @Autowired
    public OrderHistoryController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all orders.
     *
     * @return A response containing a list of all orders.
     */
    @GetMapping
    public ResponseDTO<List<Order>> getAllOrders() {
        return new ResponseDTO<>(HttpStatus.OK.value(), "All orders are listed below.", orderService.getAllOrders());
    }

    /**
     * Retrieves orders made by a specific user.
     *
     * @param userId The ID of the user.
     * @return A response containing a list of orders made by the specified user.
     */
    @GetMapping("/{userId}")
    public ResponseDTO<List<Order>> getSpecificUserOrders(@PathVariable Long userId) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Bellow the orders which made the user by ID: " + userId,
                orderService.getSpecificUserOrders(userId));
    }
}
