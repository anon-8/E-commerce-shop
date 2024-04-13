package com.anon.ecom.item;

import com.anon.ecom.item.domain.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>,
        PagingAndSortingRepository<ItemEntity, Long> {

}
