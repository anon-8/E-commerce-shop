package com.anon.ecom.order.services;

import com.anon.ecom.cart.domain.CartItemEntity;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.order.OrderRepository;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.domain.OrderEntity;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.config.Mapper;
import com.anon.ecom.cart.CartItemRepository;
import com.anon.ecom.itemCopy.ItemCopyRepository;
import com.anon.ecom.user.services.UserService;
import org.springframework.stereotype.Service;
//import com.anon.ecom.payU.PayU;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemCopyRepository itemCopyRepository;
    private final UserService userService;

    private final Mapper<OrderEntity, OrderDto> orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, CartItemRepository cartItemRepository, ItemCopyRepository itemCopyRepository, UserService userService, Mapper<OrderEntity, OrderDto> orderMapper) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemCopyRepository = itemCopyRepository;
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
    public List<CartItemEntity> getUserCart(Long userId){

        return cartItemRepository.findUserCartItems(userId);
    }
    @Override
    public OrderDto placeOrderFromCart() {
        UserEntity user = userService.getUser();
        List<CartItemEntity> cart = getUserCart(user.getId());

        OrderEntity orderEntity = OrderEntity.builder()
                .buyer(user)
                .status("pending")
                .notifyUrl("https://ecom.com/notify")
                .customerIp("127.0.0.1")
                .description("Digital Key Marketplace")
                .currencyCode("PLN")

                .copies(new ArrayList<>())
                .build();
        orderEntity = orderRepository.save(orderEntity);

        int totalAmount = 0;
        List<ItemCopyEntity> itemCopiesToSave = new ArrayList<>();

        for (CartItemEntity cartItem : cart) {
            ItemEntity item = cartItem.getItem();
            UserEntity seller = cartItem.getSeller();
            BigDecimal price = cartItem.getPrice();
            Integer quantity = cartItem.getQuantity();

            BigDecimal multiplier = BigDecimal.valueOf(100.0);

            totalAmount = totalAmount + (quantity * (price.multiply(multiplier).intValue()));

            List<ItemCopyEntity> itemCopies = itemCopyRepository.findLatestSellOffersByItemIdAndSellerIdAndPrice(item.getId(), seller.getId(), price);

            for (int i = 0; i < quantity; i++) {
                ItemCopyEntity itemCopy = itemCopies.get(i);
                itemCopy.setStatus("pending");
                itemCopy.setOrder(orderEntity);
                itemCopyRepository.save(itemCopy);
            }
            cartItemRepository.delete( cartItem );
        }

        orderEntity.setPaymentId(UUID.randomUUID().toString());
        orderEntity.setCopies(itemCopiesToSave);
        orderEntity.setTotalAmount(totalAmount);
        orderRepository.save(orderEntity);


        return orderMapper.mapTo(orderEntity);
    }
    }

