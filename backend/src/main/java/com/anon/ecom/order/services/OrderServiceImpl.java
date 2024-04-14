package com.anon.ecom.order.services;

import com.anon.ecom.cart.domain.CartEntity;
import com.anon.ecom.cart.services.CartService;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.itemCopy.services.ItemCopyService;
import com.anon.ecom.order.OrderRepository;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.domain.OrderEntity;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.config.Mapper;
import com.anon.ecom.user.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;
    private final ItemCopyService itemCopyService;
    private final Mapper<OrderEntity, OrderDto> orderMapper;

    public OrderServiceImpl(UserService userService, Mapper<OrderEntity, OrderDto> orderMapper, ItemCopyService itemCopyService, OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.itemCopyService = itemCopyService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }
    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<CartEntity> getUserCart(Long userId){
        return cartService.findUserCartItems(userId);
    }

    @Override
    public OrderDto placeOrderFromCart() {
        UserEntity user = userService.getUser();
        List<CartEntity> cart = getUserCart(user.getId());

        OrderEntity orderEntity = OrderEntity.builder()
                .buyer(user)
                .status("pending")
                .notifyUrl("https://ecom.com/notify")
                .customerIp("127.0.0.1")
                .description("Digital Key Marketplace")
                .currencyCode("PLN")

                .copies(new ArrayList<>())
                .build();
        orderEntity = save(orderEntity);

        int totalAmount = 0;
        List<ItemCopyEntity> itemCopiesToSave = new ArrayList<>();

        for (CartEntity cartItem : cart) {
            ItemEntity item = cartItem.getItem();
            UserEntity seller = cartItem.getSeller();
            BigDecimal price = cartItem.getPrice();
            Integer quantity = cartItem.getQuantity();

            BigDecimal multiplier = BigDecimal.valueOf(100.0);

            totalAmount = totalAmount + (quantity * (price.multiply(multiplier).intValue()));

            List<ItemCopyEntity> itemCopies = itemCopyService.findLatestSellOffersByItemIdAndSellerIdAndPrice(item.getId(), seller.getId(), price);

            for (int i = 0; i < quantity; i++) {
                ItemCopyEntity itemCopy = itemCopies.get(i);
                itemCopy.setStatus("pending");
                itemCopy.setOrder(orderEntity);
                itemCopyService.save(itemCopy);
            }
            cartService.deleteCartItem(cartItem.getId());
        }

        orderEntity.setPaymentId(UUID.randomUUID().toString());
        orderEntity.setCopies(itemCopiesToSave);
        orderEntity.setTotalAmount(totalAmount);
        orderRepository.save(orderEntity);


        return orderMapper.mapTo(orderEntity);
    }
}

