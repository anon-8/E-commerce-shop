package com.anon.ecom.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item_copies")
public class ItemCopyEntity {

    @Id
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
    private OrderEntity order;
}