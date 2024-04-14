package com.anon.ecom.cart.services;

import com.anon.ecom.cart.domain.CartDto;
import com.anon.ecom.cart.domain.CartEntity;

import java.util.List;
import java.util.Optional;

public interface CartService {
    List<CartEntity> findAll();
    Optional<CartEntity> findOne(Long id);
    CartEntity saveOrPartialUpdate(CartEntity cartItemEntity);
    void deleteCartItem(Long id);
    CartDto cartManipulation(CartDto cartDto);

    List<CartEntity> findUserCartItems(Long userId);

}
