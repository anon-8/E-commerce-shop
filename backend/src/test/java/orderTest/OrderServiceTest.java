package orderTest;

import com.anon.ecom.cart.domain.CartItemEntity;
import com.anon.ecom.cart.services.CartService;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.itemCopy.services.ItemCopyService;
import com.anon.ecom.order.OrderRepository;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.domain.OrderEntity;
import com.anon.ecom.config.Mapper;
import com.anon.ecom.order.services.OrderServiceImpl;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldSaveOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        OrderEntity savedOrder = orderService.save(orderEntity);

        assertNotNull(savedOrder);
        assertEquals(orderEntity, savedOrder);
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void shouldDeleteOrderEntity() {
        Long orderId = 1L;

        orderService.delete(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void shouldGetUserCartItems() {
        Long userId = 1L;
        List<CartItemEntity> cartItems = new ArrayList<>();
        when(cartService.findUserCartItems(userId)).thenReturn(cartItems);

        List<CartItemEntity> result = orderService.getUserCart(userId);

        assertNotNull(result);
        assertEquals(cartItems, result);
        verify(cartService, times(1)).findUserCartItems(userId);
    }

    @Test
    void shouldGetUserCartItem() {
        Long userId = 1L;
        Long itemId = 1L;
        Long sellerId = 1L;
        BigDecimal price = BigDecimal.TEN;
        CartItemEntity cartItem = new CartItemEntity();
        when(cartService.findUserCartItem(userId, itemId, sellerId, price)).thenReturn(cartItem);

        CartItemEntity result = orderService.getUserCartItem(userId, itemId, sellerId, price);

        assertNotNull(result);
        assertEquals(cartItem, result);
        verify(cartService, times(1)).findUserCartItem(userId, itemId, sellerId, price);
    }

}
