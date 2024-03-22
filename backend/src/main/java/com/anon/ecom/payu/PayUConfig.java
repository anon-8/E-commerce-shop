package com.anon.ecom.payu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayUConfig {

    @Bean
    public PayUApiCredentials payUApiCredentials() {
        return PayUApiCredentials.sandbox();
    }
}