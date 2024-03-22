package com.anon.ecom.services;

import com.anon.ecom.domain.dto.OrderDto;
import com.anon.ecom.domain.entities.CartItemEntity;
import com.anon.ecom.domain.entities.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderEntity save(OrderEntity orderEntity);
    void delete(Long id);
    OrderDto placeOrderFromCart();
    List<CartItemEntity> getUserCart(Long userId);
}
