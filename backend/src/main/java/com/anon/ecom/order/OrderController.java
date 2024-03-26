package com.anon.ecom.order;

import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.services.OrderService;
import com.anon.ecom.payu.OrderCreateResponse;
import com.anon.ecom.payu.*;
import com.anon.ecom.payu.ReservationDetails;
import com.anon.ecom.payu.PaymentData;
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
