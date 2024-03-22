package com.anon.ecom.mappers.impl;

import com.anon.ecom.domain.dto.OrderDto;
import com.anon.ecom.domain.entities.OrderEntity;
import com.anon.ecom.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
@Component
public class OrderMapperImpl implements Mapper<OrderEntity, OrderDto> {
    private final ModelMapper modelMapper;
    public OrderMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public OrderDto mapTo(OrderEntity orderEntity) {
        return modelMapper.map(orderEntity, OrderDto.class);
    }
    @Override
    public OrderEntity mapFrom(OrderDto orderDto) {
        return modelMapper.map(orderDto, OrderEntity.class);
    }
}
