package com.anon.ecom.cart.services;

import com.anon.ecom.cart.domain.CartItemDto;
import com.anon.ecom.cart.domain.CartItemEntity;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.user.domain.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CartService {
    List<CartItemEntity> findAll();
    Optional<CartItemEntity> findOne(Long id);
    CartItemEntity saveOrPartialUpdate(CartItemEntity cartItemEntity);
    void deleteCartItem(Long id);
    CartItemDto cartManipulation(CartItemDto cartItemDto);

    List<CartItemEntity> findUserCartItems(Long userId);

    CartItemEntity findUserCartItem(Long userId, Long itemId, Long sellerId, BigDecimal price);
}
