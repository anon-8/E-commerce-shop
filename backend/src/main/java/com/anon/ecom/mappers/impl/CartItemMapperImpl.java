package com.anon.ecom.mappers.impl;

import com.anon.ecom.domain.dto.CartItemDto;
import com.anon.ecom.domain.entities.CartItemEntity;
import com.anon.ecom.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapperImpl implements Mapper<CartItemEntity, CartItemDto> {

    private final ModelMapper modelMapper;
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