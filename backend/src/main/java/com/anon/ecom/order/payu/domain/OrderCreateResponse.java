package com.anon.ecom.order.payu.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateResponse {

    private String redirectUri;
    private String orderId;
    private String extOrderId;

}