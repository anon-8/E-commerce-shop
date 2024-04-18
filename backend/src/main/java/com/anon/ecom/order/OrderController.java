package com.anon.ecom.order;

import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.payu.PayU;
import com.anon.ecom.order.services.OrderService;
import com.anon.ecom.order.payu.domain.OrderCreateResponse;
import com.anon.ecom.order.payu.domain.ReservationDetails;
import com.anon.ecom.order.payu.domain.PaymentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class OrderController {

    private final OrderService orderService;
    private final PayU payU;

    @Autowired
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
