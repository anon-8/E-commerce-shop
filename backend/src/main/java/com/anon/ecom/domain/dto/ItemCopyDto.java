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
public class ItemCopyDto {
    private Long id;

    private UserDto seller;

    private ItemDto item;

    private String copy_key;

    private BigDecimal price;

}
