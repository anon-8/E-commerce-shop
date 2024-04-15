package com.anon.ecom.itemCopy;

import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ItemCopyRepository extends CrudRepository<ItemCopyEntity, Long>, JpaRepository<ItemCopyEntity, Long> {

    @Query("SELECT a FROM ItemCopyEntity a WHERE a.seller.id = :sellerId AND a.item.id = :itemId AND a.price = :price AND a.status='for sale'")
    List<ItemCopyEntity> findAllSellOffersByItemIdAndSellerIdAndPrice(@Param("sellerId") Long sellerId,
                                                                      @Param("itemId") Long itemId,
                                                                      @Param("price") BigDecimal price);

    @Query("SELECT a FROM ItemCopyEntity a WHERE a.item.id = :itemId AND a.status='for sale'")
    List<ItemCopyEntity> findAllSellOffersByItemId(@Param("itemId") Long itemId);

    @Query("SELECT a FROM ItemCopyEntity a WHERE a.seller.id = :sellerId AND a.status='for sale'")
    List<ItemCopyEntity> findAllSellOffersBySellerId(@Param("sellerId") Long sellerId);
    @Query("SELECT a FROM ItemCopyEntity a WHERE a.seller.id = :sellerId AND a.item.id = :itemId AND a.status='for sale'")
    List<ItemCopyEntity> findAllSellOffersBySellerIdAndItemId(@Param("sellerId") Long sellerId,
                                                              @Param("itemId") Long itemId);
    @Query("SELECT a FROM ItemCopyEntity a WHERE a.seller.id = :sellerId AND a.item.id = :itemId AND a.price = :price AND a.status = 'for sale' ORDER BY a.createdAt")
    List<ItemCopyEntity> findOldestSellOffersByItemIdAndSellerIdAndPrice(
            @Param("itemId") Long itemId,
            @Param("sellerId") Long sellerId,
            @Param("price") BigDecimal price
    );


}
