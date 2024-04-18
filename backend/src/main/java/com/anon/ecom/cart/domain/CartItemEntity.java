package com.anon.ecom.cart.domain;

import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="cart_items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CartItemEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    private BigDecimal price;

    private Integer quantity;

}