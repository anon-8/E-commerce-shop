package com.anon.ecom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long id;

    private UserDto user;

    private ItemDto item;

    private UserDto seller;

    private BigDecimal price;

    private Integer quantity;
}