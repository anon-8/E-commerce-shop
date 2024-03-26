package com.anon.ecom.itemCopy.domain;

import com.anon.ecom.config.Mapper;
import com.anon.ecom.itemCopy.domain.ItemCopyDto;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
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