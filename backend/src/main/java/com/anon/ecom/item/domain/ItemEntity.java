package com.anon.ecom.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="items")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    private Long id;

    private String title;
    private String developer;
    private Integer releaseYear;
    private Integer ageRequired;
    private String platform;

    private String imageUrl = "https://digital.datablitz.com.ph/cdn/shop/products/DATABLITZ_CyberPunk.png?v=1672379201";

    @ElementCollection
    @CollectionTable(name = "item_tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "tag")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<String> tags;

}
