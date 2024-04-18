package com.anon.ecom.order.domain;

import com.anon.ecom.config.Mapper;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.domain.OrderEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class OrderMapperImpl implements Mapper<OrderEntity, OrderDto> {
    private final ModelMapper modelMapper;
    @Autowired
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
