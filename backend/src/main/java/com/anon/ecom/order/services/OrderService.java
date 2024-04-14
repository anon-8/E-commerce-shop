package com.anon.ecom.order.services;

import com.anon.ecom.cart.domain.CartItemEntity;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.domain.OrderEntity;
import com.anon.ecom.user.domain.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    OrderEntity save(OrderEntity orderEntity);
    void delete(Long id);

    CartItemEntity getUserCartItem(Long userId, Long itemId, Long sellerId, BigDecimal price);

    List<CartItemEntity> getUserCart(Long userId);

    OrderDto placeOrderFromCart();

    OrderDto placeOrder(Long itemId, Long sellerId, BigDecimal price);
}
