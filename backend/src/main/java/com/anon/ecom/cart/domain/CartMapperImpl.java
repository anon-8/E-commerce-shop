package com.anon.ecom.cart.domain;

import com.anon.ecom.config.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartMapperImpl implements Mapper<CartEntity, CartDto> {

    private final ModelMapper modelMapper;
    public CartMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public CartDto mapTo(CartEntity cartItemEntity) {
        return modelMapper.map(cartItemEntity, CartDto.class);
    }
    @Override
    public CartEntity mapFrom(CartDto cartDto) {
        return modelMapper.map(cartDto, CartEntity.class);
    }
}