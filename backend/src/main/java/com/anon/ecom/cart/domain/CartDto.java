package com.anon.ecom.cart.domain;

import com.anon.ecom.item.domain.ItemDto;
import com.anon.ecom.user.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long id;

    private UserDto user;

    private ItemDto item;

    private UserDto seller;

    private BigDecimal price;

    private Integer quantity;
}