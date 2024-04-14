package com.anon.ecom.cart;

import com.anon.ecom.cart.domain.CartEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<CartEntity, Long> {
    boolean existsByItemIdAndUserIdAndSellerIdAndPrice(Long itemId, Long userId, Long sellerId, BigDecimal price);

    CartEntity findByItemIdAndUserIdAndSellerIdAndPrice(Long itemId, Long userId, Long sellerId, BigDecimal price);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE CartEntity c SET " +
            "c.quantity = COALESCE(:#{#cartItemEntity.quantity}, c.quantity) " +
            "WHERE c.id = :#{#cartItemEntity.id}")
    void partialUpdate(@Param("cartItemEntity") CartEntity cartItemEntity);

    @Query("SELECT a FROM CartEntity a WHERE a.user.id = :userId")
    List<CartEntity> findUserCartItems(@Param("userId") Long userId);
}
