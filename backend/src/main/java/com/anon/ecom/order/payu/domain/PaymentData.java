package com.anon.ecom.order.payu.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentData {

    private final String id;
    private final String url;

    public PaymentData(String id, String url) {

        this.id = id;
        this.url = url;
    }
}