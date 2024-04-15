package com.anon.ecom.order.payu.config;

import com.anon.ecom.order.payu.domain.PayUApiCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PayUConfig {

    @Bean
    @Primary
    public PayUApiCredentials payUApiCredentials() {
        return new PayUApiCredentials();
    }
}