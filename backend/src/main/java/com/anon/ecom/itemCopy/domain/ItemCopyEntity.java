package com.anon.ecom.itemCopy.domain;

import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.order.domain.OrderEntity;
import com.anon.ecom.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item_copies")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCopyEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemcopy_id_seq")
    private Long id;

    private String copyKey;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    private String status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private OrderEntity order;
}