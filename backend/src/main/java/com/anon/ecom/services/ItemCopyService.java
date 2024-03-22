package com.anon.ecom.services;

import com.anon.ecom.domain.dto.ItemCopyDto;
import com.anon.ecom.domain.entities.ItemCopyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemCopyService {
    ItemCopyEntity save(ItemCopyEntity itemCopyEntity);
    List<ItemCopyEntity> findAll();
    Page<ItemCopyEntity> findAll(Pageable pageable);
    List<ItemCopyEntity> findAllByUserId(Long userId);
    List<ItemCopyEntity> findAllByUserIdAndItemId(Long userId, Long itemId);
    Optional<ItemCopyEntity> findOne(Long id);
    void delete(Long id);
    ItemCopyDto putItemCopyForSale(ItemCopyDto itemCopyDto);
}
