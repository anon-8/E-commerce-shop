package com.anon.ecom.item.domain;

import com.anon.ecom.config.Mapper;
import com.anon.ecom.item.domain.ItemDto;
import com.anon.ecom.item.domain.ItemEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class ItemMapperImpl implements Mapper<ItemEntity, ItemDto> {
    private final ModelMapper modelMapper;
    @Autowired
    public ItemMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public ItemDto mapTo(ItemEntity itemEntity) {
        return modelMapper.map(itemEntity, ItemDto.class);
    }
    @Override
    public ItemEntity mapFrom(ItemDto itemDto) {
        return modelMapper.map(itemDto, ItemEntity.class);
    }
}
