package com.anon.ecom.itemCopy.services;

import com.anon.ecom.itemCopy.domain.ItemCopyDto;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemCopyService {
    ItemCopyEntity save(ItemCopyEntity itemCopyEntity);
    List<ItemCopyEntity> findAll();
    Page<ItemCopyEntity> findAll(Pageable pageable);
    List<ItemCopyEntity> findAllSellOffersByUserId(Long userId);
    List<ItemCopyEntity> findAllSellOffersByUserIdAndItemId(Long userId, Long itemId);
    Optional<ItemCopyEntity> findOne(Long id);
    void delete(Long id);
    ItemCopyEntity putItemCopyForSale(ItemCopyDto itemCopyDto);
}
