package com.anon.ecom.repositories;

import com.anon.ecom.domain.entities.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long>,
        PagingAndSortingRepository<ItemEntity, Long> {

}
