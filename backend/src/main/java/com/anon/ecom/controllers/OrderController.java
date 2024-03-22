package com.anon.ecom.controllers;

import com.anon.ecom.domain.dto.OrderDto;
import com.anon.ecom.payu.OrderCreateResponse;
import com.anon.ecom.payu.*;
import com.anon.ecom.payu.ReservationDetails;
import com.anon.ecom.payu.PaymentData;
import com.anon.ecom.services.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class OrderController {

    private final OrderService orderService;
    private final PayU payU;
    public OrderController(OrderService orderService, PayU payU) {
        this.orderService = orderService;
        this.payU = payU;
    }
    @GetMapping(path = "/place-order-from-cart")
    public ReservationDetails placeOrderFromCart(){

        OrderDto order = orderService.placeOrderFromCart();

        OrderCreateResponse response = payU.handle(order);

        PaymentData payment = new PaymentData(response.getOrderId(), response.getRedirectUri());

        return new ReservationDetails(order.getPaymentId(), payment.getUrl());
    }
}
