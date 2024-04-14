package com.anon.ecom.order.services;

import com.anon.ecom.cart.domain.CartItemEntity;
import com.anon.ecom.cart.services.CartService;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.itemCopy.services.ItemCopyService;
import com.anon.ecom.order.OrderRepository;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.domain.OrderEntity;
import com.anon.ecom.order.exceptions.OrderProcessingException;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.config.Mapper;
import com.anon.ecom.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
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
    @Transactional
    public OrderEntity save(OrderEntity orderEntity) {
        try {
            return orderRepository.save(orderEntity);
        } catch (DataAccessException ex) {
            throw new OrderProcessingException("Error saving order entity", ex);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            orderRepository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new OrderProcessingException("Error deleting order with id: " + id, ex);
        }
    }

    @Override
    public List<CartItemEntity> getUserCart(Long userId){
        return cartService.findUserCartItems(userId);
    }

    @Override
    public CartItemEntity getUserCartItem(Long userId, Long itemId, Long sellerId, BigDecimal price){
        return cartService.findUserCartItem(userId, itemId, sellerId, price);
    }

    @Override
    public OrderDto placeOrderFromCart() {
        UserEntity user = userService.getUser();
        List<CartItemEntity> cart = getUserCart(user.getId());
        OrderEntity orderEntity = createOrderEntity(user);
        for (CartItemEntity cartItem : cart) {
            orderEntity = processCartItemOrder(cartItem, orderEntity);
        }
        saveOrderEntity(orderEntity);
        return orderMapper.mapTo(orderEntity);
    }

    @Override
    public OrderDto placeOrder(Long itemId, Long sellerId, BigDecimal price) {
         UserEntity user = userService.getUser();
         CartItemEntity cartItemEntity = getUserCartItem(user.getId(), itemId, sellerId, price);
         OrderEntity orderEntity = createOrderEntity(user);
         OrderEntity processedCartItemOrder = processCartItemOrder(cartItemEntity, orderEntity);
         saveOrderEntity(processedCartItemOrder);
         return orderMapper.mapTo(processedCartItemOrder);
    }

    private OrderEntity createOrderEntity(UserEntity user) {
        OrderEntity orderEntity = OrderEntity.builder()
                .paymentId(UUID.randomUUID().toString())
                .buyer(user)
                .status("pending")
                .notifyUrl("https://ecom.com/notify")
                .customerIp("127.0.0.1")
                .description("Digital Key Marketplace")
                .currencyCode("PLN")
                .copies(new ArrayList<>())
                .totalAmount(0)
                .build();
        saveOrderEntity(orderEntity);
        return orderEntity;
    }

    private OrderEntity processCartItemOrder(CartItemEntity cartItemEntity, OrderEntity orderEntity) {
        ItemEntity item = cartItemEntity.getItem();
        UserEntity seller = cartItemEntity.getSeller();
        BigDecimal price = cartItemEntity.getPrice();
        Integer quantity = cartItemEntity.getQuantity();

        int totalAmount = orderEntity.getTotalAmount() + calculateTotalAmount(price, quantity);
        orderEntity.setTotalAmount(totalAmount);

        List<ItemCopyEntity> itemCopies = findSellOffers(item, seller, price);

        for (ItemCopyEntity itemCopyEntity : itemCopies) {
            itemCopyEntity.setStatus("pending");
            itemCopyEntity.setOrder(orderEntity);
            itemCopyService.save(itemCopyEntity);
            orderEntity.getCopies().add(itemCopyEntity);
        }
        cartService.deleteCartItem(cartItemEntity.getId());
        return orderEntity;
    }

    private List<ItemCopyEntity> findSellOffers(ItemEntity item, UserEntity seller, BigDecimal price) {
        return itemCopyService.findLatestSellOffersByItemIdAndSellerIdAndPrice(item.getId(), seller.getId(), price);
    }

    private int calculateTotalAmount(BigDecimal price, Integer quantity) {
        return quantity * (price.multiply(BigDecimal.valueOf(100.0)).intValue());
    }

    private void saveOrderEntity(OrderEntity orderEntity) {
        orderRepository.save(orderEntity);
    }
}

