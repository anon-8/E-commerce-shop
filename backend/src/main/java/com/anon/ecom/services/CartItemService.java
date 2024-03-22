package com.anon.ecom.services;

import com.anon.ecom.domain.dto.CartItemDto;
import com.anon.ecom.domain.entities.CartItemEntity;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    List<CartItemEntity> findAll();
    Optional<CartItemEntity> findOne(Long id);
    CartItemEntity saveOrPartialUpdate(CartItemEntity cartItemEntity);
    void deleteCartItem(Long id);
    CartItemDto cartManipulation(CartItemDto cartItemDto);

}
