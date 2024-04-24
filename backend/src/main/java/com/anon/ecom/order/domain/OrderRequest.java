package com.anon.ecom.order.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderRequest {

    Long itemId;
    Long sellerId;
    BigDecimal price;

}
