package com.anon.ecom.cart.domain;

import com.anon.ecom.config.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapperImpl implements Mapper<CartItemEntity, CartItemDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public CartItemMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public CartItemDto mapTo(CartItemEntity cartItemEntity) {
        return modelMapper.map(cartItemEntity, CartItemDto.class);
    }
    @Override
    public CartItemEntity mapFrom(CartItemDto cartItemDto) {
        return modelMapper.map(cartItemDto, CartItemEntity.class);
    }
}