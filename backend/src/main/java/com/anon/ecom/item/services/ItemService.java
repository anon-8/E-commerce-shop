package com.anon.ecom.item.services;

import com.anon.ecom.item.domain.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    ItemEntity save(ItemEntity itemEntity);
    ItemEntity createUpdateItem(Long id, ItemEntity item);
    List<ItemEntity> findAll();
    Page<ItemEntity> findAll(Pageable pageable);

    Optional<ItemEntity> findOne(Long id);
    boolean isExists(Long id);
    ItemEntity partialUpdate(Long id, ItemEntity itemEntity);
    void delete(Long id);

}
