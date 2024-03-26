package com.anon.ecom.cart.services;

import com.anon.ecom.cart.domain.CartItemDto;
import com.anon.ecom.cart.domain.CartItemEntity;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    List<CartItemEntity> findAll();
    Optional<CartItemEntity> findOne(Long id);
    CartItemEntity saveOrPartialUpdate(CartItemEntity cartItemEntity);
    void deleteCartItem(Long id);
    CartItemDto cartManipulation(CartItemDto cartItemDto);

}
