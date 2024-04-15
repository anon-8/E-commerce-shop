package com.anon.ecom.order.payu.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payu")
public class PayUApiCredentials {

    private String posId;
    private String secondKey;
    private String clientId;
    private String clientSecret;
    private String baseUrl;
    private String notifyUrl;
}