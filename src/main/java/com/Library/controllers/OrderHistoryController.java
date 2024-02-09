package com.Library.controllers;

import com.Library.services.OrderService;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for retrieving order history.
 */
@RestController
@RequestMapping("/api/v1/order/history")
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
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
     * @param email The email of the user.
     * @return A response containing a list of orders made by the specified user.
     */
    @GetMapping("/{email}")
    public ResponseDTO<List<Order>> getSpecificUserOrders(@PathVariable String email) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Bellow the orders which made the user by email: " + email,
                orderService.getSpecificUserOrders(email));
    }

    @GetMapping("/topUsers/{count}")
    public ResponseDTO<List<Order>> getTopUsersMadeOrder(@PathVariable Integer count) {
        return new ResponseDTO<>(HttpStatus.OK.value(),
                "Bellow the top " + count + " users which made the most amount of orders.",
                orderService.getTopUsersMadeOrder(count));
    }
}
