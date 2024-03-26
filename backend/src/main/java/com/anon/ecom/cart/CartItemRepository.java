package com.anon.ecom.cart;

import com.anon.ecom.cart.domain.CartItemEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface CartItemRepository extends CrudRepository<CartItemEntity, Long> {
    boolean existsByItemIdAndUserIdAndSellerIdAndPrice(Long itemId, Long userId, Long sellerID, BigDecimal price);
    CartItemEntity findByItemIdAndUserIdAndSellerIdAndPrice(Long itemId, Long userId, Long sellerId, BigDecimal price);
    @Transactional
    @Modifying
    @Query("UPDATE CartItemEntity c SET " +
            "c.quantity = COALESCE(:#{#cartItemEntity.quantity}, c.quantity) " +
            "WHERE c.id = :#{#cartItemEntity.id}")
    void partialUpdate(@Param("cartItemEntity") CartItemEntity cartItemEntity);
    @Query("SELECT a FROM CartItemEntity a WHERE a.user.id = :userId")
    List<CartItemEntity> findUserCartItems(@Param("userId") Long userId);
}
