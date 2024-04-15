package com.anon.ecom.order.domain;

import com.anon.ecom.itemCopy.domain.ItemCopyDto;
import com.anon.ecom.user.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    private UserDto buyer;

    private String paymentId;

    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String extOrderId;
    private String status;
    private Integer totalAmount;

    private List<ItemCopyDto> copies;

}
