package com.epam.esm.gift_extended.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.service.OrderService;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/{userId}/userOrders")
    public void makeOrder(@PathVariable Integer userId, @RequestBody List<Integer> certIds) {
        orderService.createOrder(userId, certIds);
    }

    @GetMapping(value = "/{userId}/userOrders")
    public List<Order> getUserOrders(@PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return orderService.getOrdersByUserId(userId, page, size, sort);
    }

    @GetMapping(value = "/{orderId}/")
    public Order getOrder(@PathVariable Integer orderId) {
        return orderService.findById(orderId);
    }

    @GetMapping(value = "/")
    public List<Order> getAllOrders(@RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return orderService.allWithPagination(page, size, sort);
    }

}
