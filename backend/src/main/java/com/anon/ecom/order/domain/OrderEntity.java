package com.anon.ecom.order.domain;

import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity buyer;

    @CreationTimestamp
    @Column(name = "ordered_at", nullable = false, updatable = false)
    private Date orderedAt;

    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ItemCopyEntity> copies = new ArrayList<>();

    private String notifyUrl;

    private String customerIp;

    private String merchantPosId;

    private String description;

    private String currencyCode;

    private String extOrderId;

    private Integer totalAmount;

    private String paymentId;

}