package com.anon.ecom.item;

import com.anon.ecom.item.domain.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long>,
        PagingAndSortingRepository<ItemEntity, Long> {

}
