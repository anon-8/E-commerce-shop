package com.anon.ecom.order.payu.config;

import com.anon.ecom.order.payu.domain.PayUApiCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayUConfig {

    @Bean
    public PayUApiCredentials payUApiCredentials() {
        return PayUApiCredentials.sandbox();
    }
}