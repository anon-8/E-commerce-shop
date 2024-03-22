package com.anon.ecom.mappers.impl;

import com.anon.ecom.domain.dto.ItemCopyDto;
import com.anon.ecom.domain.entities.ItemCopyEntity;
import com.anon.ecom.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemCopyMapperImpl implements Mapper<ItemCopyEntity, ItemCopyDto> {
    private final ModelMapper modelMapper;

    public ItemCopyMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ItemCopyDto mapTo(ItemCopyEntity itemCopyEntity) {
        return modelMapper.map(itemCopyEntity, ItemCopyDto.class);
    }
    @Override
    public ItemCopyEntity mapFrom(ItemCopyDto itemCopyDto) {
        return modelMapper.map(itemCopyDto, ItemCopyEntity.class);
    }
}