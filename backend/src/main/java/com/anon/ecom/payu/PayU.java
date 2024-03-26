package com.anon.ecom.payu;


import com.anon.ecom.order.domain.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class PayU {
    private final PayUApiCredentials configuration;
    private final RestTemplate http;
    @Autowired
    public PayU(PayUApiCredentials configuration, RestTemplate http) {
        this.configuration = configuration;
        this.http = http;
    }
    public OrderCreateResponse handle(OrderDto orderDto) {

        orderDto.setMerchantPosId(configuration.getPosId());
        orderDto.setNotifyUrl(configuration.getNotifyUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");
        headers.add("Authorization", String.format("Bearer %s", getToken()));
        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto, headers);

        ResponseEntity<OrderCreateResponse> response = http.postForEntity(
                getUrl("/api/v2_1/orders"),
                request,
                OrderCreateResponse.class);

        return response.getBody();
    }
    private String getToken() {
        String body = String.format(
                "grant_type=client_credentials&client_id=%s&client_secret=%s",
                configuration.getClientId(),
                configuration.getClientSecret());

        String url = getUrl("/pl/standard/user/oauth/authorize");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        HttpEntity<AccessTokenResponse> response = http.postForEntity(
                url,
                request,
                AccessTokenResponse.class);

        return Objects.requireNonNull(response.getBody()).getAccessToken();
    }
    private String getUrl(String uri) {
        return String.format("%s%s", configuration.getBaseUrl(), uri);
    }

}
