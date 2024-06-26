package com.anon.ecom.order.payu.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDetails {
    String reservationId;
    String paymentUrl;

    public ReservationDetails(String reservationId, String paymentUrl) {

        this.reservationId = reservationId;
        this.paymentUrl = paymentUrl;
    }

}