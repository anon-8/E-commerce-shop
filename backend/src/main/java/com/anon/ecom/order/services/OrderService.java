package com.anon.ecom.order.services;

import com.anon.ecom.cart.domain.CartEntity;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.domain.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderEntity save(OrderEntity orderEntity);
    void delete(Long id);
    OrderDto placeOrderFromCart();
    List<CartEntity> getUserCart(Long userId);
}
